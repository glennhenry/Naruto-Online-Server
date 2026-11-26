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
- somewhere in one of the SWF, there's code that defines the list of assets that needs to be downloaded for the game startup, including `pkg` files and extracting them.
- by this point, the loading should advance to 14% with `SoundPlugin.swf` being loaded last.
- the next step is the game using `NarutoServer.swf` which connects to the socket server to a set IP from the flashvars.
- `NarutoServer.as` request IP list separated by `;` via standard HTTP request. If there is no response data, it will use one of the two default IPs.
