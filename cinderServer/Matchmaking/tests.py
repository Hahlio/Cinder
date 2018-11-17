from django.urls import reverse,resolve

class TestMatchmaking:

    def test_Matches_url(self):
        path = reverse('matchmaking', kwargs={'profile_id' : 1})
        assert resolve(path).view_name == 'matchmaking'

        