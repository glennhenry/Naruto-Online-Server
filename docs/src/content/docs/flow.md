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
