# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import math

from django.db import models
from django.db.models import Q
from userprofile.models import Profile
from django.core.exceptions import ObjectDoesNotExist

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
            M1 = Match(user1=userPro, user2=profile, score=0, user1HasMatched=False, user2HasMatch = False, user1accepted=False, user2accepted=False)
            M1.generate()
            M1.save()
    return 0

def matchAccept(userid, match, acceptance):
    userProfile = Profile.objects.get(pk=userid)

    if match.user1 == userProfile:
        match.user1accepted = acceptance
        match.user1HasMatched = True

        if match.user2HasMatched:
            if match.user2accepted:
                # Notify User2 of a match;
                match.hasMatched = True
    else:
        match.user2accepted = acceptance
        match.user2HasMatched = True

        if match.user1HasMatched:
            if match.user1accepted:
                # Notify user1 of a match;
                match.hasMatched = True
    match.save()

def createGroup(groupName, profile_id):
    currUser = Profile.objects.get(pk=profile_id)
    newMatch = Match(group_name= groupName, hasMatched = True, user1HasMatched = True)
    newMatch.save()
    newMatch.group_members.add(currUser)
    newMatch.save()
    retval = {}
    retval["GroupID"] = newMatch.id
    return retval

def groupAdd(userMatchID, matchID, profile_id):
    group = Match.objects.get(pk=matchID)
    userMatch = Match.objects.get(pk=userMatchID)
    currUser = Profile.objects.get(pk=profile_id)
    newMember = userMatch.returnOtherMatch(currUser)
    group.group_members.add(newMember)
    userMatch.save()
    #notify group member

def groupLeave(userID, matchID):
    try:
        group = Match.objects.get(pk=matchID)
        leavingMember = Profile.objects.get(pk=userID)
        group.group_members.remove(leavingMember)
        groupList = group.group_members
        if groupList.exists():
            group.save()
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



