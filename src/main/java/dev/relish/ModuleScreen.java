package dev.relish;

import dev.relish.modules.Module;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/** Lunar-style mod menu: sidebar on the left, a grid of toggle cards on the right. Right Shift by default. */
public class ModuleScreen extends Screen {
	private static final Identifier LOGO = Identifier.fromNamespaceAndPath(RelishClient.MOD_ID, "textures/gui/logo.png");
	private static final int SIDEBAR_W = 92;
	private static final int PAD = 8;
	private static final int HEADER = 34;

	private final Map<Module, Float> anim = new HashMap<>();
	private int px, py, pw, ph;

	public ModuleScreen() {
		super(Component.literal("Relish Client"));
	}

	private void layout() {
		pw = Math.min(this.width - 24, 440);
		ph = Math.min(this.height - 24, 270);
		px = (this.width - pw) / 2;
		py = (this.height - ph) / 2;
	}

	private int cols() {
		return (pw - SIDEBAR_W) >= 330 ? 3 : 2;
	}

	private int cardW() {
		int c = cols();
		return (pw - SIDEBAR_W - PAD * (c + 1)) / c;
	}

	private int cardH() {
		int c = cols();
		int n = ModuleManager.all().size();
		int rows = (n + c - 1) / c;
		int h = (ph - HEADER - PAD * (rows + 1)) / rows;
		return Math.max(24, Math.min(44, h));
	}

	private int cardX(int i) {
		return px + SIDEBAR_W + PAD + (i % cols()) * (cardW() + PAD);
	}

	private int cardY(int i) {
		return py + HEADER + PAD + (i / cols()) * (cardH() + PAD);
	}

	private int navY(int k) {
		return py + 44 + k * 22;
	}

	private static boolean in(double mx, double my, int x, int y, int w, int h) {
		return mx >= x && mx < x + w && my >= y && my < y + h;
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
		super.extractRenderState(g, mouseX, mouseY, delta);
		layout();
		var font = this.minecraft.font;

		// panel + sidebar
		Gfx.roundRect(g, px, py, pw, ph, 4, Theme.PANEL);
		Gfx.roundRect(g, px, py, SIDEBAR_W, ph, 4, Theme.SIDEBAR);
		g.fill(px + SIDEBAR_W - 4, py, px + SIDEBAR_W, py + ph, Theme.SIDEBAR);
		g.blit(RenderPipelines.GUI_TEXTURED, LOGO, px + 10, py + 9, 0, 0, 28, 28, 128, 128, 128, 128);
		g.text(font, "RELISH", px + 44, py + 13, Theme.ACCENT, true);
		g.text(font, "v2.1", px + 44, py + 25, Theme.TEXT_DIM, false);

		// sidebar nav
		String[] nav = {"Mods", "HUD Editor", "Reset Layout"};
		for (int k = 0; k < nav.length; k++) {
			int ny = navY(k);
			boolean hover = in(mouseX, mouseY, px + 8, ny, SIDEBAR_W - 16, 18);
			boolean active = k == 0;
			if (active) Gfx.roundRect(g, px + 8, ny, SIDEBAR_W - 16, 18, 3, 0xFF1F2A44);
			else if (hover) Gfx.roundRect(g, px + 8, ny, SIDEBAR_W - 16, 18, 3, 0xFF171726);
			g.text(font, nav[k], px + 14, ny + 5, active ? Theme.ACCENT : Theme.TEXT, false);
		}

		// header
		List<Module> modules = ModuleManager.all();
		int enabled = 0;
		for (Module m : modules) if (m.enabled) enabled++;
		g.text(font, "Mods", px + SIDEBAR_W + PAD, py + 13, Theme.TEXT, true);
		String count = enabled + "/" + modules.size() + " enabled";
		g.text(font, count, px + pw - PAD - font.width(count), py + 13, Theme.TEXT_DIM, false);

		// cards
		int cw = cardW();
		int ch = cardH();
		for (int i = 0; i < modules.size(); i++) {
			Module m = modules.get(i);
			int cx = cardX(i);
			int cy = cardY(i);
			boolean hover = in(mouseX, mouseY, cx, cy, cw, ch);

			float target = m.enabled ? 1f : 0f;
			float a = anim.getOrDefault(m, target);
			a += (target - a) * 0.3f;
			anim.put(m, a);

			Gfx.roundRect(g, cx, cy, cw, ch, 3, hover ? Theme.CARD_HOVER : Theme.CARD);
			if (m.enabled) g.fill(cx, cy + 5, cx + 2, cy + ch - 5, Theme.ACCENT);

			if (ch >= 30) {
				g.text(font, m.name, cx + 9, cy + ch / 2 - 9, Theme.TEXT, true);
				g.text(font, m.enabled ? "Enabled" : "Disabled", cx + 9, cy + ch / 2 + 2,
						m.enabled ? Theme.ACCENT : Theme.TEXT_DIM, false);
			} else {
				g.text(font, m.name, cx + 9, cy + (ch - 8) / 2, Theme.TEXT, true);
			}

			// toggle switch
			int tw = 22, th = 10;
			int tx = cx + cw - tw - 8;
			int ty = cy + (ch - th) / 2;
			Gfx.roundRect(g, tx, ty, tw, th, 4, Gfx.lerpColor(Theme.OFF_TRACK, Theme.ACCENT, a));
			int kx = tx + 1 + Math.round((tw - th) * a);
			Gfx.roundRect(g, kx, ty + 1, 8, 8, 4, 0xFFFFFFFF);
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		layout();
		double mx = event.x();
		double my = event.y();

		if (in(mx, my, px + 8, navY(1), SIDEBAR_W - 16, 18)) {
			this.minecraft.setScreen(new HudEditorScreen(this));
			return true;
		}
		if (in(mx, my, px + 8, navY(2), SIDEBAR_W - 16, 18)) {
			ModuleManager.resetLayout();
			return true;
		}

		List<Module> modules = ModuleManager.all();
		for (int i = 0; i < modules.size(); i++) {
			if (in(mx, my, cardX(i), cardY(i), cardW(), cardH())) {
				Module m = modules.get(i);
				m.enabled = !m.enabled;
				Config.save();
				return true;
			}
		}
		return super.mouseClicked(event, doubleClick);
	}
}
