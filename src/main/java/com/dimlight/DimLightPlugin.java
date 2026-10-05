package com.dimlight;

import com.google.inject.Provides;
import java.util.function.Supplier;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.Keybind;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
	name = "Dim Light",
	description = "Dim the game screen from 0 to 100% in any area. Works with GPU and 117HD.",
	tags = {"brightness", "dark", "dim", "117hd", "light"}
)
public class DimLightPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private KeyManager keyManager;

	@Inject
	private ConfigManager configManager;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private DimLightConfig config;

	@Inject
	private DimLightState state;

	@Inject
	private DimLightOverlay sceneOverlay;

	@Inject
	private DimLightInterfaceOverlay interfaceOverlay;

	private final HotkeyListener toggleHotkey = hotkey(() -> config.toggleHotkey(), this::toggle);
	private final HotkeyListener darkerHotkey = hotkey(() -> config.increaseHotkey(), () -> changeDarkness(config.step()));
	private final HotkeyListener lighterHotkey = hotkey(() -> config.decreaseHotkey(), () -> changeDarkness(-config.step()));

	@Override
	protected void startUp()
	{
		state.refreshSettings();
		state.reset();
		overlayManager.add(sceneOverlay);
		overlayManager.add(interfaceOverlay);
		keyManager.registerKeyListener(toggleHotkey);
		keyManager.registerKeyListener(darkerHotkey);
		keyManager.registerKeyListener(lighterHotkey);
	}

	@Override
	protected void shutDown()
	{
		keyManager.unregisterKeyListener(toggleHotkey);
		keyManager.unregisterKeyListener(darkerHotkey);
		keyManager.unregisterKeyListener(lighterHotkey);
		overlayManager.remove(sceneOverlay);
		overlayManager.remove(interfaceOverlay);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!DimLightConfig.GROUP.equals(event.getGroup()))
		{
			return;
		}

		state.refreshSettings();

		// Moving the slider: make sure the change is visible and show the level on screen only
		if ("darkness".equals(event.getKey()))
		{
			state.setActive(true);
			announce(false);
		}
	}

	private void toggle()
	{
		state.setActive(!state.isActive());
		announce(true);
	}

	private void changeDarkness(int delta)
	{
		int value = Math.max(0, Math.min(100, config.darkness() + delta));
		state.setActive(true);
		configManager.setConfiguration(DimLightConfig.GROUP, "darkness", value);
		announce(true);
	}

	private void announce(boolean allowChat)
	{
		IndicatorMode mode = config.indicatorMode();
		String text = state.isActive() ? "Dim Light: " + config.darkness() + "%" : "Dim Light: Off";

		if (mode.showsOnScreen())
		{
			state.showIndicator(text, config.indicatorDuration());
		}

		if (allowChat && mode.showsInChat())
		{
			clientThread.invoke(() -> sendChatMessage(text));
		}
	}

	private void sendChatMessage(String text)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		chatMessageManager.queue(QueuedMessage.builder()
			.type(ChatMessageType.CONSOLE)
			.runeLiteFormattedMessage(new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(text)
				.build())
			.build());
	}

	private static HotkeyListener hotkey(Supplier<Keybind> keybind, Runnable action)
	{
		return new HotkeyListener(keybind)
		{
			@Override
			public void hotkeyPressed()
			{
				action.run();
			}
		};
	}

	@Provides
	DimLightConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DimLightConfig.class);
	}
}
