package dev.relish.modules;

import dev.relish.Gfx;
import dev.relish.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class Module {
	public final String name;
	public boolean enabled = true;
	public int x;
	public int y;
	public final int defaultX;
	public final int defaultY;

	protected Module(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.defaultX = x;
		this.defaultY = y;
	}

	/** Draw this module on the HUD. Only called while enabled. */
	public void render(GuiGraphicsExtractor g, Minecraft mc) {}

	/** Called once per client tick while enabled. */
	public void tick(Minecraft mc) {}

	/** Size of the module's bounding box, used by the HUD editor. 0 = not a HUD element. */
	public int width(Minecraft mc) { return 0; }
	public int height(Minecraft mc) { return 0; }

	public boolean isMovable() { return true; }

	public void resetPosition() {
		x = defaultX;
		y = defaultY;
	}

	public boolean contains(Minecraft mc, double px, double py) {
		int w = width(mc);
		int h = height(mc);
		return w > 0 && h > 0 && px >= x && px <= x + w && py >= y && py <= y + h;
	}

	/** Draws a small translucent label box at (bx, by) and returns its width. */
	protected int box(GuiGraphicsExtractor g, Minecraft mc, String text, int bx, int by) {
		int w = mc.font.width(text) + 10;
		Gfx.roundRect(g, bx, by, w, 14, 3, Theme.HUD_BG);
		g.fill(bx, by + 3, bx + 1, by + 11, Theme.ACCENT);
		g.text(mc.font, text, bx + 5, by + 3, Theme.TEXT, true);
		return w;
	}
}
