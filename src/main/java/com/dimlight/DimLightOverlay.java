package com.dimlight;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/** Dims only the 3D game scene. Drawn above the scene but under the interface. */
public class DimLightOverlay extends Overlay
{
	private final Client client;
	private final DimLightState state;

	@Inject
	private DimLightOverlay(Client client, DimLightState state)
	{
		this.client = client;
		this.state = state;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D g)
	{
		// When "Dim interface too" is on, the interface overlay covers everything
		if (client.getGameState() != GameState.LOGGED_IN || state.isDimInterface())
		{
			return null;
		}

		Color color = state.getOverlayColor();
		if (color == null)
		{
			return null;
		}

		g.setColor(color);
		g.fillRect(
			client.getViewportXOffset(),
			client.getViewportYOffset(),
			client.getViewportWidth(),
			client.getViewportHeight()
		);
		return null;
	}
}
