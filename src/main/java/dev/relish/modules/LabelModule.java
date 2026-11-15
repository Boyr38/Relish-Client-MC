package dev.relish.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** A module that is just one line of text in a box. */
public abstract class LabelModule extends Module {
	protected LabelModule(String name, int x, int y) {
		super(name, x, y);
	}

	protected abstract String text(Minecraft mc);

	@Override
	public void render(GuiGraphicsExtractor g, Minecraft mc) {
		box(g, mc, text(mc), x, y);
	}

	@Override
	public int width(Minecraft mc) {
		return mc.font.width(text(mc)) + 10;
	}

	@Override
	public int height(Minecraft mc) {
		return 14;
	}
}
