package dev.relish;

import dev.relish.modules.*;
import java.util.ArrayList;
import java.util.List;

public final class ModuleManager {
	private static final List<Module> MODULES = new ArrayList<>();

	private ModuleManager() {}

	public static void init() {
		MODULES.clear();
		MODULES.add(new FpsModule(4, 4));
		MODULES.add(new CpsModule(4, 22));
		MODULES.add(new PingModule(4, 40));
		MODULES.add(new CoordsModule(4, 58));
		MODULES.add(new KeystrokesModule(4, 80));
		MODULES.add(new ArmorModule(4, 180));
		MODULES.add(new PotionModule(120, 4));
		MODULES.add(new ZoomModule());
	}

	public static List<Module> all() {
		return MODULES;
	}

	public static void resetLayout() {
		for (Module m : MODULES) m.resetPosition();
		Config.save();
	}

	@SuppressWarnings("unchecked")
	public static <T extends Module> T get(Class<T> type) {
		for (Module m : MODULES) {
			if (type.isInstance(m)) return (T) m;
		}
		throw new IllegalStateException("Module not registered: " + type);
	}
}
