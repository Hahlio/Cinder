from django.db import models

import ast, json

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

    def createProfile(x):
        x = x.decode("utf-8")
        x = json.loads(x)
        temp = Profile(name=x["name"],username=x["username"],lat=x["lat"],lng=x["lng"],school=x["school"],courses=x["courses"],preferences=x["preferences"],interests=x["interests"])
        temp.save()
        retval = {}
        retval["id"] = temp.id
        return retval

    def modifyProfile(x, profile_id):
        p = Profile.objects.get(pk=profile_id)
        x = x.decode("utf-8")
        x = json.loads(x)
        p.name=x["name"]
        p.username=x["username"]
        p.lat=x["lat"]
        p.lng=x["lng"]
        p.school=x["school"]
        p.courses=x["courses"]
        p.preferences=x["preferences"]
        p.interests=x["interests"]
        p.save()
        retval = {}
        #retval["id"] = temp.id
        return p.inJson()

    def findID(user):
        retval = {}
        try:
            p = Profile.objects.get(username=user)
            retval["id"] = p.id
        except:
            retval["id"] = -1
        return retval
