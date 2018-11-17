from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from . import views


urlpatterns = [
    # /Matchmaking
    path('', views.index, name='index'),
    # /Matchmaking/id
    path('<int:profile_id>',views.matches.as_view(), name="matchmaking"),
]
