---
title: Intro
slug: index
description: Intro
---

Documentation about Naruto Online.

### File Formats

Required decoding techniques:
- LZMA
- AMF3 (not Flash remoting)

- `resource.cfg` is an LZMA-compressed file that contains:
  - AMF3-encoded dictionary version map, which is a big entry of all Naruto Online assets.
  - Optional (generally always present) XML part that list Naruto Online assets required by the game to start. It is divided into 3 directory: `flash`, `assets`, `config` and all of them is associated with its respective `.pkg` files under a common `archive` grouping.
