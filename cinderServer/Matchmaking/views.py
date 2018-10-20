# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.http import HttpResponse, HttpRequest, JsonResponse
from .models import Profile

import json

def index(request):
    return HttpResponse("Hello, world. You're at the matchmaking index.")

def match(request, profile_id):

    # TODO: make this restful and proper
    return HttpResponse("invalid request")

def profDetails(request, profile_id):
    p = Profile.objects.get(pk=profile_id)
    if request.method == 'GET':
        return JsonResponse(p.inJson())
    else:
        # TODO: make this restful and proper
        return HttpResponse("invalid request")

def createProf(request):
    if request.method == 'POST':
        return JsonResponse(Profile.createProfile(request.body))
    else:
        # TODO: make this restful and proper
        return HttpResponse("invalid request")
