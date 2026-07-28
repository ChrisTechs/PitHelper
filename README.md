# PitHelper

PitHelper is a Minecraft Forge clientside mod designed for the Hypixel Pit. 

It provides a collection of QoL utilities like event tracking.

## Features

* **Event Tracking & Overlay:** Uses event data provided by BrookeAFK to track upcoming Pit events and displays them on a HUD overlay.


* **Quick Maths Solver:** **Beware this feature is use at own risk even though it does not strictly go against [Hypixel Allowed Modifications](https://support.hypixel.net/hc/en-us/articles/6472550754962-Hypixel-Allowed-Modifications) as it does not automatically send the answer which still requires human input.** Automatically evaluates Quick Maths chat equations. It can be configured to copy the answer to the clipboard or automatically open chat with the solution.


* **Friend and Enemy Management:** A local system to categorize other players as friends or enemies.


* **Player Highlighting:** Tints friends as green and enemies as red if configured to do so. Tint players with chain and iron armor as pink if configured to do so.


* **Auto Spawn & Death Handling:** QoL features like: Blocks player movement for X seconds after death and keybind toggle auto /spawn which auto enters /spawn as soon as your /spawn cooldown expires. **Beware this feature is use at own risk even though it does not strictly go against [Hypixel Allowed Modifications](https://support.hypixel.net/hc/en-us/articles/6472550754962-Hypixel-Allowed-Modifications) as it does not automatically send commands without human input.**
It is functionally the same as using a keybind to send /spawn combined with a /spawn cooldown countdown.


* **Lobby Tracking:** Keeps track of the current Hypixel server and lobby states. If configured sends a stats breakdown of the current lobby including prestige average and prestige breakdowns.

## Commands

The following commands are registered by the mod:

* `/pithelper` - Opens the main UI and configuration menu.
* `/pitfriend [player]` - Manages the local friend list.
* `/pitenemy [player]` - Manages the local enemy list.


## Limitations:

*   This mod is exclusively available for **Minecraft Forge 1.8.9**.

## Compilation Instructions

If you wish to compile the mod from the source code yourself.

```bash
./gradlew build
```

There is also github actions build that does exactly this.
## License

This project is licensed under the GNU Affero General Public License v3.0 (AGPLv3).

Copyright (c) 2026 Christian Steenkamp

See the LICENSE file in the root of this repository for the full license text.

## Legal & Disclaimer

**PitHelper is a third party mod and is NOT affiliated with, endorsed by, or associated with Hypixel Inc.**

This mod utilizes the Hypixel Public API through a private backend service. All data is fetched and cached according to the Hypixel Developer Policies. Usage of certain features (like the Quick Maths Solver or Auto Spawn) is at the user's own risk; players should remain aware of Hypixel's rules regarding macros and fair play.

The `clayj` UI library included in this project's source code is licensed separately under the zlib/libpng and BSD 3-Clause licenses.
