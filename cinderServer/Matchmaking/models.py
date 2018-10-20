# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from userprofile.models import Profile

class Match(models.Model):
    user1 = models.ForeignKey(Profile, related_name='user1', on_delete=models.DO_NOTHING)
    user2 = models.ForeignKey(Profile, related_name='user2', on_delete=models.DO_NOTHING)
    score = models.IntegerField()

    def generate(profile_id):
        return 0
