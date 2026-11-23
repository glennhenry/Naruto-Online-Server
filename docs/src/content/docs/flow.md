---
title: Flow
slug: flow
description: Flow
---

- `entry.swf` is the main file
- it downloads `resoruce.cfg`
- it initializes `FileAssetManager` (version map, archives XML)
- it download `LoginConfig.xml`
- with `KeyFileLoader.as` every cfg and rsl is downloaded. THis includes `naruto.core.swf`, `naruto.include.swf`, `naruto.ui.swf`, and `naruto.exui.swf` which I believe is the core extension where many classes and logic are present.

Because `entry.swf` arbitrarily access class that aren't present in that SWF. For example, it doesn't have code download `pkg` archive or socket server connection.

