from django.db import models

from django.db import models
from userprofile.models import Profile
from Matchmaking.models import Match
from django.db.models import Q
from django.core.exceptions import ObjectDoesNotExist, MultipleObjectsReturned

class Message(models.Model):
    sender = models.ForeignKey(Profile, related_name='sender', on_delete=models.DO_NOTHING)
    match = models.ForeignKey(Match, related_name='reciever', on_delete=models.DO_NOTHING)
    group = models.ForeignKey(Match, related_name='group', on_delete=models.DO_NOTHING)
    message = models.TextField()
    groupMessage = models.BooleanField(default=False)
    timestamp = models.DateTimeField('date sent', auto_now_add=True)

    def __str__(self):
        return self.message

    def toJson(self):
        retval = {}
        retval["sender"] = self.sender.id
        retval["message"] = self.message
        retval["timestamp"] = self.timestamp
        return retval

def createMessage(senderid, recieverid, message, groupbool):
    retval = {}
    try:
        sender = Profile.objects.get(pk=senderid)
        reciever = Profile.objects.get(pk=recieverid)
        match = Match.objects.all().filter(Q(user1__exact=sender)|Q(user2__exact=reciever)).filter(Q(user1__exact=sender)|Q(user2__exact=reciever)).first()
        # TODO: Change the group param
        msg = Message(sender=sender, match=match, group=match, message=message, groupMessage=groupbool)
        msg.save()
        retval["id"] = msg.id
    except ObjectDoesNotExist:
        retval["id"] = -1
    except MultipleObjectsReturned:
        retval["id"] = -2
    return retval

def smessageLog(senderid, recieverid, numMsg):
    """
    Gets the message log for a match (not group)
        matchid:    the messages you want to check for the match
        numMsg:     the number of message you want returned
    """
    retval = {}
    messageLog = []
    sender = Profile.objects.get(pk=senderid)
    reciever = Profile.objects.get(pk=recieverid)
    match = Match.objects.all().filter(Q(user1__exact=sender)|Q(user2__exact=reciever)).filter(Q(user1__exact=sender)|Q(user2__exact=reciever)).first()
    messageList = Message.objects.all().filter(match__exact=match).order_by('-timestamp')[:numMsg]
    for eachMessage in messageList:
        messageLog.append(eachMessage.id)
    retval["messages"] = messageLog
    return retval

def gmessageLog(senderid, groupid, numMsg):
    # TODO: Implement this function
    return 0

def getMessage(msgid):
    message = Message.objects.get(pk=msgid)
    return message
