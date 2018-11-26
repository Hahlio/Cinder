import datetime
from django.db import models
from userprofile.models import Profile
from Matchmaking.models import Match, groupMembers
from django.db.models import Q
from django.core.exceptions import ObjectDoesNotExist, MultipleObjectsReturned
from fcm_django.models import FCMDevice

class Message(models.Model):
    sender = models.ForeignKey(Profile, related_name='sender', on_delete=models.DO_NOTHING)
    matchID = models.ForeignKey(Match, related_name='matches', on_delete=models.DO_NOTHING)
    message = models.TextField(default="Test",null=True, blank=True)
    timestamp = models.DateTimeField('date sent', auto_now_add=True)

    def __str__(self):
        return self.message

    def toJson(self):
        retval = {}
        retval["sender"] = self.sender.id
        retval["message"] = self.message
        retval["timestamp"] = self.timestamp
        return retval

def createMessage(senderid, matchid, message, isGroup):
    retval = {}
    try:
        sender = Profile.objects.get(pk=senderid)
        match = Match.objects.get(pk=matchid)
        msg = Message(sender=sender, matchID = match, message=message)
        msg.save()
        if isGroup:
            group = groupMembers(matchid)
            for member in group:
                #notify all group members they have a message
                print(member.id)
                userDevice = FCMDevice()
                userDevice.registration_id = member.deviceID
                userDevice.type = "Android"
                notificationMsg = match.group_name + " has recieved a new message"
                if member.notification:
                    userDevice.send_message(title="New Message", body=notificationMsg)
                userDevice.send_message(data={"title" : "New Message", "body" : notificationMsg})
        else:
            #notify user they have a message
            userDevice = FCMDevice()
            userDevice.registration_id = match.returnOtherMatch(sender).deviceID
            userDevice.type = "Android"
            notificationMsg = sender.name + " has sent you a message"
            if match.returnOtherMatch(sender).notification:
                userDevice.send_message(title="New Message", body=notificationMsg)
            userDevice.send_message(data={"title" : "New Message", "body" : notificationMsg})
        retval["id"] = msg.id
    except ObjectDoesNotExist:
        retval["id"] = -1
    except MultipleObjectsReturned:
        retval["id"] = -2
    return retval

def messageLog(matchid):
    """
    Gets the message log for a match (not group)
        matchid:    the messages you want to check for the match
        numMsg:     the number of message you want returned
    """
    retval = {}
    messages = []
    users = []
    userID = []
    timestamps = []
    chatMatchId = Match.objects.get(pk=matchid)
    messageList = Message.objects.all().filter(matchID__exact=chatMatchId).order_by('-timestamp')
    for eachMessage in messageList:
        messages.append(eachMessage.message)
        users.append(eachMessage.sender.name)
        userID.append(eachMessage.sender.id)
        timestamps.append((eachMessage.timestamp-datetime.timedelta(hours=8)).strftime('%m/%d/%Y \n%I:%M %p'))
    retval["messages"] = messages
    retval["users"] = users
    retval["userID"] = userID
    retval["timestamps"] = timestamps
    return retval
