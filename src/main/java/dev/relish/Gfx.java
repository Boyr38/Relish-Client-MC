package dev.relish;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Small drawing helpers (rounded rectangles, colour blending). */
public final class Gfx {
	private Gfx() {}

	/** Filled rectangle with rounded corners of radius r (r should be <= h/2). */
	public static void roundRect(GuiGraphicsExtractor g, int x, int y, int w, int h, int r, int color) {
		if (r <= 0 || w < 2 * r || h < 2 * r) {
			g.fill(x, y, x + w, y + h, color);
			return;
		}
		for (int i = 0; i < r; i++) {
			double dy = r - i - 0.5;
			int inset = (int) Math.round(r - Math.sqrt(r * r - dy * dy));
			g.fill(x + inset, y + i, x + w - inset, y + i + 1, color);
			g.fill(x + inset, y + h - 1 - i, x + w - inset, y + h - i, color);
		}
		g.fill(x, y + r, x + w, y + h - r, color);
	}

	public static int lerpColor(int a, int b, float t) {
		t = Math.max(0f, Math.min(1f, t));
		int ar = (a >> 16) & 0xFF, ag = (a >> 8) & 0xFF, ab = a & 0xFF, aa = (a >>> 24);
		int br = (b >> 16) & 0xFF, bg = (b >> 8) & 0xFF, bb = b & 0xFF, ba = (b >>> 24);
		int rr = (int) (ar + (br - ar) * t);
		int rg = (int) (ag + (bg - ag) * t);
		int rb = (int) (ab + (bb - ab) * t);
		int ra = (int) (aa + (ba - aa) * t);
		return (ra << 24) | (rr << 16) | (rg << 8) | rb;
	}
}
