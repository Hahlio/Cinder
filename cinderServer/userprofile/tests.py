from .models import Profile, createProfile, modifyProfile, findID, validID
from mixer.backend.django import mixer

import pytest

# Test arguments which are used

# New for modification
testArgs = {}
expectArgs = {}
numberCases = 2

newArgs = {}
newArgs["name"] = "Jane Doe"
newArgs["username"] = "Janemasterstudy"
newArgs["lat"] = 123.321
newArgs["lng"] = 321.123
newArgs["school"] = "Not plebian school"
newArgs["courses"] = "Someone cares"
newArgs["preferences"] = "Other pleasure sessions"
newArgs["interests"] = "Videos"

# First test case
testArgs[0] = {}
testArgs[0]["name"] = "John Doe"
testArgs[0]["username"] = "JohnStudyMiester"
testArgs[0]["password"] = "testpasswordthatissupersecure"
testArgs[0]["deviceid"] = "an_edgey_phone"
testArgs[0]["lat"] = 123.321
testArgs[0]["lng"] = 321.123
testArgs[0]["school"] = "International University of Colleges of Polytechnical University"
testArgs[0]["courses"] = "Noone cares"
testArgs[0]["preferences"] = "Quiet,Loud"
testArgs[0]["interests"] = "Animal Videos"
# Expected
expectArgs[0] = {}
expectArgs[0]["name"] = "John Doe"
expectArgs[0]["username"] = "JohnStudyMiester"
expectArgs[0]["lat"] = 123.321
expectArgs[0]["lng"] = 321.123
expectArgs[0]["school"] = "International University of Colleges of Polytechnical University"
expectArgs[0]["courses"] = "Noone cares"
expectArgs[0]["preferences"] = "Quiet,Loud"
expectArgs[0]["interests"] = "Animal Videos"

# Second test case
testArgs[1] = {}
testArgs[1]["name"] = "Tester McTester"
testArgs[1]["username"] = "TesterMaster"
testArgs[1]["password"] = "test password"
testArgs[1]["deviceid"] = "testdeviceid123"
testArgs[1]["lat"] = 123.321
testArgs[1]["lng"] = 321.123
testArgs[1]["school"] = "Ultrapleb School That Noone goes to"
testArgs[1]["courses"] = "Noone cares and no one will look"
testArgs[1]["preferences"] = "Circle Activities"
testArgs[1]["interests"] = "Wet Sports"
# Expected
expectArgs[1] = {}
expectArgs[1]["name"] = "Tester McTester"
expectArgs[1]["username"] = "TesterMaster"
expectArgs[1]["lat"] = 123.321
expectArgs[1]["lng"] = 321.123
expectArgs[1]["school"] = "Ultrapleb School That Noone goes to"
expectArgs[1]["courses"] = "Noone cares and no one will look"
expectArgs[1]["preferences"] = "Circle Activities"
expectArgs[1]["interests"] = "Wet Sports"

@pytest.mark.django_db
class Test:
    def test_model(self):
        test0 = createProfile(testArgs[0])
        uid0 = test0["id"]
        dhash0 = test0["hash"]
        uprof0 = Profile.objects.get(pk=uid0)

        assert test0 != None
        assert uprof0.loggedin == True
        assert uprof0.__str__() == expectArgs[0]["name"]
        assert sorted(uprof0.inJson()) == sorted(expectArgs[0])

        assert validID(uid0) == True
        assert validID(-1) == False

        assert findID(expectArgs[0]["username"])["id"] == uid0
        assert findID("Thisshouldnotbeausername123asweareinatestdatabase")["id"] == -1

        assert sorted(modifyProfile(newArgs, uid0)) == sorted(newArgs)

        assert uprof0.authenticate(dhash0, testArgs[0]["deviceid"]) == True
        assert uprof0.authenticate(dhash0, "123") == False
        assert uprof0.authenticate("123", "123") == False
        assert uprof0.authenticate("123", testArgs[0]["deviceid"]) == False

        assert uprof0.logout("123", "123")["success"] == False
        assert uprof0.logout("123", testArgs[0]["deviceid"])["success"] == False
        assert uprof0.logout(dhash0, "123")["success"] == False
        assert uprof0.logout(dhash0, testArgs[0]["deviceid"])["success"] == True

    def test_creation_and_string_formats(self):
        test = {}
        uid = {}
        dhash = {}
        uprof = {}
        for x in range(0, numberCases - 1):
            test[x] = createProfile(testArgs[x])
            uid[x] = test[x]["id"]
            dhash[x] = test[x]["hash"]
            uprof[x] = Profile.objects.get(pk=uid[x])

            assert test[x] != None
            assert uprof[x].loggedin == True
            assert uprof[x].__str__() == expectArgs[x]["name"]
            assert sorted(uprof[x].inJson()) == sorted(expectArgs[x])

    def test_validID(self):
        test = {}
        uid = {}
        dhash = {}
        uprof = {}
        for x in range(0, numberCases - 1):
            test[x] = createProfile(testArgs[x])
            uid[x] = test[x]["id"]
            dhash[x] = test[x]["hash"]
            uprof[x] = Profile.objects.get(pk=uid[x])

            assert validID(uid[x]) == True

        assert validID(-1) == False

    def test_findID(self):
        test = {}
        uid = {}
        dhash = {}
        uprof = {}
        for x in range(0, numberCases - 1):
            test[x] = createProfile(testArgs[x])
            uid[x] = test[x]["id"]
            dhash[x] = test[x]["hash"]
            uprof[x] = Profile.objects.get(pk=uid[x])

            assert findID(expectArgs[x]["username"])["id"] == uid[x]
        assert findID("Thisshouldnotbeausername123asweareinatestdatabase")["id"] == -1

    def test_modify_profile(self):
        test = {}
        uid = {}
        dhash = {}
        uprof = {}
        for x in range(0, numberCases - 1):
            test[x] = createProfile(testArgs[x])
            uid[x] = test[x]["id"]
            dhash[x] = test[x]["hash"]
            uprof[x] = Profile.objects.get(pk=uid[x])

            assert sorted(modifyProfile(newArgs, uid[x])) == sorted(newArgs)
            assert sorted(uprof[x].inJson()) == sorted(newArgs)

    def test_authenticate(self):
        test = {}
        uid = {}
        dhash = {}
        uprof = {}
        for x in range(0, numberCases - 1):
            test[x] = createProfile(testArgs[x])
            uid[x] = test[x]["id"]
            dhash[x] = test[x]["hash"]
            uprof[x] = Profile.objects.get(pk=uid[x])

            assert uprof[x].authenticate(dhash[x], testArgs[x]["deviceid"]) == True
            assert uprof[x].authenticate(dhash[x], "Notapassword") == False
            assert uprof[x].authenticate("Notahash", "Notapassword") == False
            assert uprof[x].authenticate("Notahash", testArgs[x]["deviceid"]) == False

    def test_logout(self):
        test = {}
        uid = {}
        dhash = {}
        uprof = {}
        for x in range(0, numberCases - 1):
            test[x] = createProfile(testArgs[x])
            uid[x] = test[x]["id"]
            dhash[x] = test[x]["hash"]
            uprof[x] = Profile.objects.get(pk=uid[x])
            
            # Is true because user is logged in upon account creation
            assert uprof[x].loggedin == True

            assert uprof[x].logout("123", "123")["success"] == False
            assert uprof[x].logout("123", testArgs[x]["deviceid"])["success"] == False
            assert uprof[x].logout(dhash[x], "123")["success"] == False
            assert uprof[x].logout(dhash[x], testArgs[x]["deviceid"])["success"] == True

            assert uprof[x].loggedin == False

    def test_login(self):
        test = {}
        uid = {}
        dhash = {}
        uprof = {}
        for x in range(0, numberCases - 1):
            testcaseArg = {}

            test[x] = createProfile(testArgs[x])
            uid[x] = test[x]["id"]
            dhash[x] = test[x]["hash"]
            uprof[x] = Profile.objects.get(pk=uid[x])
            uprof[x].logout(dhash[x], testArgs[x]["deviceid"])
            
            testcaseArg["password"] = "Notapassword"
            testcaseArg["deviceid"] = "Notadeviceid"
            assert uprof[x].loggedin == False
            assert uprof[x].login(testcaseArg)["success"] == False
            assert uprof[x].loggedin == False
            uprof[x].logout(dhash[x], testArgs[x]["deviceid"])

            testcaseArg["password"] = "Notapassword"
            testcaseArg["deviceid"] = testArgs[x]["deviceid"]
            assert uprof[x].login(testcaseArg)["success"] == False
            assert uprof[x].loggedin == False
            uprof[x].logout(dhash[x], testArgs[x]["deviceid"])

            testcaseArg["password"] = testArgs[x]["password"]
            testcaseArg["deviceid"] = "Notadeviceid"
            assert uprof[x].login(testcaseArg)["success"] == True
            assert uprof[x].loggedin == True
            uprof[x].logout(dhash[x], testArgs[x]["deviceid"])

            testcaseArg["password"] = testArgs[x]["password"]
            testcaseArg["deviceid"] = testArgs[x]["deviceid"]
            assert uprof[x].login(testcaseArg)["success"] == True
            assert uprof[x].loggedin == True
