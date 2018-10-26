# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.http import HttpResponse
from .models import returnListOfMatches, returnMatch
from .serializers import MatchListSerializer
from userprofile.models import Profile


def index(request):
    return HttpResponse("Hello, world. You're at the matchmaking index.")

class matches(APIView):

    def get(self, request, profile_id):
        if validID(profile_id):
            profileList = returnListOfMatches(profile_id)
            return Response(profileList, status=status.HTTP_200_OK)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)
        

    def post(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    def put(self, request, profile_id):
        if validID(profile_id):
            matched = returnMatch(request.data)
            serializer = MatchListSerializer(matched, data=request.data)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data, status=status.HTTP_202_ACCEPTED)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    def delete(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)