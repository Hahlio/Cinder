# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import math

from django.db import models
from django.db.models import Q
from userprofile.models import Profile
from django.core.exceptions import ObjectDoesNotExist
from fcm_django.models import FCMDevice

class Match(models.Model):
    user1 = models.ForeignKey(Profile, null=True, blank=True, related_name="user1", on_delete=models.CASCADE)
    user2 = models.ForeignKey(Profile, null=True, blank=True, related_name="user2", on_delete=models.CASCADE)
    score = models.IntegerField(default=-1)
    # checks if both users has accepted or rejected
    user1HasMatched = models.BooleanField(default=False)
    user2HasMatched = models.BooleanField(default=False)

    hasMatched = models.BooleanField(default=False)
    # checks if this person accepted or declined the match.
    # false also means user blocked.
    user1accepted = models.BooleanField(default=False)
    user2accepted = models.BooleanField(default=False)

    group_members = models.ManyToManyField(Profile)
    group_name = models.CharField(default="NULL", max_length=100)

    def returnOtherMatch(self, profile):
        if self.user1 == profile:
            return self.user2
        else:
            return self.user1

    def generate(self):
        self.score = 0
        if not set(self.user1.courses.split(',')).isdisjoint(self.user2.courses.split(',')):
    		# Arbitary School match score
            if self.user1.school == self.user2.school:
                self.score += 50
    		# Arbitary preference match score
            preferences = len(set(self.user1.preferences.split(',')) & set(self.user2.preferences.split(',')))
            self.score += 30 * preferences

    		# Arbitary interest match score
            interests = len(set(self.user1.interests.split(',')) & set(self.user2.interests.split(',')))
            self.score += 10 * interests

    		# distance formula based on longitude/latitude
            dlon = self.user1.lng - self.user2.lng
            dlat = self.user1.lat - self.user2.lat
            a = (math.sin(math.radians(dlat/2)))**2 + math.cos(math.radians(self.user1.lat)) * math.cos(math.radians(self.user1.lat)) * (math.sin(math.radians(dlon/2)))**2
            c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
    		# 6371 is raidus of earth in KM
    		# d is kilometers apart.
            d = c * 6371

    		# Arbitary Distance score
            Rate = -0.2
    		# Exponential decay function
            distanceScore = 50 * math.exp(Rate*d)
            self.score += distanceScore

        else:
            self.score = -1000

        return self.score

def updateMatch(profile):
    matchList = Match.objects.all().filter((Q(user1__exact= profile) & Q(user1HasMatched = False))|(Q(user2__exact= profile) & Q(user2HasMatched = False)))
    for matches in matchList:
        matches.generate()
        matches.save()
    return 0

def returnMatch(request):
    p1 = Profile.objects.get(pk=request["user1"])
    p2 = Profile.objects.get(pk=request["user2"])

    match = Match.objects.all().filter(Q(user1__exact=p1)|Q(user2__exact=p1)).filter(Q(user1__exact=p2)|Q(user2__exact=p2)).first()

    return match

def returnListOfMatches(profile_id):
    p = Profile.objects.get(pk=profile_id)

    # Grabs all that are not matched, score greater than 0, ordered by decreasing order.
    # give only last 5.
    retVal = {}
    profileList = []

    matchList = Match.objects.all().filter((Q(user1__exact=p) & Q(user1HasMatched = False))|(Q(user2__exact=p) & Q(user2HasMatched = False))).filter(score__gte=0).order_by('-score')[:5]
    for eachMatch in matchList:
        profileList.append(eachMatch.returnOtherMatch(p).id)

    retVal["Matches"] = profileList
    return retVal

 # used when creating a new user.
def createMatch(profile):
    #grabs all users
    profile_set1 = Profile.objects.all()
    for userPro in profile_set1:
        if userPro.id != profile.id:
            M1 = Match(user1=userPro, user2=profile, score=0, user1HasMatched=False, user2HasMatched = False, user1accepted=False, user2accepted=False)
            M1.generate()
            M1.save()
    return 0

def matchAccept(userid, match, acceptance):
    userProfile = Profile.objects.get(pk=userid)
    otherUser = match.returnOtherMatch(userProfile)

    if match.user1 == userProfile:
        match.user1accepted = acceptance
        match.user1HasMatched = True

        if match.user2HasMatched:
            if match.user2accepted:
                match.hasMatched = True
                # Notify User2 of a match;
                user2device = FCMDevice()
                user2device.registration_id = otherUser.deviceID
                user2device.type = "Android"
                notificationMsg = "A new match has been found for you!"
                if otherUser.notification:
                    user2device.send_message(title="New Match!", body=notificationMsg)
                user2device.send_message(data={"title" : "New Match!", "body" : notificationMsg})
                
                
    else:
        match.user2accepted = acceptance
        match.user2HasMatched = True

        if match.user1HasMatched:
            if match.user1accepted:
                match.hasMatched = True
                # Notify user1 of a match;
                user1device = FCMDevice()
                user1device.registration_id = otherUser.deviceID
                user1device.type = "Android"
                notificationMsg = "A new match has been found for you!"
                if otherUser.notification:
                    user1device.send_message(title="New Match!", body=notificationMsg)
                user1device.send_message(data={"title" : "New Match!", "body" : notificationMsg})
                
    match.save()

def createGroup(groupName, profile_id):
    currUser = Profile.objects.get(pk=profile_id)
    newMatch = Match(group_name= groupName, hasMatched = True, user1HasMatched = True)
    newMatch.save()
    newMatch.group_members.add(currUser)
    newMatch.save()
    retval = {}
    retval["matchid"] = newMatch.id
    return retval

def groupAdd(userMatchID, matchID, profile_id):
    group = Match.objects.get(pk=matchID)
    userMatch = Match.objects.get(pk=userMatchID)
    currUser = Profile.objects.get(pk=profile_id)
    newMember = userMatch.returnOtherMatch(currUser)
    if not group.group_members.all().filter(id=newMember.id).count():
        group.group_members.add(newMember)
        userMatch.save()

        #notify group member
        userDevice = FCMDevice()
        userDevice.registration_id = newMember.deviceID
        userDevice.type = "Android"
        notificationMsg = currUser.name + " has added you into Group: " + group.group_name
        if newMember.notification:
            userDevice.send_message(title="Group Invite", body=notificationMsg)
        userDevice.send_message(data={"title" : "Group Invite", "body" : notificationMsg})
    

def groupLeave(userID, matchID):    
    try:
        group = Match.objects.get(pk=matchID)
        leavingMember = Profile.objects.get(pk=userID)
        group.group_members.remove(leavingMember)
        groupList = group.group_members
        if groupList.exists():
            group.save()
            for member in group.group_members.all():
                #notify all group members they have a message
                print(member.id)
                userDevice = FCMDevice()
                userDevice.registration_id = member.deviceID
                userDevice.type = "Android"
                notificationMsg = leavingMember.name + " has left the Group " + group.group_name
                if member.notification:
                    userDevice.send_message(title="Group Notification", body=notificationMsg)
                userDevice.send_message(data={"title" : "Group Notification", "body" : notificationMsg})
        else:
            group.delete()
    except ObjectDoesNotExist:
        pass

def groupMembers(matchID):
    group = Match.objects.get(pk=matchID)
    return group.group_members.all()

def returnContacts(userID):
    userProfile = Profile.objects.get(pk=userID)

    retval = {}
    nameList = []
    idList = []
    contactList = Match.objects.all().filter(Q(user1__exact=userProfile)|Q(user2__exact=userProfile)).filter(hasMatched = True)
    for eachMatch in contactList:
        nameList.append(eachMatch.returnOtherMatch(userProfile).name)
        idList.append(eachMatch.id)
    retval["users"] = nameList
    retval["matchID"] = idList
    return retval

def removeContact(matchID, userID):
    try:
        currMatch = Match.objects.get(pk=matchID)
        currUser = Profile.objects.get(pk=userID)
        if currUser == currMatch.user1 or currUser == currMatch.user2:
            currMatch.hasMatched = False
            currMatch.save()
            return True
        else:
            return False
    except ObjectDoesNotExist:
        return False


def returnGroups(userID):
    currUser = Profile.objects.get(pk=userID)

    retval = {}
    groupList = []
    idList = []

    matchedGroups = Match.objects.all().filter(hasMatched = True).filter(user2HasMatched = False).filter(group_members__id = currUser.id)
    for group in matchedGroups:
        groupList.append(group.group_name)
        idList.append(group.id)

    retval["groups"] = groupList
    retval["matchID"] = idList
    return retval

def returnGroupContacts(userID, matchID):
    userProfile = Profile.objects.get(pk=userID)

    retval = {}
    listoProfiles = []
    nameList = []
    idList = []
    contactList = Match.objects.all().filter(Q(user1__exact=userProfile)|Q(user2__exact=userProfile)).filter(hasMatched = True)
    for matches in contactList:
        listoProfiles.append(matches.returnOtherMatch(userProfile))

    groupMems = groupMembers(matchID)
    notInList = set(listoProfiles).difference(set(groupMems))
    for members in notInList:
        nameList.append(members.name)
        idList.append(Match.objects.all().filter(Q(user1__exact=userProfile)|Q(user2__exact=userProfile)).filter(Q(user1__exact=members)|Q(user2__exact=members)).first().id)
    retval["users"] = nameList
    retval["matchID"] = idList
    return retval

