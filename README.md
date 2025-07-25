# Мой первый Minecraft Utility Плагин

Этот плагин демонстрирует базовые возможности разработки плагинов для Minecraft серверов на платформе Spigot/PaperMC с использованием Java.


## Возможности:

* Настраиваемое приветствие игроков:
    * При входе нового игрока на сервер отображается настраиваемое сообщение из config.yml.
    * Поддерживает заполнитель %player% для имени игрока и цветовые коды (&).
* Контроль установки ТНТ:
    * Администратор может включить или выключить запрет на установку ТНТ через config.yml.
* Переключатель блоков:
    * При правом клике по камню он превращается в алмазный блок, и наоборот.
* Временное усиление (Факел):
    * При правом клике в воздух с факелом игрок получает временные эффекты скорости и силы проводника.
* Сундук-сокровищница:
    * При правом клике по сундуку игрок получает 10 алмазов (один раз).
* Отображение информации о клике:
    * Сообщает игроку, по какому блоку он кликнул или кликнул ли по воздуху.


## Установка:

1.  Скачайте файл плагина (JAR-файл) из раздела [Releases](ссылка_на_раздел_releases_на_github_после_того_как_ты_сделаешь_первый_release).
2.  Поместите его в папку plugins вашего Spigot/PaperMC сервера.
3.  Перезагрузите или запустите сервер.
4.  После первого запуска в папке plugins/НазваниеТвоегоПлагина/ появится файл config.yml.


## Конфигурация (config.yml):

`yaml
welcome-message: §aПривет, %player_name%! Добро пожаловать на сервер!
# Включить/выключитть функцию приветствия:
enable-welcome-message: false
# Сменить цвет сообщений в чате
chat-echo-prefix: §c[RED Эхо] §5
# включить/выключить функцию тнт!
disable-tnt: true

