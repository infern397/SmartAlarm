# Будильник с заданиями
![Главный экран приложения](src for readme/mainActivity.png)
## Описание приложения
Приложение представляет менеджер будильников, которые требуют 
решения математического выражения для отключения.
## Добавление и изменение будильника
![Экран добавления и изменения](src for readme/addEditActivity.png)
## Удаление будильника
![Удаление будильника по свайпу](src for readme/delete.gif)
## Срабатываение будильника
### Уведомление
![Уведомление](src for readme/notification.png)
### Задача
![Задача](src for readme/task.png)
## Технические детали
* База данны - SQLite в связке с библиотекой Room
* Будильники реализованы через PendingIntent и Receiver
* Математические выражения генерируются случайным образом
* Архитектура - MVVM