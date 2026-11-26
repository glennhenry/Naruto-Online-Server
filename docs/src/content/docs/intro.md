---
title: Intro
slug: index
description: Intro
---

Naruto Online is a flash game.

- [File formats](docs/file-format)

### Changes

Below contains the changes made to the original files.

#### entry.swf

1. `entry.as` changed login cgi url to localhost
2. `ServerInfoRequest.as` changed `defaultIp` to localhost

#### item2.pkg

Edit the `NarutoServer.swf` embedded inside `item2.pkg`.

1. Changed every external IP to localhost

### Logging

Naruto Online own a logging system through `naruto.core.swf`, located in the package `com.tencent.morefun.framework.log`. The logger has two output targets: IDE output and file output.

- Currently, it is unknown where the IDE output is produced to.
- The file output exports all game's log into a file with Zlib compression + Base64 encoding.

Example of log:

```log
#isDebugger:	true
■#os:	Windows 10
■#language:	en_gb
■#version:	WIN 32,0,0,465
■#hasAudio:	true
■#screenResolutionX:	1920
■#screenResolutionY:	1080
■#hasMP3:	true
■#LogVersion:	1
■2026-11-26 14:12:36.563	CORE	3	start flash crashed detect
■2026-11-26 14:12:36.568	CORE	3	[plugin][LoadManager][load]flash/server/ServerConfig.xml3802
■2026-11-26 14:12:36.575	CORE	3	[plugin][LoadManager][load]flash/sound/SoundConfig.xml3809
■2026-11-26 14:12:36.576	[thread::main]	pushTask->skip:flash/archives/item1.pkg, cause:loading
```

To export the log, you must type the keyword `hylogfile`.
