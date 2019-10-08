# Quantas Vezes Ela Partiu
Reactive application to figure out how many times ``Tim Maia - Ela Partiu`` song would be played from a location to another location.

## Need to create
To make this project work you'll need to create ``resources/external.yml`` file with the following information inside:
```yaml
google:
  key: API_KEY
  directions.api: https://maps.googleapis.com/maps/api/directions/json?origin={from}&destination={to}&key=${key}

spotify:
  id: SPOTIFY_ID
  secret: SPOTIFY_SECRET
  song.id: 1uSTmyzNeDR69JUKX72vvU
  token.api: https://accounts.spotify.com/api/token
  track.api: https://api.spotify.com/v1/tracks/${song.id}
```