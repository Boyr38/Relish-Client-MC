package dev.relish.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/** Armor pieces with remaining durability, top to bottom: helmet, chestplate, leggings, boots. */
public class ArmorModule extends Module {
	private static final EquipmentSlot[] SLOTS = {
			EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET };
	private static final int ROW = 18;

	public ArmorModule(int x, int y) {
		super("Armor", x, y);
	}

	@Override
	public int width(Minecraft mc) { return 20 + 32; }

	@Override
	public int height(Minecraft mc) { return ROW * SLOTS.length - 2; }

	@Override
	public void render(GuiGraphicsExtractor g, Minecraft mc) {
		if (mc.player == null) return;
		int cy = y;
		for (EquipmentSlot slot : SLOTS) {
			ItemStack stack = mc.player.getItemBySlot(slot);
			if (!stack.isEmpty()) {
				g.item(stack, x, cy);
				if (stack.isDamageableItem()) {
					int max = stack.getMaxDamage();
					int left = max - stack.getDamageValue();
					int pct = max <= 0 ? 100 : left * 100 / max;
					int color = pct > 50 ? 0xFF55FF55 : pct > 25 ? 0xFFFFFF55 : 0xFFFF5555;
					g.text(mc.font, Integer.toString(left), x + 20, cy + 4, color, true);
				}
			}
			cy += ROW;
		}
	}
}
