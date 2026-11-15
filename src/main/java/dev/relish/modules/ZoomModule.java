package dev.relish.modules;

/** Toggle only: the zoom itself (hold C) is handled in RelishClient by temporarily lowering the FOV option. */
public class ZoomModule extends Module {
	public static final int ZOOM_FOV = 30;

	public ZoomModule() {
		super("Zoom", 0, 0);
	}

	@Override
	public boolean isMovable() { return false; }
}
