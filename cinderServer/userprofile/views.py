"""
Views used for accessing the representation of Profiles.
The details of the profile can be accessed from here, as well
as searching for the ID of the profile and creation of profiles.
"""
import json

from threading import Thread
from django.http import JsonResponse
from django.db.models import Q
from Matchmaking.models import Match, updateMatch, createMatch

from .models import Profile, modifyProfile, createProfile, findID, validID

def profDetails(request, profile_id):
    """
    Methods used when the profile already exists
    GET: gets the profile details
    PUT: updates the profile (MUST SEND VALUES WHICH ARE NOT CHANGED ALSO)
    DELETE: removes the profile and also the matches associated with it
    """
    # Tries to retreive the profile before executing anything
    code = 200
    retval = {}
    if validID(profile_id):
        p = Profile.objects.get(pk=profile_id)
        if request.method == 'GET':
            retval = p.inJson()
            return JsonResponse(retval)
        elif request.method == 'PUT':
            jsonArgs = request.body.decode("utf-8")
            args = json.loads(jsonArgs)
            retval = modifyProfile(args, profile_id)
            # Updates the matchmaking algorithm in the background
            thread = Thread(target=updateMatch, args=(Profile.objects.get(pk=retval["id"]),))
            thread.start()
        elif request.method == 'DELETE':
            Match.objects.all().filter(Q(user1__exact=p)|Q(user2__exact=p)).delete()
            p.delete()
            retval["success"] = 1
        else:
            code = 405
            retval["status"] = 405
            retval["userMessage"] = "The requested method is not allowed"
    else:
        code = 404
        retval["status"] = 404
        retval["userMessage"] = "The profile you requested does not exist"
    return JsonResponse(retval, status=code)

def createProf(request):
    """
    Methods used when the profile doesn't exist
    POST: Used to create a new profile
    """
    code = 200
    retval = {}
    if request.method == 'POST':
        jsonArguments = request.body.decode("utf-8")
        args = json.loads(jsonArguments)
        if findID(args["username"])["id"] != -1:
            code = 406
            retval["status"] = 406
            retval["userMessage"] = "Username already exists"
        else:
            retval = createProfile(args)
            # Updates the matchmaking algorithm in the background
            thread = Thread(target=createMatch, args=(Profile.objects.get(pk=retval["id"]),))
            thread.start()
    else:
        code = 405
        retval["status"] = 405
        retval["userMessage"] = "The requested method is not allowed"
    return JsonResponse(retval, status=code)

def lookupUser(request):
    """
    Methods used when the profile ID isn't known
    GET: Used to find the ID of the username
    """
    code = 200
    retval = {}
    if request.method == 'PUT':
        jsonArguments = request.body.decode("utf-8")
        args = json.loads(jsonArguments)
        retval = findID(args["username"])
        uid = retval["id"]
        if not validID(retval["id"]):
            #code = 404
            #retval["status"] = 404
            retval["hash"] = -1
            #retval["userMessage"] = "The user does not exists"
        else:
            largs = {}
            largs["password"] = args["password"]
            largs["deviceid"] = args["deviceid"]
            p = Profile.objects.get(pk=retval["id"])
            retval = p.login(largs)
            retval["id"] = uid
    else:
        code = 405
        #retval["status"] = 405
        #retval["userMessage"] = "The requested method is not allowed"
    return JsonResponse(retval, status=code)
