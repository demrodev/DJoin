
# Information

[DJoin](https://github.com/demrodev/DJoin) is an easy-to-use plugin to customize your players' login to the server.


## Using
The plugin has been tested and works stably on Minecraft version 1.21. Compatible with Paper-1.21
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
Сообщения для групп (формат: группа: "сообщение")
# Поддерживаются цвета через & (например &a) и HEX через &#RRGGBB
# Плейсхолдеры PlaceholderAPI: %player_name%, %player_displayname% и др.
join-messages:
  default: "&7[&c?&7] &e%player_name% &7присоединился к серверу!"
  vip: "&a[VIP] &e%player_name% &fзашёл на сервер!"
  mvp: "&6MVP &e%player_name% &fзашёл на сервер благословить Вас!"
```
## Commands

- /djoin addgroup (groupname) - adding LuckPerms group to config.yml
- /djoin delgroup (groupname) - removing LuckPerms group from config.yml
- /djoin setmsg (groupname) (message) - setting JoinMessage for someone group from config.yml

IMPORTANT! You need to use quotation marks on both sides when you type JoinMessage.
- /djoin reload - reload configuration.
## Permissions

- djoin.djoin - allow to use /djoin
## Features

- HEX-colors (&#RRGGBB)
- PlaceholderAPI support
- Simple color codes (like, &a-&f)