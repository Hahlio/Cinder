from django.urls import path

from . import views

urlpatterns = [
    # /Matchmaking
    path('', views.index, name='index'),
    # /Matchmaking/id
    path('<int:profile_id>',views.match),
    # /Matchmaking/profile/id
    path('profile/<int:profile_id>',views.profDetails),
    # /Matchmaking/profile
    path('profile',views.createProf),
]
