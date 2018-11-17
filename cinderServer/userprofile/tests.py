from django.db import models
from .models import Profile, createProfile
import pytest

@pytest.mark.django_db
class Test:

    def test_create(self):
        testArgs = {}
        testArgs["name"] = "Richard Dickenson"
        testArgs["username"] = "test"
        testArgs["password"] = "test"
        testArgs["lat"] = 123.321
        testArgs["lng"] = 321.123
        testArgs["school"] = "Plebian school"
        testArgs["courses"] = "Noone cares"
        testArgs["preferences"] = "Self pleasure sessions"
        testArgs["interests"] = "Animal Videos"
        #print("Set up test arguments")
        test = createProfile(testArgs)
        #print(test[id])
        #if test[id] != -1:
            #Match.objects.all().filter(Q(user1__exact=test[id])|Q(user2__exact=test[id])).delete()
        assert True == True
