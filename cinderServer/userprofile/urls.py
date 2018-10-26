from django.urls import path

from . import views

urlpatterns = [
    # profile/username/user
    path('username/<str:user>', views.lookupUser),
    # profile/id
    path('<int:profile_id>', views.profDetails),
    # profile/
    path('', views.createProf),
]
