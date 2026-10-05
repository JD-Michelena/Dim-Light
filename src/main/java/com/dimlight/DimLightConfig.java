package com.dimlight;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;

@ConfigGroup(DimLightConfig.GROUP)
public interface DimLightConfig extends Config
{
	String GROUP = "dimlight";

	@ConfigSection(
		name = "Hotkeys",
		description = "Keyboard shortcuts",
		position = 10
	)
	String hotkeysSection = "hotkeys";

	@Range(min = 0, max = 100)
	@ConfigItem(
		keyName = "darkness",
		name = "Darkness (%)",
		description = "0 = no change, 100 = fully black",
		position = 1
	)
	default int darkness()
	{
		return 50;
	}

	@ConfigItem(
		keyName = "tint",
		name = "Overlay color",
		description = "Black is the default; use a dark blue or red tint if you prefer",
		position = 2
	)
	default Color tint()
	{
		return Color.BLACK;
	}

	@ConfigItem(
		keyName = "dimInterface",
		name = "Dim interface too",
		description = "Also darkens the inventory, chatbox, minimap and other interface elements",
		position = 3
	)
	default boolean dimInterface()
	{
		return false;
	}

	@ConfigItem(
		keyName = "smoothTransition",
		name = "Smooth transition",
		description = "Fade between darkness levels and when toggling on/off instead of changing instantly",
		position = 4
	)
	default boolean smoothTransition()
	{
		return true;
	}

	@Range(min = 100, max = 2000)
	@ConfigItem(
		keyName = "fadeDuration",
		name = "Fade duration (ms)",
		description = "How long the fade takes",
		position = 5
	)
	default int fadeDuration()
	{
		return 500;
	}

	@ConfigItem(
		keyName = "indicatorMode",
		name = "Level indicator",
		description = "Show the darkness level when it changes: as on-screen text, as a chat message, or both",
		position = 6
	)
	default IndicatorMode indicatorMode()
	{
		return IndicatorMode.ON_SCREEN;
	}

	@Range(min = 500, max = 5000)
	@ConfigItem(
		keyName = "indicatorDuration",
		name = "Indicator duration (ms)",
		description = "How long the on-screen text stays visible",
		position = 7
	)
	default int indicatorDuration()
	{
		return 2000;
	}

	@ConfigItem(
		keyName = "toggleHotkey",
		name = "Toggle on/off",
		description = "Turns the dimming on or off without changing your darkness level",
		section = hotkeysSection,
		position = 11
	)
	default Keybind toggleHotkey()
	{
		return new Keybind(KeyEvent.VK_F12, InputEvent.CTRL_DOWN_MASK);
	}

	@ConfigItem(
		keyName = "increaseHotkey",
		name = "Darker",
		description = "Increases darkness by the step amount",
		section = hotkeysSection,
		position = 12
	)
	default Keybind increaseHotkey()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "decreaseHotkey",
		name = "Lighter",
		description = "Decreases darkness by the step amount",
		section = hotkeysSection,
		position = 13
	)
	default Keybind decreaseHotkey()
	{
		return Keybind.NOT_SET;
	}

	@Range(min = 1, max = 50)
	@ConfigItem(
		keyName = "step",
		name = "Step (%)",
		description = "How much each Darker/Lighter key press changes the darkness",
		section = hotkeysSection,
		position = 14
	)
	default int step()
	{
		return 10;
	}
}
