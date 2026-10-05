# Dim Light

A RuneLite plugin that darkens the game screen to any level from 0% to 100%, in every area of the game.

## Features

- **Darkness slider (0-100%)**: 0% leaves the game untouched, 100% is fully black.
- **Overlay color**: use black or pick a tint.
- **Dim interface too** (optional): also darkens the inventory, chatbox and minimap. Off by default, so only the game world is dimmed.
- **Smooth transition**: changes and on/off fade in and out. The duration is configurable.
- **Hotkeys**: toggle the dimming on/off (default `Ctrl+F12`) and raise or lower the darkness by a configurable step.
- **Level indicator**: shows the current level (for example "Dim Light: 60%") on screen, in the chat, or both.

## Notes

- Dim Light only draws a semi-transparent layer over the screen. It does not read or display any extra game information and does not interact with the game in any way.
- It makes no network requests and has no third-party dependencies.
- It cannot make the game brighter than it already is, only darker. If you also use a brightness or exposure setting in 117HD, both effects add up.

## License

BSD 2-Clause. See `LICENSE`.
