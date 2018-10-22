from django.db import models

from django.db import models
from userprofile.models import Profile

class Message(models.Model):
    sender = models.ForeignKey(Profile, related_name='sender', on_delete=models.DO_NOTHING)
    reciever = models.ForeignKey(Profile, related_name='reciever', on_delete=models.DO_NOTHING)
    message = models.TextField()
    timestamp = models.DateTimeField('date sent')

    def __str__(self):
        return self.message
