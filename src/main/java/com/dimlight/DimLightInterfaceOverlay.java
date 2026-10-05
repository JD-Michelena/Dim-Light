package com.dimlight;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Top-most overlay. Dims the whole canvas (inventory, chatbox, minimap...) when
 * "Dim interface too" is on, then draws the level indicator on top of it.
 */
public class DimLightInterfaceOverlay extends Overlay
{
	private final Client client;
	private final DimLightState state;

	@Inject
	private DimLightInterfaceOverlay(Client client, DimLightState state)
	{
		this.client = client;
		this.state = state;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
	}

	@Override
	public Dimension render(Graphics2D g)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return null;
		}

		if (state.isDimInterface())
		{
			Color color = state.getOverlayColor();
			if (color != null)
			{
				g.setColor(color);
				g.fillRect(0, 0, client.getCanvasWidth(), client.getCanvasHeight());
			}
		}

		renderIndicator(g);
		return null;
	}

	private void renderIndicator(Graphics2D g)
	{
		String text = state.getIndicatorText();
		if (text == null)
		{
			return;
		}

		int alpha = (int) Math.round(state.getIndicatorOpacity() * 255);
		if (alpha <= 0)
		{
			return;
		}

		Font oldFont = g.getFont();
		g.setFont(FontManager.getRunescapeBoldFont());
		FontMetrics fm = g.getFontMetrics();

		int x = client.getViewportXOffset() + (client.getViewportWidth() - fm.stringWidth(text)) / 2;
		int y = client.getViewportYOffset() + 40;

		g.setColor(new Color(0, 0, 0, alpha));
		g.drawString(text, x + 1, y + 1);
		g.setColor(new Color(255, 255, 255, alpha));
		g.drawString(text, x, y);

		g.setFont(oldFont);
	}
}
