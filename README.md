# XP Ring

A simple XP ring that lets you save experience levels and retrieve them later.

Available for:

- Minecraft **1.21.1** with **NeoForge 21.1.248**
- Minecraft **1.20.1** with **Forge 47.4.28**

## Features

- Right-click the ring to open a simple XP storage screen.
- Save 1, 5, or 10 levels at a time.
- Extract 1, 5, or 10 stored levels.
- See how many levels are currently stored.
- XP is stored separately inside each ring.
- The ring glows while it contains XP.
- Works in single-player and multiplayer.

## Recipe

Craft the ring with one emerald in the center and four gold ingots around it:

```text
 G
GEG
 G
```

`G` = Gold Ingot

`E` = Emerald

## Installation

1. Install one of the supported Minecraft versions.
2. Install the matching Forge or NeoForge loader.
3. Place the XP Ring JAR in the Minecraft `mods` folder.
4. Start Minecraft with the matching mod-loader profile.

The mod must also be installed on the server when playing multiplayer.

## Building from source

For Minecraft 1.21.1 with NeoForge, install Java 21 and run:

```shell
./gradlew build
```

Output: `build/libs/xp_ring-neoforge-1.21.1-1.0.0.jar`

For Minecraft 1.20.1 with Forge, install Java 17 and run:

```shell
cd forge-1.20.1
./gradlew build
```

Output: `forge-1.20.1/build/libs/xp_ring-forge-1.20.1-1.0.0.jar`

## CurseForge

When publishing, use this short summary:

> A simple XP ring to save, store, and retrieve your experience levels.

Upload each JAR as a separate CurseForge file and select its matching Minecraft
version and mod loader.

## License

All Rights Reserved.
