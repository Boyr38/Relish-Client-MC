package dev.relish.modules;

import net.minecraft.client.Minecraft;

public class CoordsModule extends LabelModule {
	public CoordsModule(int x, int y) {
		super("Coords", x, y);
	}

	@Override
	protected String text(Minecraft mc) {
		if (mc.player == null) return "XYZ: -";
		return String.format("XYZ: %.0f / %.0f / %.0f", mc.player.getX(), mc.player.getY(), mc.player.getZ());
	}
}
