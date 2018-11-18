import json
import hashlib
import time

from django.db import models
from django.core.exceptions import ObjectDoesNotExist, MultipleObjectsReturned

class Profile(models.Model):
    name = models.CharField(default="John Doe", max_length=100)
    username = models.CharField(default="user", max_length=100)
    password = models.CharField(default="1234", max_length=100)
    loggedin = models.BooleanField(default=False)
    currentHash = models.CharField(default="abcedfghijklmnop", max_length=100)
    lat = models.FloatField()
    lng = models.FloatField()
    school = models.CharField(default="none", max_length=100)
    courses = models.TextField()
    preferences = models.TextField()
    interests = models.TextField()

    def __str__(self):
        return self.name

    def inJson(self):
        """
        Returns the profile object in a JSON representation
        """
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

    def login(self, args):
        """
        Checks if the user credentials match the ones stored on the server
            json_arg: the arguments of the function
                MUST CONTAIN: password - the password to the user's account
                MUST CONTAIN: deviceid - the unique deviceid
        Returns dictionary with success true and a hashcode to authenticate further actions if
        logged in else returns success false
        """
        retval = {}
        retval["success"] = False
        retval["hash"] = -1
        if self.password == args["password"]:
            hashed = hashlib.sha1()
            hashed.update(str(time.time()).encode('utf-8'))
            self.currentHash = hashed.hexdigest()
            retval["success"] = True
            retval["hash"] = self.currentHash
            hashed = hashlib.sha1()
            hashed.update(str(str(self.currentHash) + args["deviceid"]).encode('utf-8'))
            self.loggedin = True
            self.currentHash = hashed.hexdigest()
            self.save()
        return retval

    def authenticate(self, hashcode, deviceid):
        """
        Checks if the user is logged in and has correct hashcode
            hashcode: the hashcode they get during login
            deviceid: the device they are currently logged in from
        Returns true if logged in with correct credentials and false otherwise
        """
        check = hashlib.sha1()
        check.update(str(hashcode + deviceid).encode('utf-8'))
        if self.loggedin and self.currentHash == check.hexdigest():
            return True
        return False

    def logout(self, hashcode, deviceid):
        """
        Logs the user out
            hashcode: the hashcode they get during login
            deviceid: the device they are currently logged in from
        returns dictionary with success true if successful otherwise false
        """
        retval = {}
        retval["success"] = False
        check = hashlib.sha1()
        check.update(str(hashcode + deviceid).encode('utf-8'))
        if self.loggedin and self.currentHash == check.hexdigest():
            self.loggedin = False
            retval["success"] = True
            self.save()
        return retval

def validID(userID):
    """
    Checks if the ID is valid and exists inside the database
    userID: the ID you want to check
    """
    try:
        Profile.objects.get(pk=userID)
        return True
    except ObjectDoesNotExist:
        return False

def findID(user):
    retval = {}
    try:
        p = Profile.objects.get(username=user)
        retval["id"] = p.id
    except ObjectDoesNotExist:
        retval["id"] = -1
    except MultipleObjectsReturned:
        retval["id"] = -2
    return retval

def createProfile(args):
    temp = Profile(name=args["name"], username=args["username"], password=args["password"], \
                   lat=args["lat"], lng=args["lng"], school=args["school"], \
                   courses=args["courses"], preferences=args["preferences"], \
                   interests=args["interests"])
    temp.save()
    creds = {}
    creds["password"] = args["password"]
    creds["deviceid"] = args["deviceid"]
    hashret = temp.login(creds)
    retval = {}
    retval["id"] = temp.id
    retval["hash"] = hashret["hash"]
    return retval

def modifyProfile(args, profile_id):
    prof = Profile.objects.get(pk=profile_id)
    
    prof.name = args["name"]
    prof.username = args["username"]
    prof.lat = args["lat"]
    prof.lng = args["lng"]
    prof.school = args["school"]
    prof.courses = args["courses"]
    prof.preferences = args["preferences"]
    prof.interests = args["interests"]
    prof.save()
    return prof.inJson()