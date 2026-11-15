package dev.relish.modules;

import net.minecraft.client.Minecraft;

public class FpsModule extends LabelModule {
	public FpsModule(int x, int y) {
		super("FPS", x, y);
	}

	@Override
	protected String text(Minecraft mc) {
		return mc.getFps() + " FPS";
	}
}
