---
title: Flow
slug: flow
description: Flow
---

- `entry.swf` is the main file
- it downloads `resoruce.cfg`
- it initializes `FileAssetManager` (version map, archives XML)
- it download `LoginConfig.xml`
- with `KeyFileLoader.as` every cfg and rsl is downloaded. This includes `naruto.core.swf`, `naruto.include.swf`, `naruto.ui.swf`, and `naruto.exui.swf` which I believe is the core extension where many classes and logic are present.

Particularly, `naruto.core.swf` and `naruto.include.swf` is the most important.

- somewhere in one of the SWF, there's code that defines the list of assets that needs to be downloaded for the game startup, including `pkg` files and extracting them.
- by this point, the loading should advance to 14% with `SoundPlugin.swf` being loaded last.
- the next step is the game using `NarutoServer.swf` which connects to the socket server to a set IP from the flashvars.
- Flash socket policy request should be requested at this point.
- JS function `getUin` and `getCookie` should be called to retrieve player's credentials.
  - If this fails to execute, the `NarutoServer.as` by `GetIPService.as` will request an IP via standard HTTP request. If there is no response data from this, it will use one of the two default hardcoded IPs.
- after that, socket is considered connected, and the game will send the server the first message of `PtLoginRequest`, expecting a `PtLoginResponse`.
- Login response continues to `CMD_GETENCRYPT_LIST`, then to `REPORT_GDPR_CHECK` (opcode 1048619), then to `CMD_GET_ROLE` with extra request of `SYS_REPORT` along the way.
- After `CMD_GET_ROLE`, player is verified and can either: create new character (selecting one of the 5 MC) or login to existing account.
  - Creating new character opens the `RegisterPlugin.swf`. Registering involves asking server for random character name and registering an account itself.
- After registering, or in the case login, the command `PreLoginCommand` is called. This moves to the plugin `LoginPlugin.swf`, where `PreLoginCommand` is handled by `ViewExecuter.as`, causing another command `StartupCommand` to be called.
- The `StartupCommand` is a bit different, its executor is `StartupPlugin.as` directly. The command is `call` is invoked, but the input is the XML `StartupConfig.xml`. The XML contains list of plugins and commands to be invoked next.
- Three next commands are: `StartHelloPackageCommand` (`HELLO_PACKAGE` message; heartbeat by `UserPlugin.swf`), `SetupGameLoadingCommand` (client-only), `LoginCommand` (`LOGIN` message; request player data by `LoginPlugin.swf`).
- During one of the plugin loading, the `RSModelPlugin.swf` is loaded, which continues to the client sending server `CMD_HONOR_GET_SETTING` opcode.
- All this result in 3 request to server: `CMD_HONOR_GET_SETTING`, `HELLO_PACKAGE`, and `LOGIN`.
