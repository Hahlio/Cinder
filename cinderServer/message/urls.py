from django.urls import path

from . import views

urlpatterns = [
    # message/
    path('', views.msgreq),
    # message/content/msg_id
    path('content/<int:msg_id>', views.content)
]
