# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.http import HttpResponse
from .models import returnListOfMatches, returnMatch
from .serializers import MatchListSerializer


def index(request):
    return HttpResponse("Hello, world. You're at the matchmaking index.")

class matches(APIView):

    def get(self, request, profile_id):
        profileList = returnListOfMatches(profile_id)
        return Response(profileList, status=status.HTTP_200_OK)

    def post(self, request, profile_id):
        return Response(request.data, status=status.HTTP_400_BAD_REQUEST)

    def put(self, request, profile_id):
        matched = returnMatch(request.data)
        serializer = MatchListSerializer(matched, data=request.data)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
