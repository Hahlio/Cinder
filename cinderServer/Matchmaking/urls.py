from django.urls import path
from rest_framework.urlpatterns import format_suffix_patterns
from . import views


urlpatterns = [
    # /matchmaking
    path('', views.index, name='index'),
    # /matchmaking/id
    path('<int:profile_id>',views.matches.as_view(), name="matchmaking"),
    # /matchmaking/id/contacts
    path('<int:profile_id>/contacts',views.contacts.as_view(), name="contacts"),
    # /matchmaking/id/groups
    path('<int:profile_id>/groups',views.groups.as_view(), name="groups"),
    # /matchmaking/id/groupscontacts
    path('<int:profile_id>/groupscontacts',views.groupcontacts.as_view(), name="groupcontacts"),
]
