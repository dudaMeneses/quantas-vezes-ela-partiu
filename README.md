# Quantas Vezes Ela Partiu
Reactive application to figure out how many times ``Tim Maia - Ela Partiu`` song would be played from a location to another location.

## Need to Create
To make this project work you'll need to create ``resources/external.yml`` file with the following information inside:
```yaml
route:
  key: API_KEY
  directions.api: http://open.mapquestapi.com/directions/v2/route?key=${key}&from={from}&to={to}

spotify:
  id: SPOTIFY_ID
  secret: SPOTIFY_SECRET
  song.id: 1uSTmyzNeDR69JUKX72vvU
  token.api: https://accounts.spotify.com/api/token
  track.api: https://api.spotify.com/v1/tracks/${song.id}
```

## Tech Stack
- Java 11
- Spring-Boot (WebFlux)
- Lombok