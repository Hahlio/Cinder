from threading import Thread
from django.http import HttpResponse, JsonResponse, HttpResponseNotFound
from .models import Profile
from Matchmaking.models import Match

def profDetails(request, profile_id):
    if request.method == 'GET':
        try:
            p = Profile.objects.get(pk=profile_id)
        except ObjectDoesNotExist:
            retval1 = HttpResponse("User does not Exist", 404)
            return retval1
        retval = p.inJson()
        return JsonResponse(retval)

    if request.method == 'PUT':
        retval = Profile.modifyProfile(request.body, profile_id)
        # Updates the matchmaking algorithm in the background
        thread = Thread(target = Match.updateMatch, args = (Profile.objects.get(pk=retval["id"]), ) )
        thread.start()
    else:
        retval = HttpResponse("Invalid request")
        retval.status_code = 400
        return retval
    return JsonResponse(retval)

def createProf(request):
    if request.method == 'POST':
        retval = Profile.createProfile(request.body)
        # Updates the matchmaking algorithm in the background
        thread = Thread(target = Match.createMatch, args = (Profile.objects.get(pk=retval["id"]), ) )
        thread.start()
        return JsonResponse(retval)
    else:
        retval = HttpResponse("Invalid request")
        retval.status_code = 400
        return retval

def lookupUser(request, user):
    if request.method == 'GET':
        retval = Profile.findID(user)
        return JsonResponse(retval)
    else:
        retval = HttpResponse("Invalid request")
        retval.status_code = 400
        return retval
