# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from django.db.models import Q
from userprofile.models import Profile
import math

import ast, json

class Match(models.Model):
    user1 = models.ForeignKey(Profile, related_name = "user1", on_delete=models.DO_NOTHING)
    user2 = models.ForeignKey(Profile, related_name = "user2", on_delete=models.DO_NOTHING)
    score = models.IntegerField(default = 0)
    # checks if this person has seen this match before
    hasMatched = models.BooleanField(default=False)
    # checks if this person accepted or declined the match.
    # false also means user blocked.
    accepted = models.BooleanField(default=False)

    # used when creating a new user.
    def createMatch(profile):
        #grabs all users
        profile_set1 = Profile.objects.all()
        for userPro in profile_set1:
            if userPro.id != profile.id:
                M1 = Match(user1 = userPro, user2 = profile, score = 0, hasMatched = False, accepted = False)
                M1.generate()
                M1.save()
        return 0

    def updateMatch(profile):
        matchList = Match.objects.all().filter(Q(user1__exact=profile)|Q(user2__exact=profile)).filter(hasMatched=False)
        for matches in matchList:
                matches.generate()
                matches.save()
        return 0

    def isMatch(profile):
    	if (user1 == profile) or (user2 == profile):
    		return True
    	else:
    		return False

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
            if self.user1.preferences == self.user2.preferences:
                self.score += 30
    		# Arbitary interest match score
            if self.user1.interests == self.user2.preferences:
                self.score+= 10

    		# distance formula based on longitude/latitude
            dlon = self.user1.lng - self.user2.lng
            dlat = self.user1.lat - self.user2.lat
            a = (math.sin(math.radians(dlat/2)))**2 + math.cos(math.radians(self.user1.lat)) * math.cos(math.radians(self.user1.lat)) * (math.sin(math.radians(dlon/2)))**2
            c = 2 * math.atan2( math.sqrt(a), math.sqrt(1-a) )
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

    def returnListOfMatches(profile_id):
        p = Profile.objects.get(pk=profile_id)

        # Grabs all that are not matched, score greater than 0, ordered by decreasing order.
        # give only last 5.
        retVal = {}
        profileList = []

        # 
        matchList = Match.objects.all().filter(Q(user1__exact=p)|Q(user2__exact=p)).filter(hasMatched=False).filter(score__gte=0).order_by('-score')[:5]
        for eachMatch in matchList:
            profileList.append(eachMatch.returnOtherMatch(p).id)
        	#profileList.append(Match.returnOtherMatch(p))

        retVal["Matches"] = profileList
        #matchList.returnOtherMatch(p).id
        return retVal


    def returnMatch(x):
        p1 = Profile.objects.get(pk=x["user1"])
        p2 = Profile.objects.get(pk=x["user2"])

        match = Match.objects.all().filter(Q(user1__exact=p1)|Q(user2__exact=p1)).filter(Q(user1__exact=p2)|Q(user2__exact=p2)).first()

        return match
