package dev.relish.modules;

import net.minecraft.client.Minecraft;

public class PingModule extends LabelModule {
	public PingModule(int x, int y) {
		super("Ping", x, y);
	}

	@Override
	protected String text(Minecraft mc) {
		int ping = 0;
		var conn = mc.getConnection();
		if (conn != null && mc.player != null) {
			var info = conn.getPlayerInfo(mc.player.getUUID());
			if (info != null) ping = info.getLatency();
		}
		return ping + " ms";
	}
}
