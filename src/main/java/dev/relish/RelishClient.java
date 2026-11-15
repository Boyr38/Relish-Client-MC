package dev.relish;

import com.mojang.blaze3d.platform.InputConstants;
import dev.relish.modules.Module;
import dev.relish.modules.ZoomModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class RelishClient implements ClientModInitializer {
	public static final String MOD_ID = "relishclient";

	private static KeyMapping menuKey;
	private static KeyMapping zoomKey;
	private static Integer savedFov = null;

	@Override
	public void onInitializeClient() {
		ModuleManager.init();
		Config.load();

		KeyMapping.Category category = KeyMapping.Category.register(
				Identifier.fromNamespaceAndPath(MOD_ID, "main"));
		menuKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.relishclient.menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, category));
		zoomKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.relishclient.zoom", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_C, category));

		HudElementRegistry.attachElementBefore(
				VanillaHudElements.CHAT,
				Identifier.fromNamespaceAndPath(MOD_ID, "hud"),
				RelishClient::renderHud);

		ClientTickEvents.END_CLIENT_TICK.register(RelishClient::tick);
	}

	private static void renderHud(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.player == null) return;
		for (Module m : ModuleManager.all()) {
			if (m.enabled) m.render(graphics, mc);
		}
	}

	private static void tick(Minecraft mc) {
		while (menuKey.consumeClick()) {
			mc.gui.setScreen(new ModuleScreen());
		}
		if (mc.player == null) {
			savedFov = null;
			return;
		}
		for (Module m : ModuleManager.all()) {
			if (m.enabled) m.tick(mc);
		}
		handleZoom(mc);
	}

	private static void handleZoom(Minecraft mc) {
		boolean zoomEnabled = ModuleManager.get(ZoomModule.class).enabled;
		boolean zooming = zoomEnabled && zoomKey.isDown() && mc.gui.screen() == null;
		var fov = mc.options.fov();
		if (zooming && savedFov == null) {
			savedFov = fov.get();
			fov.set(ZoomModule.ZOOM_FOV);
		} else if (!zooming && savedFov != null) {
			fov.set(savedFov);
			savedFov = null;
		}
	}
}
