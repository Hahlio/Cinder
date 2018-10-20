# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

import ast

class Profile(models.Model):
    name = models.CharField(max_length=100)
    lat = models.FloatField()
    lng = models.FloatField()
    school = models.CharField(max_length=100)
    courses = models.TextField()
    preferences = models.TextField()
    interests = models.TextField()

    def __str__(self):
        return self.name

    def inJson(self):
        retval = {}
        retval["name"] = self.name
        retval["lat"] = self.lat
        retval["lng"] = self.lng
        retval["school"] = self.school
        retval["courses"] = self.courses
        retval["preferences"] = self.preferences
        retval["interests"] = self.interests
        return retval

    def createProfile(x):
        x = x.decode("utf-8")
        x = ast.literal_eval(x)
        temp = Profile(name=x["name"],lat=x["lat"],lng=x["lng"],school=x["school"],courses=x["courses"],preferences=x["preferences"],interests=x["interests"])
        temp.save()
        retval = {}
        retval["id"] = temp.id
        return retval

class Match(models.Model):
    user1 = models.ForeignKey(Profile, related_name='user1', on_delete=models.DO_NOTHING)
    user2 = models.ForeignKey(Profile, related_name='user2', on_delete=models.DO_NOTHING)
    score = models.IntegerField()

    def generate(profile_id):
        return 0;

class Message(models.Model):
    sender = models.ForeignKey(Profile, related_name='sender', on_delete=models.DO_NOTHING)
    reciever = models.ForeignKey(Profile, related_name='reciever', on_delete=models.DO_NOTHING)
    message = models.TextField()
    timestamp = models.DateTimeField('date sent')
    def __str__(self):
        return self.message
