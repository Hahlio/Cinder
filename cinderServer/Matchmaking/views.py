# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.http import HttpResponse, HttpRequest, JsonResponse
from .models import Match
from .serializers import MatchListSerializer


def index(request):
    return HttpResponse("Hello, world. You're at the matchmaking index.")

class matches(APIView):

    def get(request, profile_id):
    	profileList = Match.returnListOfMatches(profile_id)
    	#serializer = ProfileListSerializer(profileList , many=True)
    	return Response(profileList, status=status.HTTP_200_OK)

    def post(request, profile_id):
    	return Response(request.data, status=status.HTTP_400_BAD_REQUEST)

    def put(request, profile_id):
        matched = Match.returnMatch(request.data)
        serializer = MatchListSerializer(matched, data=request.data)
        if serializer.is_valid():
        	serializer.save()
        	return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
