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

### Important files

- `entry.swf` is the main entry point of the game, connecting to other SWF files.
- `naruto.core.swf` contains game code of core features like logging, crypto, protobuf, debug, and other infra code.
- `naruto.include.swf` is the main SWF containing gameplay code and protocol definitions (protobuf).
- `NarutoServer.swf` (inside `flash/item2.pkg`) contains socket code.

### Networking

Socket code is located at `NarutoServer.swf/SocketServer.as` and protocol and other stuff is defined at `naruto.include.swf`.

- Each message has an opcode (there are 2000+ opcodes)
- Each message brings optional client data
- Almost every message is encrypted using TEA algorithm with a hardcoded key, except for login and get encrypt list.
- Similarly, almost every response from server is encrypted as well.
- The payload itself utilizes ProtoBuf.
- The `SocketProtocol` is a wire-level wrapper for any message. It contains a client request message and server response message for later.
- Every message uses the class `Message`, which a lot of implementations are ProtoBuf generated code.
- The `encodeByMessage` is called for any request. Its payload will be logged and then each message's ProtoBuf auto-generated packaging code `writeToBuffer` will be called.
- The `writeToBuffer` results writes to the `bytes` internal buffer on each `Message` class.
- If encryption is enabled, that result `bytes` will be encrypted.
- Once encryption is done, the bytes will be packaged into wire-format using `data` defined by `SocketProtocol`.
- And finally, send it to server.
