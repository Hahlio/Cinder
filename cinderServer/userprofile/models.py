from django.db import models
from django.core.exceptions import ObjectDoesNotExist

import json

class Profile(models.Model):
    name = models.CharField(default="John Doe", max_length=100)
    username = models.CharField(default="user",max_length=100)
    lat = models.FloatField()
    lng = models.FloatField()
    school = models.CharField(default="none", max_length=100)
    courses = models.TextField()
    preferences = models.TextField()
    interests = models.TextField()

    def __str__(self):
        return self.name

    def inJson(self):
        retval = {}
        retval["name"] = self.name
        retval["username"] = self.username
        retval["lat"] = self.lat
        retval["lng"] = self.lng
        retval["school"] = self.school
        retval["courses"] = self.courses
        retval["preferences"] = self.preferences
        retval["interests"] = self.interests
        return retval

def createProfile(jsonArguments):
    jsonArguments = jsonArguments.decode("utf-8")
    args = json.loads(jsonArguments)
    temp = Profile(name=args["name"],username=args["username"],lat=args["lat"],lng=args["lng"],school=args["school"],courses=args["courses"],preferences=args["preferences"],interests=args["interests"])
    temp.save()
    retval = {}
    retval["id"] = temp.id
    return retval

def findID(user):
    retval = {}
    try:
        p = Profile.objects.get(username=user)
        retval["id"] = p.id
    except ObjectDoesNotExist:
        retval["id"] = -1
    return retval

def modifyProfile(jsonArgs, profile_id):
    prof = Profile.objects.get(pk=profile_id)
    jsonArgs = jsonArgs.decode("utf-8")
    args = json.loads(jsonArgs)
    prof.name=args["name"]
    prof.username=args["username"]
    prof.lat=args["lat"]
    prof.lng=args["lng"]
    prof.school=args["school"]
    prof.courses=args["courses"]
    prof.preferences=args["preferences"]
    prof.interests=args["interests"]
    prof.save()
    return prof.inJson()