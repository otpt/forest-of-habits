# Forest of habits

### Запуск приложения
Быстрая сборка и запуск
```
./start
```
Запуск контейнеров с api и базой данных 
```
docker compose --profile api up
```
Обновление контейнера с api после обновления исходного кода(git pull)
```
docker compose --profile api build
```
Только контейнер с базой данных(postgres)
```
docker compose up -d
```