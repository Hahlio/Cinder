import json

from django.shortcuts import render
from django.http import JsonResponse

from .models import Message, createMessage, smessageLog, getMessage

# Create your views here.

def msgreq(request):
    code = 200
    retval = {}
    jsonArgs = request.body.decode("utf-8")
    args = json.loads(jsonArgs)
    if request.method == 'GET':
        retval = smessageLog(args["senderid"], args["recieverid"], args["nummsg"])
    elif request.method == 'POST':
        retval = createMessage(args["senderid"], args["recieverid"], args["message"], False)
    else:
        code = 405
        retval["status"] = 405
        retval["userMessage"] = "The requested method is not allowed"
    return JsonResponse(retval, status=code)

def content(request, msg_id):
    code = 200
    retval = {}
    if request.method == 'GET':
        retval = getMessage(msg_id).toJson()
    else:
        code = 405
        retval["status"] = 405
        retval["userMessage"] = "The requested method is not allowed"
    return JsonResponse(retval, status=code)