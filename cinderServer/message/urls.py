from django.urls import path

from . import views

urlpatterns = [
    # message/<userID>
    path('<int:profile_id>', views.messages),
]
