package dev.relish;

import dev.relish.modules.Module;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

/**
 * Drag-to-move HUD editor. The HUD itself keeps rendering underneath, so this screen only
 * draws outlines on top and moves the modules.
 */
public class HudEditorScreen extends Screen {
	private final Screen parent;
	private Module dragging = null;
	private double offX;
	private double offY;

	public HudEditorScreen(Screen parent) {
		super(Component.literal("Edit HUD Layout"));
		this.parent = parent;
	}

	@Override
	public void onClose() {
		Config.save();
		this.minecraft.gui.setScreen(parent);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float delta) {
		super.extractRenderState(g, mouseX, mouseY, delta);
		String hint = "Drag modules to move them. Esc to finish.";
		g.text(this.minecraft.font, hint, this.width / 2 - this.minecraft.font.width(hint) / 2, 8, 0xFFFFFFFF, true);

		for (Module m : ModuleManager.all()) {
			if (!m.enabled || !m.isMovable()) continue;
			int w = m.width(this.minecraft);
			int h = m.height(this.minecraft);
			if (w <= 0 || h <= 0) continue;
			int color;
			if (m == dragging) color = 0xFFFFD700;
			else if (m.contains(this.minecraft, mouseX, mouseY)) color = 0xFFFFFFFF;
			else color = 0x80FFFFFF;
			g.outline(m.x - 1, m.y - 1, w + 2, h + 2, color);
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (super.mouseClicked(event, doubleClick)) return true;
		List<Module> modules = ModuleManager.all();
		for (int i = modules.size() - 1; i >= 0; i--) {
			Module m = modules.get(i);
			if (m.enabled && m.isMovable() && m.contains(this.minecraft, event.x(), event.y())) {
				dragging = m;
				offX = event.x() - m.x;
				offY = event.y() - m.y;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (dragging == null) return super.mouseDragged(event, dragX, dragY);
		int w = dragging.width(this.minecraft);
		int h = dragging.height(this.minecraft);
		dragging.x = (int) Math.max(0, Math.min(this.width - w, event.x() - offX));
		dragging.y = (int) Math.max(0, Math.min(this.height - h, event.y() - offY));
		return true;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (dragging != null) {
			dragging = null;
			Config.save();
			return true;
		}
		return super.mouseReleased(event);
	}
}
