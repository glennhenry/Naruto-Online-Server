---
title: Intro
slug: index
description: Intro
---

Naruto Online is a flash game.

- [File formats](docs/file-format)

### Changes

Below contains the changes made to the original files.

Certain changes such as changing external domain to localhost is not required, but recommended to ensure our client do not make requests to official server.

- huoying.qq domain for login cgi
- huoying.qq domain for empty.swf

#### entry.swf

1. `entry.as` changed login cgi url to localhost (recommended)
2. `ServerInfoRequest.as` changed `defaultIp` to localhost (not required)

#### item2.pkg

Edit the `NarutoServer.swf` embedded inside `item2.pkg`.

1. Changed every external IP to localhost (not required)

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

- The `SocketProtocol` is a wire-level wrapper for socket message. It contains client message and server message field for later.
- Each socket message has an opcode (there are 1500+ opcodes), also known as command ID.
- Other than opcode, there are also packet identification information such as packet serial number, player ID, server ID, and role.
- Each socket message has an optional payload.
- Almost every message's payload is encrypted using TEA algorithm with a hardcoded key, except for login and get encrypt list command.
  - The get encrypt list command asks the server whether further communication should be encrypted or not. If yes, the server may also provide the list of opcode that is excluded from encryption.
  - If no, then any communication, whether its client to server or vice versa, will be in plain text.
- The payload itself utilizes ProtoBuf. They typically inherit the class `Message`.
- The `encodeByMessage` is called for any request. Its payload will be logged and then each message's ProtoBuf auto-generated packaging code `writeToBuffer` will be called.
- The `writeToBuffer` results writes to the `bytes` internal buffer on each `Message` class.
- If encryption is enabled, that result `bytes` will be encrypted.
- Once encryption is done, the bytes will be packaged into wire-format using `data` defined by `SocketProtocol`.
- And finally, send it to server.

Packet wire representation details:

```
[2 bytes short] magic number 2305
[4 bytes uint] payload length
[4 bytes uint] opcode, also known as command ID
[4 bytes uint] packet serial number
[4 bytes uint] fixed number 1
[8 bytes double] fixed number 0
[4 bytes uint] self player ID
[2 bytes short] self role
[2 bytes short] server ID
[4 bytes uint] fixed number 0
[N bytes] payload bytes
```

Server response structure is similar.

```
[2 bytes short] magic number 2305
[4 bytes uint] payload length
[4 bytes uint] opcode, also known as command ID
[4 bytes uint] packet serial number
[4 bytes uint] type
[20 bytes] dummy bytes
[N bytes] payload bytes
```

- In the payload, every response message should have `ret` or `retInfo` which is a field that provides error code and error message to client. This is optional — error code should be 0 if there is not error.
- The `type` seems to be an identifier for which action the server wants the client to perform. Certain responses or server-initiated message doesn't have payload, and the type represent a client action.
- The 20 dummy bytes is required as per client expectation.

### Command System

Client leveraged _command_ system as the architecture to perform certain actions, typically used to make a request to server.

Client creates a class that implements `Command`. Once it is constructed, the client typically invoke the `call` method on it. This initiates a request to execute that command.

Each command has an executor which implements the `Executer` class. These executors are responsible to handle the command request. An executor can handle multiple commands.

For example, `SysReportCommand` has an executor of `ReportExecuter`. Inside `doCommand` of `ReportExecutor`, there is switch statements that maps multiple command into their respective function that handles it.

The `sysReport` is the function that handles `SysReportCommand`. Inside the function, it calls the networking layer to make a request to the server, sending the opcode `250881` (SYS_REPORT).

### Plugin System

Naruto Online don't bundle all their code in one single SWF, but use the notion of plugin, where it bundles code and logic for a specific domain.

For example, registering uses the plugin `RegisterPlugin.swf`, while login uses `LoginPlugin.swf`.

Plugin system can make code searching more difficult because every code is scattered and each of the SWFs are decompiled. A command can exist in `naruto.core.swf`, but its execution elsewhere. Though, each command defines plugin name which indicates the plugin that should be installed to run them. The `naruto.include.swf/def/PluginDef` contains the list of plugin definitions.

Certain plugin has their pairing of XML file, which act as the configuration. For example, when the `StartupCommand` is invoked, the `StartupPlugin.swf` already registered beforehand is initialized. This continues to `StartupPlugin.as` reading the `StartupConfig.xml`, which in this case initialize other plugins and call other commands.

### System Report

System report, also known as `SYS_REPORT` or the opcode 250881, is a message sent by client intended to inform the server for system information. It's like a client sending logs to server, but those logs is typically useful for server-side.

For example, during register/loading, the game sends information like:

- with id `PROTO_REPORT_LOG_ID_LOGIN_NODE` message `FINISH_NINJA_SELECT` (both sent in number, but we map it to the constants identifier)
- same id, message `CREATE_ROLE_SUCCESSFUL`
- message `GAME_LOADING_PERCENT_20`
- message `GAME_LOADING_PERCENT_40`
- and many more...
