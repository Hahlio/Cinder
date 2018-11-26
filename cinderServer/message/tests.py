import pytest
from django.test import TestCase
from Matchmaking.models import Match
from userprofile.models import Profile
from .models import Message, createMessage, messageLog

@pytest.mark.django_db
class Test:
    def test_model(self):
        testProf1 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf1.save()
        testProf2 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf2.save()
        match = Match(user1=testProf1, user2=testProf2, score=100, user1HasMatched=False, user2HasMatched = False, user1accepted=True, user2accepted=True)
        match.save()
        msg = Message(sender=testProf1, matchID = match, message="message")
        msg.save()
        assert msg.__str__() == "message"
        assert msg.toJson()["sender"] == testProf1.id

        assert createMessage(testProf1.id, match.id, "Hello", False)["id"] != -1
        assert createMessage(testProf1.id, match.id, "Hello", True)["id"] != -1
        assert createMessage(1000, match.id, "Hello", False)["id"] == -1

        assert messageLog(match.id) != None

    def test_toJson(self):
        testProf1 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf1.save()
        testProf2 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf2.save()
        match = Match(user1=testProf1, user2=testProf2, score=100, user1HasMatched=False, user2HasMatched = False, user1accepted=True, user2accepted=True)
        match.save()
        msg = Message(sender=testProf1, matchID = match, message="message")
        msg.save()
        assert msg.toJson()["sender"] == testProf1.id

    def test_to_string(self):
        testProf1 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf1.save()
        testProf2 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf2.save()
        match = Match(user1=testProf1, user2=testProf2, score=100, user1HasMatched=False, user2HasMatched = False, user1accepted=True, user2accepted=True)
        match.save()
        msg = Message(sender=testProf1, matchID = match, message="message")
        msg.save()
        assert msg.__str__() == "message"

    def test_create_message(self):
        testProf1 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf1.save()
        testProf2 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf2.save()
        match = Match(user1=testProf1, user2=testProf2, score=100, user1HasMatched=False, user2HasMatched = False, user1accepted=True, user2accepted=True)
        match.save()
        msg = Message(sender=testProf1, matchID = match, message="message")
        msg.save()
        assert createMessage(testProf1.id, match.id, "Hello", False)["id"] != -1
        assert createMessage(testProf1.id, match.id, "Hello", True)["id"] != -1
        assert createMessage(1000, match.id, "Hello", False)["id"] == -1

    def test_log(self):
        testProf1 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf1.save()
        testProf2 = Profile(name="Test", username="username", password="password", \
                            lat=123, lng=123, school="school", \
                            courses="courses", preferences="preferences", \
                            interests="interests")
        testProf2.save()
        match = Match(user1=testProf1, user2=testProf2, score=100, user1HasMatched=False, user2HasMatched = False, user1accepted=True, user2accepted=True)
        match.save()
        msg = Message(sender=testProf1, matchID = match, message="message")
        msg.save()
        assert messageLog(match.id) != None