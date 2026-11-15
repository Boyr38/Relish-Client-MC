package dev.relish;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.relish.modules.Module;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/** Saves each module's enabled state and HUD position to config/relishclient.json. */
public final class Config {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Type TYPE = new TypeToken<Map<String, Entry>>() {}.getType();

	public static class Entry {
		public boolean enabled = true;
		public int x;
		public int y;
	}

	private Config() {}

	private static Path file() {
		return FabricLoader.getInstance().getConfigDir().resolve("relishclient.json");
	}

	public static void load() {
		Path f = file();
		if (!Files.exists(f)) {
			save();
			return;
		}
		try {
			Map<String, Entry> map = GSON.fromJson(Files.readString(f), TYPE);
			if (map == null) return;
			for (Module m : ModuleManager.all()) {
				Entry e = map.get(m.name);
				if (e == null) continue;
				m.enabled = e.enabled;
				m.x = e.x;
				m.y = e.y;
			}
		} catch (IOException | RuntimeException ex) {
			System.err.println("[Relish] Could not read config, using defaults: " + ex);
		}
	}

	public static void save() {
		Map<String, Entry> map = new LinkedHashMap<>();
		for (Module m : ModuleManager.all()) {
			Entry e = new Entry();
			e.enabled = m.enabled;
			e.x = m.x;
			e.y = m.y;
			map.put(m.name, e);
		}
		try {
			Files.writeString(file(), GSON.toJson(map, TYPE));
		} catch (IOException ex) {
			System.err.println("[Relish] Could not save config: " + ex);
		}
	}
}
