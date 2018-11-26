import json

from django.shortcuts import render
from django.http import JsonResponse
from userprofile.models import validID

from .models import Message, createMessage, messageLog

# Create your views here.

def messages(request, profile_id):
    code = 200
    retval = {}
    jsonArgs = request.body.decode("utf-8")
    args = json.loads(jsonArgs)
    if validID(profile_id):
        if request.method == 'PUT':
            retval = messageLog(args["matchid"])
            #print(retval)
        elif request.method == 'POST':
            print(args)
            retval = createMessage(profile_id, args["matchid"], args["message"], args["isGroup"])
            #print(retval)
        else:
            code = 405
            retval["status"] = 405
            retval["userMessage"] = "The requested method is not allowed"
    else:
        code = 404
        retval["status"] = 404
        retval["userMessage"] = "The profile you requested does not exist"
    return JsonResponse(retval, status=code)