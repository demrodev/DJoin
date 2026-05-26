
# Information

[DJoin](https://github.com/demrodev/DJoin) is an easy-to-use plugin to customize your players' login to the server.


## Using
The plugin has been tested and works stably on Minecraft version 1.21. Compatible with Paper-1.21+
## Extensions

For the plugin to work properly, you need to install [LuckPerms](https://github.com/LuckPerms/LuckPerms) and [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI). This is necessary for the plugin to correctly identify messages specific to special groups. The PlaceholderAPI allows you to customize messages using placeholders.
## Support

If you find an error in the plugin or have a question about its use, please let us know via: 
- [Telegram](https://t.me/notromanenko)
- [E-mail](mailto:nulledphp@vk.com)
- [Issue tracker](https://github.com/demrodev/DJoin/issues)
## Usage
config.yml
```yaml
# Префикс плагина (будет добавляться перед всеми сообщениями от команд)
prefix: "&8[&aDJoin&8]&r"

# Сообщения команд (можно менять и использовать цвета &)
messages:
  group_added: "&aГруппа '{group}' добавлена."
  group_removed: "&aГруппа '{group}' удалена."
  group_not_found: "&cГруппа '{group}' не найдена."
  msg_set: "&aСообщение для группы '{group}' установлено."
  cmd_added: "&aКоманда добавлена группе '{group}'."
  cmd_removed: "&aКоманда с индексом {index} удалена."
  cmd_invalid_index: "&cНеверный индекс."
  cmd_list_header: "&6Команды для группы '{group}':"
  cmd_list_entry: "&7{index}: &f{command}"
  cmd_list_empty: "&eНет команд для группы '{group}'."
  reload_success: "&aКонфигурация перезагружена."
  usage_addgroup: "&cИспользование: /join addgroup <название_группы>"
  usage_delgroup: "&cИспользование: /join delgroup <название_группы>"
  usage_setmsg: "&cИспользование: /join setmsg <группа> \"<сообщение>\""
  usage_addcmd: "&cИспользование: /join addcmd <группа> \"<команда>\"\n&eПример: /join addcmd vip \"eco give %player_name% 100\""
  usage_delcmd: "&cИспользование: /join delcmd <группа> <индекс>"
  usage_listcmd: "&cИспользование: /join listcmd <группа>"
  help_header: "&6=== DJoin Commands ==="
  help_addgroup: "&e/join addgroup <группа>&7 - добавить группу"
  help_delgroup: "&e/join delgroup <группа>&7 - удалить группу"
  help_setmsg: "&e/join setmsg <группа> \"сообщение\"&7 - установить сообщение"
  help_addcmd: "&e/join addcmd <группа> \"команда\"&7 - добавить команду при входе"
  help_delcmd: "&e/join delcmd <группа> <индекс>&7 - удалить команду по индексу"
  help_listcmd: "&e/join listcmd <группа>&7 - показать команды группы"
  help_reload: "&e/join reload&7 - перезагрузить конфиг"

# Сообщения о входе для групп (с поддержкой PlaceholderAPI, цветов и HEX)
join-messages:
  default: "&7[&c?&7] &e%player_name% &7присоединился к серверу!"
  vip: "&a[VIP] &e%player_name% &fзашёл на сервер!"
  mvp: "&6MVP &e%player_name% &fзашёл на сервер благословить Вас!"

# Команды, выполняемые при входе (от консоли) для каждой группы
join-commands:
  default:
    - "say Добро пожаловать, %player_name%!"
  vip:
    - "eco give %player_name% 100"
    - "lp user %player_name% permission settemp vip.boost true 1h"
  mvp:
    - "effect give %player_name% speed 60 1"
    - "tellraw %player_name% {\"text\":\"С возвращением, чемпион!\",\"color\":\"gold\"}"
```
## Commands

- /djoin addgroup (groupname) - adding LuckPerms group to config.yml
- /djoin delgroup (groupname) - removing LuckPerms group from config.yml
- /djoin setmsg (groupname) (message) - setting JoinMessage for someone group from config.yml (IMPORTANT! You need to use quotation marks on both sides when you type JoinMessage.)
- /djoin addcmd (groupname) ("command") - adding cmd which would execute when player join by Console.
- /djoin delcmd (groupname) (index) - removing cmd from group.
- /djoin listcmd (groupname) - get list and cmd indexes for group.
- /djoin reload - reload configuration.
## Permissions

- djoin.djoin - allow to use /djoin
## Features

- HEX-colors (&#RRGGBB)
- PlaceholderAPI support
- Simple color codes (like, &a-&f)
- Paper 1.21+ support
- Fully customizable
