package com.dimlight;

import java.awt.Color;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * State shared by the overlays: whether dimming is on, the current (animated)
 * darkness, and the on-screen level indicator.
 * <p>
 * Config values used while rendering are cached here and refreshed only when the
 * config changes, so nothing allocates or parses settings every frame.
 */
@Singleton
public class DimLightState
{
	private static final long INDICATOR_FADE_OUT_MS = 300;

	private final DimLightConfig config;

	// Cached config values
	private int darknessSetting;
	private Color tint = Color.BLACK;
	private boolean dimInterface;
	private boolean smoothTransition;
	private int fadeDurationMs;

	private boolean active = true;

	// Fade animation, in percent (0-100)
	private double from;
	private double to;
	private double current;
	private long startTime;

	// Last color handed to the overlays, reused while nothing changes
	private Color cachedColor;
	private Color cachedTint;
	private int cachedAlpha = -1;

	// On-screen indicator
	private String indicatorText;
	private long indicatorUntil;

	@Inject
	DimLightState(DimLightConfig config)
	{
		this.config = config;
	}

	public synchronized void refreshSettings()
	{
		darknessSetting = config.darkness();
		tint = config.tint();
		dimInterface = config.dimInterface();
		smoothTransition = config.smoothTransition();
		fadeDurationMs = config.fadeDuration();
	}

	/** Called on plugin start so the dimming fades in from nothing. */
	public synchronized void reset()
	{
		active = true;
		from = 0;
		to = 0;
		current = 0;
		startTime = System.currentTimeMillis();
		indicatorText = null;
	}

	public synchronized boolean isActive()
	{
		return active;
	}

	public synchronized void setActive(boolean active)
	{
		this.active = active;
	}

	public synchronized boolean isDimInterface()
	{
		return dimInterface;
	}

	/** Color to fill with this frame, or null when nothing should be drawn. */
	public synchronized Color getOverlayColor()
	{
		advanceFade();

		int alpha = (int) Math.round(current * 255 / 100);
		if (alpha <= 0)
		{
			return null;
		}

		if (alpha != cachedAlpha || tint != cachedTint)
		{
			cachedColor = new Color(tint.getRed(), tint.getGreen(), tint.getBlue(), alpha);
			cachedAlpha = alpha;
			cachedTint = tint;
		}
		return cachedColor;
	}

	private void advanceFade()
	{
		double target = active ? darknessSetting : 0;

		if (!smoothTransition)
		{
			from = to = current = target;
			return;
		}

		long now = System.currentTimeMillis();
		if (target != to)
		{
			// Target changed: start a new fade from wherever we are right now
			from = current;
			to = target;
			startTime = now;
		}

		if (current == to)
		{
			return;
		}

		double t = Math.min(1.0, (now - startTime) / (double) fadeDurationMs);
		// Smoothstep easing
		current = t >= 1.0 ? to : from + (to - from) * (t * t * (3 - 2 * t));
	}

	public synchronized void showIndicator(String text, int durationMs)
	{
		indicatorText = text;
		indicatorUntil = System.currentTimeMillis() + durationMs;
	}

	/** Text to show on screen, or null if there is nothing to show. */
	public synchronized String getIndicatorText()
	{
		if (indicatorText != null && System.currentTimeMillis() >= indicatorUntil)
		{
			indicatorText = null;
		}
		return indicatorText;
	}

	/** 1.0 while visible, fading to 0.0 during the last moments. */
	public synchronized double getIndicatorOpacity()
	{
		long remaining = indicatorUntil - System.currentTimeMillis();
		return Math.max(0, Math.min(1.0, remaining / (double) INDICATOR_FADE_OUT_MS));
	}
}
