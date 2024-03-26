# Forest of habits

### Запуск приложения
Запуск контейнеров с api и базой данных 
```
docker compose --profile api up
```
Обновление контейнера с api после обновления исходного кода(git pull)
```
docker compose --profile api up
```
Только контейнер с базой данных(postgres)
```
docker compose up -d
```
