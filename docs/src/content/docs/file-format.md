---
title: File Format
slug: file-format
description: File format
---

Naruto Online leveraged various file formats. Most notably, it utilizes Adobe's built-in serialization format, Action Message Format 3.

Required decoding techniques:

- LZMA compression
- Zlib compression
- AMF3 serialization format (e.g., object, dictionary, string, etc.)
- Other custom binary structure

### Naruto Online assets

The Naruto Online assets is grouped into their respective version release. The version is a string tag like `EN_NarutoAlpha6.00Build304` — meaning the assets has been present since the version 6.00 Build 304.

:::note
The `EN_NarutoAlpha6.00Build304` is the oldest version number that is still used in the current game. It is allegedly the version 6.0 of Naruto Online on 2019.
:::

Any Naruto Online assets is never overwritten, only re-added in later versions. If `file.swf` is present in version 6.00 Build 304, then an update to `file.swf` is done, they will keep the old version untouched, and create a new file in the newer versions. This implies that **every Naruto Online assets version exists** — as long as the CDN keeps it. In other word, you can find an old version of something even when the current game is using the newer version, you just need to know which version did it appeared.

### Type of assets

Almost all Naruto Online assets contains a compressed section (LZMA/Zlib). Below are various formats and an example of its occurence. Note that different structure of the same file format may exist.

Standard formats, nothing special:

- **gif**: example; `assets/game/EN_NarutoAlpha6.00Build304/assets/activity/800213/chmj.gif`
- **png**: `assets/game/EN_NarutoAlpha6.00Build304/assets/activity/800213/dzmb.png`
- **jpg**: `assets/game/EN_NarutoAlpha6.00Build304/assets/activity/facebook/1.jpg`
- **f4v**: Adobe Flash video; `assets/game/EN_NarutoAlpha6.00Build304/assets/battle/video/21001304.f4v`
- **flv**: Adobe Flash video; `assets/game/EN_NarutoAlpha6.00Build304/assets/battle/video/2GUYzhouhuzhuiya.flv`
- **mp3**: music, sfx; `assets/game/EN_NarutoAlpha6.00Build304/assets/sound/scene/SE/13road.mp3`
- **bmp**: `assets/game/EN_NarutoAlpha6.00Build304/assets/rumorPlot/plotimg/40461.bmp` (neighbor files are `jpg` but this somehow uses `bmp`)
- **JPG**: `assets/game/EN_NarutoAlpha6.00Build304/assets/rumorPlot/plotimg/90160.JPG` (neighbor files are `jpg` but this somehow named `JPG`)

Special formats, introduce compression, AMF, and/or custom structure:

- **xml**: game configuration; XML with Zlib compression; `assets/game/EN_NarutoAlpha6.00Build304/assets/battle/BaojuInfo.xml`
- **cfg**: game configuration; AMF serialization with Zlib compression; `assets/game/EN_NarutoAlpha6.00Build304/config/bag/conf/TreasureMapItemCFG.cfg`
- **xml** variant 2: scene/map configuration; XML with Zlib compression; `assets/game/EN_NarutoAlpha6.00Build304/assets/scene/map/10001_scene_bg.xml`
- **cfg** variant 2: scene/map configuration; AMF seerialization; `assets/game/EN_NarutoAlpha6.00Build304/assets/scene/map/10001_scene_bg.cfg`
- **bin**: scene/map configuration; AMF serialization; likely same structure as above; `assets/game/EN_NarutoAlpha6.00Build304/assets/scene/map/10001_scene_bg.bin`
- **map**: scene/map configuration; likely bitmap; `assets/game/EN_NarutoAlpha6.00Build304/assets/scene/map/10001/MapInfo.map`
- **pkg**: assets bundle file; custom structure embedding assets; `assets/game/EN_NarutoAlpha6.00Build304/assets/archives/item2.pkg`
- **swf**: SWF files, sprite files; standard CWS header; `assets/game/EN_NarutoAlpha6.00Build304/assets/role/npc/scene/npcres/60001_outdoor.swf`
- **swf**: SWF files, code; specially obfuscated SWF; `assets/game/EN_NarutoAlpha6.00Build304/flash/baoju/BaoJuPlugin.swf`

Obscure formats, likely unwanted/accidentally added:

- **txt**: Only 1 file, empty content; `assets/game/EN_NarutoAlpha6.00Build304/assets/battle/role/right/soundActionNoticeLog.txt`
- **sh**: Bash script, likely some server utilities; `assets/game/EN_NarutoAlpha6.00Build304/assets/cardPackage/mystery/card2ninja.sh`
- **fla**: Adobe Flash animate file; likely used to create SWF animations; `assets/game/EN_NarutoAlpha6.00Build304/assets/scene/战斗场景.fla`
- **html**: HTML file, showing the same image as `战斗场景.fla`; `assets/game/EN_NarutoAlpha6.00Build304/assets/scene/战斗场景.html`
- **xlsx**: Excel file; the same directory shows various character skill video, this spreadsheet displays the list; `assets/game/EN_NarutoAlpha6.00Build304/assets/skill/videos/技能视频配置.xlsx`
- **bat**: Batch file, likely some server utilities; `assets/game/EN_NarutoAlpha6.00Build304/assets/sound/SYNC12.bat`
- **mme**: 7z compression containing XML files and images that are very old (~2013); The XML is likely data to instruct server to compress the images; `assets/game/EN_NarutoAlpha6.00Build304/assets/task/tasknpc/模板.mme`
- **lnk**: Windows shortcut; `assets/game/EN_NarutoAlpha6.72Build300/assets/battle/effect.lnk`
- **db**: Windows thumbnail file; `assets/game/EN_NarutoAlpha6.00Build304/assets/newplot/dialog/11005011/Thumbs.db`
- **inc**: almost empty file, containing only `{}`; `assets/game/EN_NarutoAlpha7.34Build300/flash/battle/initializer.as.inc`
- _no extension_: empty file; could be a mistake, though in the directory, there's a naming pattern where this file sort of act like a separator between different naming scheme; `assets/game/EN_NarutoAlpha6.00Build304/assets/treasureHunting/tipsPic/1007001`

### resource.cfg

`resource.cfg` is a file that when decoded, contains a dictionary of assets versions and XML data of assets bundle.

- The version map is a big entry listing all Naruto Online assets. Each entry list an assets URL, version tag, revision number, and some others. More specifically, it _likely_ includes all files that are stored in the Naruto Online remote server. Unnecessary files like `Thumbs.db`, Adobe animate files, Bash script, and some others are included.
- Also called archives, this XML contains Naruto Online assets packages that is essential to launch the game. The Naruto Online assets is separated into three: `assets`, `config`, and `flash`. The archives XML follow that structure, and each structure is grouped by a `.pkg` file. See about [.pkg](#package-file).

#### Versioning details

`resource.cfg` isn't constant and is updated on each new version of Naruto Online (which is released every Thursday in the EN version). In other word, during Thursday update, a new `resource.cfg` version will be pushed into a new version. The new version could either change the minor version (6.00 to 6.01, up to 6.99 then 7.00) or increasing the build number (Build 300 to Build 301). The build number is a patch within that week, while a change on minor version represent a new week cycle.

A patch happens when anything in the game is updated. This includes a removal of event in the middle of week, hotfix, change in rewards, etc. that are configured from file directly. This patch is pushed by editing a particular file and updating the `resource.cfg` list to pinpoint the new version of that file, which therefore results in a new `resource.cfg` that are listed under the increased build number.

When a new week cycle happens, in-game events are updated, and so is various assets that configures the game (e.g., configuring available events, rewards, and schedule). This results in a new minor version.

#### Format details

The detail of `resource.cfg` format as follows:

```
LZMA compression
    -> [4 bytes] AMF data length
    -> AMF data
    -> [1 byte] is archives XML present flag
        -> if the flag is not 0
        -> [4 bytes] XML length
        -> XML string UTF-8
```

- This means the entire `resource.cfg` is LZMA compressed.
- After compressing, the first 4 bytes represent the bytes length of AMF data.
- An AMF decoder should decode this portion, returning an AMF dictionary. This AMF dictionary represent the version map.
- The next is a single byte representing whether archives XML is present.
- If XML is present, the next is 4 bytes representing XML length in bytes.
- The rest of bytes are XML data in UTF-8

#### Version Map

Example of an entry in the version map (exported as JSON):

```json
"flash/honor/HonorUI.swf": {
    "size": 637165,
    "belong": null,
    "tag": "EN_NarutoAlpha7.34Build300",
    "ver": 3,
    "url": "flash/honor/HonorUI.swf",
    "__amfType": "alias.com.tencent.morefun.naruto.file::FileAssetInfo"
}
```

This means that the assets `flash/honor/HonorUI.swf` was last updated during the release of version 7.34 Build 300, and is apparently the second-time it was updated according to `ver` (making this the third version).

:::note
`__amfType` is our server custom marker to track the original AS3 class when we export them to foreign format like JSON.
:::

:::tip
Since `resource.cfg` is updated on each version, and that the Naruto Online CDN never deletes old assets, you can also download old version of `resource.cfg`. An old `resource.cfg` will list old assets.

Theoretically, you can build a Naruto Online server that sets the `resource.cfg` to some old version (like 6.00 Build 304, around 2019) and emulate the server like you are playing the 2019 version of Naruto Online.

Even more, it _should_ be possible to play 1.00 version (which is probably 2016/2017), which likely had system that has been removed up to now (e.g., old convoy, group instance, chase trial), or even combines multiple versions like having the old version of ninja (e.g., Sailor Sakura) but modern version of events.

Though, combining contents is a matter of editing the game's configuration — editing the `resource.cfg` to change the required assets is just another way of doing it.
:::

#### Archives XML

Example of archives XML (exported as XML):

```xml
<archives>
  <flash>
    <archive id="1" num="61" url="flash/archives/item1.pkg">
      <item url="flash/activity/ActivityPluginConfig.xml"/>
      <item url="flash/arena/arenaBadge.xml"/>
      <item url="flash/arena/ArenaConfig.xml"/>
      <item url="flash/cardPackage/CardPackageConfig.xml"/>
      <item url="flash/chat/ChatConfig.xml"/>
      ...
    </archive>
    <archive id="2" num="9" url="flash/archives/item2.pkg">
      <item url="flash/server/NarutoServer.swf"/>
      ...
    </archive>
  </flash>
  <assets>
    ...
  </assets>
  <config>
    ...
  </config>
```

The XML contains a list of packages file that the game is supposed to download and extract. Each package file contains necessary assets for game startup.

- `id`: Unique identifier of the `pkg` file within each collection
- `num`: The number of assets embedded inside the pkg file
- `url`: The relative assets URL of the `pkg` file
- `item.url`: The relative assets URL of file content inside

For example, in the `flash` category of assets collection, the archive package `item1.pkg` is a file that embeds 61 child assets, while the `item2.pkg` embeds 9 child assets.

Using the URL, the game may consult the version map to obtain the appropriate version and construct the remote URL to download the file.

### Package file

The package file (`.pkg`), also known as archives or assets bundle, is a custom file format that the game uses to package assets together. While the archives XML list every `pkg` files and its contents, the `pkg` embed directly the listed files inside.

It is part of the essential game bootstrap.

- Download `resource.cfg`
- Decode and parse the archives XML
- Download each `pkg` file
- Extract each `pkg` file

Assets embedded inside pkg files are also present in the version map. In other word, you can obtain the contents inside the pkg file without extracting it, but by downloading them directly.

pkg files don't change a lot since it simply list the essential game assets and configuration.

#### Format details

**Every assets embedded inside pkg file is uncompressed**, this includes the XML and CFG files, and the unencrypted SWF.

```
Zlib compression
    -> [2 bytes] filename length
    -> filename string UTF-8
    -> [4 bytes] file length
    -> file bytes
```

The entire file is Zlib compressed, then it follows a custom structure. The amount of files bundled inside pkg is in accordance to what archives XML listed.

### XML data

The `.xml` files are assets that configures in-game contents. The format is very simple — it's simply a Zlib compressed XML.

For example, when `ArenaBadgeCFG.xml` is decoded:

```xml
<?xml version="1.0" encoding="UTF-8"?>

<items>
	<row>
		<id>1</id>
		<name>Ninja School Student</name>
		<point>4</point>
		<typeName>Ninja School Student Reward</typeName>
	</row>
  ...
</items>
```

### CFG data

The `.cfg` files are assets that configures in-game contents. It is similar with the XML format; in some cases, there exist XML and CFG version of the same configuration.

CFG files are AMF3 (Action Message Format 3) encoded. They are real ActionScript 3 classes in the game's code, but is serialized. Specifically, `.cfg` files are Zlib-compressed AMF3 payload.

For example, `HomeGiftItemCFG` exported to JSON:

```json
{
  "44300006": {
    "__amfType": "HomeGiftItemCFG",
    "name": "Ninja Hound Puppet",
    "id": 44300006,
    "desc": "A plushy that looks a lot like a young Akamaru."
  },
  "44300005": {
    "__amfType": "HomeGiftItemCFG",
    "name": "Piggy Bank",
    "id": 44300005,
    "desc": "A cute piggy bank made to look like a piggy."
  },
  ...
}
```

The file's content is serialized as `Dictionary`. It uses a string key and the value is an `Object` which is the class `HomeGiftItemCFG` that has the field `name`, `id`, and `desc`.

:::note
`__amfType` is our server custom marker to track the original AS3 class when we export them to foreign format like JSON.
:::

### Map files

There are 4 formats used for map:

- **xml**: e.g., `10001_scene_bg.xml`
- **cfg** `10001_scene_bg.cfg`
- **bin**: `10001_scene_bg.bin`
- **map**: `MapInfo.map`

There are three layers for each map: background, foreground, and middleground.

- Background is the background image you see in the game.
- Foreground is currently unknown — I have checked that an asset file was missing from version map. Maybe the foreground layer is not used?
- Middleground is animated object in the scene. Inside the SWF, there's AMF and XML data configuring the animation.

There are always `.xml`, `.cfg`, and `.bin` for each layer (total 9), a `.map` for each scene area, `alphaMap.png`, `sceneMap.jpg`, and `sceneMap.png`. Together this results in 13 files for any area. There is also `ImgItem` folder which contains the background image and SWF animation.

- The `xml` files are standard Zlib-compressed XML, same as [XML data](#xml-data).
- The `cfg` and `bin` file seems to contain the exact same bytes. They are uncompressed AMF encoded data, similar to [CFG data](#cfg-data) but uncompressed.
- The usage and format details of `map` file remains unknown. It contains binary structure that is repetitive and geometrically structured — possibly it's some kind of bitmap that configures the map. I'm talking about a file that determines which coordinate is walkable, pass-through, or something else.

The `xml`, `cfg`, and `bin`, although different format, all contain the same configuration. It remains unknown that which file does the game utilized, and why is there 3 separate files.

### SWF

Naruto Online rely on multiple SWF files. `entry.swf` is the main SWF file that initialize the game and download child SWF. SWF may either be a sprite or animation (like character model, scene animation) or contain code. Every SWF files are specially obfuscated, except `entry.swf` and sprites. Another exception is SWF files extracted from [pkg files](#package-file).

#### Format details

This includes almost every SWF listed in the version map excluding sprites SWF. For example, `naruto.core.swf`, `BaoJuPlugin.swf`, `BattlefieldPlugin.swf`, and many more.

This SWF can't be decompiled in JPEXS. It has a `ZWS` header which denotes an LZMA-compressed SWF. However, the structure seems to be non-standard.

**The SWF is apparently obfuscated in order to fool Flash decompiler like JPEXS.**

The standard SWF header is `[3 bytes header][1 byte version][4 bytes uncompressed length]`, where header can be `ZWS` (LZMA), `CWS` (Zlib), `FWS` (uncompressed).

However, Naruto Online SWF is made like:

```
[3 bytes] ZWS -> fake header
[1 byte]  0   -> fixed; fake version
[4 bytes] FF FF FF FF -> fixed; fake length
```

Then, the bytes of real headers and real length follows but they are mixed together. Afterward, to hide the obvious compression header like `0x78 0xda` (Zlib) or the 13 bytes header of LZMA, the subsequent bytes (up to 128 bytes) are also scrambled. After that, the real bytes of SWF follows.

Details:

```
[3 bytes] ZWS -> fake header
[1 byte]  0   -> fixed; fake version
[4 bytes] FF FF FF FF -> fixed; fake length
[11 bytes] scrambled real header + file length
[<= 128 bytes] scrambled version of the first 128 bytes of the compressed body
```

:::tip
For algorithm, see `entry.swf:util.decryptSWF`.

Note: `decryptSWF` supports real SWF that aren't obfuscated this way.
:::
