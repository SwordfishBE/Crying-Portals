# Crying Portals

Build Nether portals from crying obsidian, obsidian, or any combination of the two.

## Installation

Put `crying_portals-<version>.jar` in the server's `mods` folder, or in your client's `mods` folder for singleplayer.
Clients joining a modded server do not need this mod.
Replace the original Crying Portals mod with this version. 

Light the frame as usual with flint and steel or a fire charge.
Vanilla portal sizes, corner rules, travel, linking, and frame breaking behavior are preserved.
No settings, commands, polling tasks, custom blocks, or mixins are needed.

## Implementation

Minecraft uses `minecraft:nether_portal_frame` for both frame validation and ignition checks.
This mod adds `minecraft:crying_obsidian` to that tag with `replace: false`, preserving vanilla and other mods' entries.
A matching additive `wover:nether_pframe` tag is included for WorldWeaver compatibility; WorldWeaver itself is optional and has not been tested.

## Building and testing

Run `./gradlew.bat build --console=plain` on Windows, or `./gradlew build` on Linux/macOS.
Requires a full JDK 25.

Install the ordinary JAR from `build/libs`, not the sources JAR.

## Credits and license

Copyright (c) 2026 SwordfishBE.
Licensed under AGPL-3.0; see [LICENSE](LICENSE).