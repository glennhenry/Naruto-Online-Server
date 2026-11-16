---
title: Tools
slug: tools
description: Tools
---

Various of tools can be made for Naruto Online.

1. `resource.cfg` viewer (decode, export, view); upload `resource.cfg`, decode it, then display a table view.
2. Various assets format viewer (decode, export, view); upload a file (e.g., `.xml`, `.cfg`, `.pkg`, etc.), decode it, then display textual representation.
3. Assets explorer; visualize available assets on disk, search an assets file across different versions.
4. Assets mirror; download assets from remote, remove unneeded local assets.

Ideally, the devtools are implemented in the backstage as separate routes. So, user/dev may visit `/backstage/tools` for the list of tools, then endpoint like `/backstage/tools/rescfg_viewer` for `resource.cfg` viewer.

However, implementing devtools is such a chore:

- Implement the tools in code (easy)
- Create UI in HTML/CSS/JS (moderate, chore)
- Implement backend endpoint in code and JS request/response handling (moderate, chore)

Instead, we can write interfaces to the tools as a form of tests. The `Tools.kt` in our server code of test package should contain all the tests.

The `Tools.kt` interface is temporary — ideally a devtools with UI should be implemented in the future.
