package dev.relish.modules;

import dev.relish.Gfx;
import dev.relish.Theme;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class KeystrokesModule extends Module {
	private static final int SIZE = 22;
	private static final int GAP = 2;
	private static final int STEP = SIZE + GAP;
	private static final int WIDTH = SIZE * 3 + GAP * 2;
	private static final int HEIGHT = STEP * 4 - GAP;

	public KeystrokesModule(int x, int y) {
		super("Keystrokes", x, y);
	}

	@Override
	public int width(Minecraft mc) { return WIDTH; }

	@Override
	public int height(Minecraft mc) { return HEIGHT; }

	@Override
	public void render(GuiGraphicsExtractor g, Minecraft mc) {
		var o = mc.options;
		key(g, mc, "W", o.keyUp, x + STEP, y, SIZE);
		key(g, mc, "A", o.keyLeft, x, y + STEP, SIZE);
		key(g, mc, "S", o.keyDown, x + STEP, y + STEP, SIZE);
		key(g, mc, "D", o.keyRight, x + STEP * 2, y + STEP, SIZE);
		int half = (WIDTH - GAP) / 2;
		key(g, mc, "LMB", o.keyAttack, x, y + STEP * 2, half);
		key(g, mc, "RMB", o.keyUse, x + half + GAP, y + STEP * 2, WIDTH - half - GAP);
		key(g, mc, "SPACE", o.keyJump, x, y + STEP * 3, WIDTH);
	}

	private void key(GuiGraphicsExtractor g, Minecraft mc, String label, KeyMapping km, int kx, int ky, int w) {
		boolean down = km.isDown();
		Gfx.roundRect(g, kx, ky, w, SIZE, 3, down ? 0xE04F8CFF : Theme.HUD_BG);
		int color = Theme.TEXT;
		int tx = kx + (w - mc.font.width(label)) / 2;
		g.text(mc.font, label, tx, ky + (SIZE - 8) / 2, color, false);
	}
}
