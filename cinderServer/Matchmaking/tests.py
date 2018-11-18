from django.urls import reverse,resolve
from userprofile.models import Profile, createProfile
from .models import returnMatch, returnListOfMatches, createMatch, updateMatch

import pytest


class TestMatchURL:

    def test_Matches_url(self):
        path = reverse('matchmaking', kwargs={'profile_id' : 1})
        assert resolve(path).view_name == 'matchmaking'

diffProfile = {}
diffProfile["name"] = "Doesn't Matter"
diffProfile["username"] = "DiffTest2"
diffProfile["password"] = "test"
diffProfile["deviceid"] = "Android5"
diffProfile["lat"] = 80.000
diffProfile["lng"] = 111.123
diffProfile["school"] = "International School"
diffProfile["courses"] = "CPSC 221"
diffProfile["preferences"] = "Group Discussion"
diffProfile["interests"] = "Animal Videos"

testProfiles = {}

#test profile 1
testProfiles[0] = {}
testProfiles[0]["name"] = "Doesn't Matter"
testProfiles[0]["username"] = "test1"
testProfiles[0]["password"] = "test"
testProfiles[0]["deviceid"] = "Android1"
testProfiles[0]["lat"] = 80.000
testProfiles[0]["lng"] = 112.123
testProfiles[0]["school"] = "Plebian school"
testProfiles[0]["courses"] = "CPEN311, CPEN321, ELEC221, CPEN 331"
testProfiles[0]["preferences"] = "Group Discussion"
testProfiles[0]["interests"] = "Animal Videos"

#test profile 2
testProfiles[1] = {}
testProfiles[1]["name"] = "Doesn't Matter"
testProfiles[1]["username"] = "test2"
testProfiles[1]["password"] = "test"
testProfiles[1]["deviceid"] = "Android1"
testProfiles[1]["lat"] = 20.321
testProfiles[1]["lng"] = 80.000
testProfiles[1]["school"] = "Plebian school"
testProfiles[1]["courses"] = "CPEN311, CPEN321, ELEC221"
testProfiles[1]["preferences"] = "Group Discussion"
testProfiles[1]["interests"] = "Animal Videos"


#test profile 3
testProfiles[2] = {}
testProfiles[2]["name"] = "Doesn't Matter"
testProfiles[2]["username"] = "test3"
testProfiles[2]["password"] = "test"
testProfiles[2]["deviceid"] = "Android1"
testProfiles[2]["lat"] = 56.321
testProfiles[2]["lng"] = 100.000
testProfiles[2]["school"] = "Plebian school"
testProfiles[2]["courses"] = "CPEN311, CPEN321, ELEC221, CPEN 331"
testProfiles[2]["preferences"] = "Group Discussion"
testProfiles[2]["interests"] = "Animal Videos"


#test profile 4
testProfiles[3] = {}
testProfiles[3]["name"] = "Doesn't Matter"
testProfiles[3]["username"] = "test4"
testProfiles[3]["password"] = "test"
testProfiles[3]["deviceid"] = "Android1"
testProfiles[3]["lat"] = 55.321
testProfiles[3]["lng"] = 25.123
testProfiles[3]["school"] = "Plebian school"
testProfiles[3]["courses"] = "CPEN311, CPEN321, ELEC221, CPEN 331"
testProfiles[3]["preferences"] = "Group Discussion"
testProfiles[3]["interests"] = "Animal Videos"


#test profile 5
testProfiles[4] = {}
testProfiles[4]["name"] = "Doesn't Matter"
testProfiles[4]["username"] = "test5"
testProfiles[4]["password"] = "test"
testProfiles[4]["deviceid"] = "Android1"
testProfiles[4]["lat"] = 33.321
testProfiles[4]["lng"] = 69.123
testProfiles[4]["school"] = "Plebian school"
testProfiles[4]["courses"] = "CPEN311, CPEN321, ELEC221, CPEN 331"
testProfiles[4]["preferences"] = "Group Discussion"
testProfiles[4]["interests"] = "Animal Videos"


#test profile 6
testProfiles[5] = {}
testProfiles[5]["name"] = "Doesn't Matter"
testProfiles[5]["username"] = "test6"
testProfiles[5]["password"] = "test"
testProfiles[5]["deviceid"] = "Android1"
testProfiles[5]["lat"] = 11.321
testProfiles[5]["lng"] = 88.420
testProfiles[5]["school"] = "Plebian school"
testProfiles[5]["courses"] = "CPEN311, CPEN321, ELEC221, CPEN 331"
testProfiles[5]["preferences"] = "Group Discussion"
testProfiles[5]["interests"] = "Animal Videos"


#test profile 7
testProfiles[6] = {}
testProfiles[6]["name"] = "Doesn't Matter"
testProfiles[6]["username"] = "test7"
testProfiles[6]["password"] = "test"
testProfiles[6]["deviceid"] = "Android1"
testProfiles[6]["lat"] = 80.000
testProfiles[6]["lng"] = 111.123
testProfiles[6]["school"] = "Plebian school"
testProfiles[6]["courses"] = "CPEN311, CPEN321, ELEC221"
testProfiles[6]["preferences"] = "Group Discussion"
testProfiles[6]["interests"] = "Animal Videos"


#test profile 8
testProfiles[7] = {}
testProfiles[7]["name"] = "Doesn't Matter"
testProfiles[7]["username"] = "test8"
testProfiles[7]["password"] = "test"
testProfiles[7]["deviceid"] = "Android1"
testProfiles[7]["lat"] = 90.321
testProfiles[7]["lng"] = 120.00
testProfiles[7]["school"] = "Plebian school"
testProfiles[7]["courses"] = "CPEN311"
testProfiles[7]["preferences"] = "Group Discussion"
testProfiles[7]["interests"] = "Animal Videos"


#test profile 9
testProfiles[8] = {}
testProfiles[8]["name"] = "Doesn't Matter"
testProfiles[8]["username"] = "test9"
testProfiles[8]["password"] = "test"
testProfiles[8]["deviceid"] = "Android1"
testProfiles[8]["lat"] = 0.321
testProfiles[8]["lng"] = 0.123
testProfiles[8]["school"] = "Plebian school"
testProfiles[8]["courses"] = "CPEN311, CPEN321, CPEN 331"
testProfiles[8]["preferences"] = "Group Discussion"
testProfiles[8]["interests"] = "Animal Videos"


#test profile 10
testProfiles[9] = {}
testProfiles[9]["name"] = "Doesn't Matter"
testProfiles[9]["username"] = "test10"
testProfiles[9]["password"] = "test"
testProfiles[9]["deviceid"] = "Android1"
testProfiles[9]["lat"] = -10.321
testProfiles[9]["lng"] = -110.123
testProfiles[9]["school"] = "Plebian school"
testProfiles[9]["courses"] = "CPEN311, CPEN321, ELEC221, CPEN 331"
testProfiles[9]["preferences"] = "Group Discussion"
testProfiles[9]["interests"] = "Animal Videos"


#test profile 11
testProfiles[10] = {}
testProfiles[10]["name"] = "Doesn't Matter"
testProfiles[10]["username"] = "test11"
testProfiles[10]["password"] = "test"
testProfiles[10]["deviceid"] = "Android1"
testProfiles[10]["lat"] = 23.321
testProfiles[10]["lng"] = 34.123
testProfiles[10]["school"] = "Plebian school"
testProfiles[10]["courses"] = "CPEN311, CPEN321"
testProfiles[10]["preferences"] = "Quiet Study"
testProfiles[10]["interests"] = "Animal Videos"

listAmount = 5


@pytest.mark.django_db
class TestModels:
    
# User is added into the database and receives a reasonable score with another user. (Testing Core Functionality)
    def test_add_new_positive(self):
        test0 = createProfile(testProfiles[0])
        uid0 = test0["id"]
        createMatch(Profile.objects.get(pk=uid0))

        test1 = createProfile(testProfiles[1])
        uid1 = test1["id"]
        createMatch(Profile.objects.get(pk=uid1))
        matchList = returnListOfMatches(uid0)

        assert len(matchList["Matches"]) == 1
        assert matchList["Matches"][0] == uid1
        

# User is added into the database but doesn’t match with another User (Testing Core Functionality)
    def test_add_new_negative(self):
        test0 = createProfile(testProfiles[0])
        uid0 = test0["id"]
        createMatch(Profile.objects.get(pk=uid0))

        test1 = createProfile(diffProfile)
        uid1 = test1["id"]
        createMatch(Profile.objects.get(pk=uid1))
        matchList = returnListOfMatches(uid0)

        assert len(matchList["Matches"]) == 0

# Application requests matches and receives 10 results each time (Testing Core functionality and reliability)
    def test_ten_matches(self):
        test = {}
        uid = {}
        for x in range(0, 10):
            test[x] = createProfile(testProfiles[x])
            uid[x] = test[x]["id"]
            createMatch(Profile.objects.get(pk=uid[x]))
        
        matchList = returnListOfMatches(uid[0])
        assert len(matchList["Matches"]) == listAmount

        matchList = returnListOfMatches(uid[0])
        assert len(matchList["Matches"]) == listAmount

        test[x] = createProfile(testProfiles[x])
            uid[x] = test[x]["id"]
            createMatch(Profile.objects.get(pk=uid[x]))

        matchList = returnListOfMatches(uid[0])
        assert len(matchList["Matches"]) == listAmount

        matchList = returnListOfMatches(uid[0])
        assert len(matchList["Matches"]) == listAmount


# Application requests matches and the 10 results are decreasing from the highest score (Testing Core functionality)
    def test_match_dec(self):
        test = {}
        uid = {}
        for x in range(0, 11):
            test[x] = createProfile(testProfiles[x])
            uid[x] = test[x]["id"]
            createMatch(Profile.objects.get(pk=uid[x]))
        
        matchList = returnListOfMatches(uid[0])
        assert len(matchList["Matches"]) == listAmount
        
        testIDs = {}
        for x in range(0, listAmount-1):
            testIDs[x] = matchList["Matches"][x]

        for x in range(0, listAmount-2):
            assert testIDs[x] >= testIDs[x+1]
        



# Application request for matches and receives 10 same matches. (Tests that matches won’t change if user hasn’t gone through them)
    def test_same_matches(self):
        test = {}
        uid = {}
        for x in range(0, 11):
            test[x] = createProfile(testProfiles[x])
            uid[x] = test[x]["id"]
            createMatch(Profile.objects.get(pk=uid[x]))
        
        matchList = returnListOfMatches(uid[0])
        assert len(matchList["Matches"]) == listAmount
        
        testIDs = {}
        for x in range(0, listAmount - 1):
            testIDs[x] = matchList["Matches"][x]

        matchList = returnListOfMatches(uid[0])

        for x in range(0, listAmount - 1):
            assert testIDs[x] == matchList["Matches"][x]

        matchList = returnListOfMatches(uid[0])

        for x in range(0, listAmount - 1):
            assert testIDs[x] == matchList["Matches"][x]

            
"""
# Application request for matches and receives 10 different matches. (Tests that if new users are added or user has accepted/declined matches, his match list is updated.)
    def test_diff_matches(self):
        test = {}
        uid = {}
        for x in range(0, 5):
            test[x] = createProfile(testProfiles[x])
            uid[x] = test[x]["id"]
            createMatch(Profile.objects.get(pk=uid[x]))
        
        matchList = returnListOfMatches(uid[0])
        assert len(matchList["Matches"]) == listAmount

        testIDs = {}
        for x in range(0, listAmount - 1):
            testIDs[x] = matchList["Matches"][x]

        test[6] = createProfile(testProfiles[6])
            uid[6] = test[6]["id"]
            createMatch(Profile.objects.get(pk=uid[6]))
            
        matchList = returnListOfMatches(uid[0])

        

# Application accepts the match (Tests core functionality)
    def test_accept_matches(self):

# Application declines the match (Tests core functionality)
    def test_decline_matches(self):
        
"""


