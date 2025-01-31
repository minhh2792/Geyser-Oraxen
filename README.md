<img src="https://dl.dropboxusercontent.com/s/kmke6kchxcwftsg/geyser-1760-860.png" alt="Geyser" width="600"/>

[![forthebadge made-with-java](https://forthebadge.com/images/badges/made-with-java.svg)](https://java.com/)

[![License: MIT](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Build Status](https://ci.opencollab.dev/job/Geyser/job/master/badge/icon)](https://ci.opencollab.dev/job/GeyserMC/job/Geyser/job/master/)
[![Discord](https://img.shields.io/discord/613163671870242838.svg?color=%237289da&label=discord)](https://discord.gg/geysermc)
[![HitCount](http://hits.dwyl.com/Geyser/GeyserMC.svg)](http://hits.dwyl.com/Geyser/GeyserMC)
[![Crowdin](https://badges.crowdin.net/geyser/localized.svg)](https://translate.geysermc.org/)

Geyser is a bridge between Minecraft: Bedrock Edition and Minecraft: Java Edition, closing the gap from those wanting to play true cross-platform.

Geyser is an open collaboration project by [CubeCraft Games](https://cubecraft.net).
#YOU MUST HAVE ORAXEN'S RESOURCE PACK PROTECTION DISABLED
This fork of geyser is still in development, there WILL be bugs
if you do encounter any bugs, please join the discord oraxen discord server at https://discord.gg/4Qk5kBT9UX

## What is Geyser?
Geyser is a proxy, bridging the gap between Minecraft: Bedrock Edition and Minecraft: Java Edition servers.
The ultimate goal of this project is to allow Minecraft: Bedrock Edition users to join Minecraft: Java Edition servers as seamlessly as possible. However, due to the nature of Geyser translating packets over the network of two different games, *do not expect everything to work perfectly!*

Special thanks to the DragonProxy project for being a trailblazer in protocol translation and for all the team members who have joined us here!

### Currently supporting Minecraft Bedrock 1.17.10 - 1.17.40 and Minecraft Java 1.17.1.

## Setting Up
Take a look [here](https://github.com/GeyserMC/Geyser/wiki/Setup) for how to set up Geyser.

[![YouTube Video](https://img.youtube.com/vi/U7dZZ8w7Gi4/0.jpg)](https://www.youtube.com/watch?v=U7dZZ8w7Gi4)

Navigate to config.yml and scroll down to custom-model-data-mappings section and put the corresponding oraxen texture, along with
whether the item is a tool (a pickaxe, axe, sword, etc) or not in this format:
```  
  - "amber_axe;true"
  - "amber_helmet;false"
  - "amber_pickaxe;true"
  - "banana;false"
  ```


## Links:
- Website: https://geysermc.org
- Docs: https://github.com/GeyserMC/Geyser/wiki
- Download: https://ci.geysermc.org
- Discord: https://discord.gg/geysermc
- Donate: https://opencollective.com/geysermc
- Test Server: `test.geysermc.org` port `25565` for Java and `19132` for Bedrock

## What's Left to be Added/Fixed
- Near-perfect movement (to the point where anticheat on large servers is unlikely to ban you)
- Resource pack conversion/CustomModelData
- Some Entity Flags
- Structure block UI

## TODO
- Add support for 3d models (Kind of done)
- Add support for java resource pack to bedrock on the fly (DONE)
- Add custom block thingies(Done-ish)
- fix bugs 

## What can't be fixed
There are a few things Geyser is unable to support due to various differences between Minecraft Bedrock and Java. For a list of these limitations, see the [Current Limitations](https://github.com/GeyserMC/Geyser/wiki/Current-Limitations) page.

## Compiling
1. Clone the repo to your computer
2. [Install Maven](https://maven.apache.org/install.html)
3. Navigate to the Geyser root directory and run `git submodule update --init --recursive`. This command downloads all the needed submodules for Geyser and is a crucial step in this process.
4. Run `mvn clean install` and locate to the `target` folder.

## Contributing
Any contributions are appreciated. Please feel free to reach out to us on [Discord](http://discord.geysermc.org/) if
you're interested in helping out with Geyser.

## Libraries Used:
- [Adventure Text Library](https://github.com/KyoriPowered/adventure)
- [NukkitX Bedrock Protocol Library](https://github.com/NukkitX/Protocol)
- [Steveice10's Java Protocol Library](https://github.com/Steveice10/MCProtocolLib)
- [TerminalConsoleAppender](https://github.com/Minecrell/TerminalConsoleAppender)
- [Simple Logging Facade for Java (slf4j)](https://github.com/qos-ch/slf4j)
