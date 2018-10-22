from rest_framework import serializers
from userprofile.models import Profile
from .models import Match

class ProfileListSerializer(serializers.ModelSerializer):

	class Meta:
		model = Profile
		fields = ('id','name', 'school', 'courses', 'lat', 'lng')
		# fields = '__all__'



class MatchListSerializer(serializers.ModelSerializer):

	class Meta:
		model = Match
		fields = ('user1','user2','score','hasMatched','accepted')
		read_only_fields = ['user1','user2','score']

