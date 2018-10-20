from django.urls import path

from . import views

urlpatterns = [
    # /Matchmaking
    path('', views.index, name='index'),
    # /Matchmaking/id
    path('<int:profile_id>',views.match),
]
