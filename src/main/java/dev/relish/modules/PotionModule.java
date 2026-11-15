package dev.relish.modules;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.effect.MobEffectInstance;

/** Active potion effects with level and time left, longest first. */
public class PotionModule extends Module {
	private static final int ROW = 16;
	private static final int MIN_W = 70;

	public PotionModule(int x, int y) {
		super("Potions", x, y);
	}

	private List<String> lines(Minecraft mc) {
		List<String> out = new ArrayList<>();
		if (mc.player == null) return out;
		List<MobEffectInstance> effects = new ArrayList<>(mc.player.getActiveEffects());
		effects.sort((a, b) -> Integer.compare(b.getDuration(), a.getDuration()));
		for (MobEffectInstance e : effects) {
			String name = e.getEffect().value().getDisplayName().getString();
			int level = e.getAmplifier() + 1;
			String time;
			if (e.isInfiniteDuration()) {
				time = "inf";
			} else {
				int s = e.getDuration() / 20;
				time = String.format("%d:%02d", s / 60, s % 60);
			}
			out.add(name + " " + level + "  " + time);
		}
		return out;
	}

	@Override
	public int width(Minecraft mc) {
		int w = MIN_W;
		for (String s : lines(mc)) w = Math.max(w, mc.font.width(s) + 10);
		return w;
	}

	@Override
	public int height(Minecraft mc) {
		return Math.max(1, lines(mc).size()) * ROW - 2;
	}

	@Override
	public void render(GuiGraphicsExtractor g, Minecraft mc) {
		int cy = y;
		for (String s : lines(mc)) {
			box(g, mc, s, x, cy);
			cy += ROW;
		}
	}
}
