# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from django.http import HttpResponse
from .models import returnListOfMatches, returnMatch, matchAccept, createGroup, groupAdd, groupLeave, groupMembers, returnContacts, returnGroups, removeContact, returnGroupContacts
from .serializers import MatchListSerializer
from userprofile.models import validID


def index(request):
    return HttpResponse("Hello, world. You're at the matchmaking index.")

class matches(APIView):

    def get(self, request, profile_id):
        if validID(profile_id):
            profileList = returnListOfMatches(profile_id)
            print(profileList)
            return Response(profileList, status=status.HTTP_200_OK)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    @classmethod
    def post(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    def put(self, request, profile_id):
        if validID(profile_id):
            matched = returnMatch(request.data)
            matchAccept(profile_id, matched, request.data["accepted"])
            print(request.data)
            return Response(request.data, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    @classmethod
    def delete(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)


class contacts(APIView):

    def get(self, request, profile_id):
        if validID(profile_id):
            contacts = returnContacts(profile_id)
            return Response(contacts, status=status.HTTP_200_OK)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    @classmethod
    def post(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    @classmethod
    def put(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    def delete(self, request, profile_id):
        if validID(profile_id):
            retval = {}
            if removeContact(request.data["matchid"], profile_id):
                retval["status"] = "Removed Contact"
                return Response(retval, status=status.HTTP_200_OK)
            else:
                retval["status"] = "User accessing wrong matchid or matchid doesn't exist."
                return Response(retval, status=status.HTTP_404_NOT_FOUND)
                            
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)


class groups(APIView):

    def get(self, request, profile_id):
        if validID(profile_id):
            groups = returnGroups(profile_id)
            return Response(groups, status=status.HTTP_200_OK)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    def post(self, request, profile_id):
        if validID(profile_id):
            retval = createGroup(request.data["groupName"], profile_id)
            return Response(retval, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    def put(self, request, profile_id):
        if validID(profile_id):
            groupAdd(request.data["userMatchID"], request.data["matchID"], profile_id)
            return Response(request.data, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    def delete(self, request, profile_id):
        if validID(profile_id):
            groupLeave(profile_id, request.data["matchid"])
            return Response(request.data, status=status.HTTP_202_ACCEPTED)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)


class groupcontacts(APIView):
    @classmethod
    def get(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    @classmethod
    def post(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)

    def put(self, request, profile_id):
        if validID(profile_id):
            print(request.data)
            groups = returnGroupContacts(profile_id, request.data["matchid"])
            return Response(groups, status=status.HTTP_200_OK)
        else:
            return Response(request.data, status=status.HTTP_404_NOT_FOUND)

    @classmethod
    def delete(self, request, profile_id):
        return Response(request.data, status=status.HTTP_405_METHOD_NOT_ALLOWED)
