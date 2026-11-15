package dev.relish.modules;

import java.util.ArrayDeque;
import java.util.Deque;
import net.minecraft.client.Minecraft;

/** Clicks per second. Samples once per tick, so it tops out around 10 CPS; a mixin can lift that later. */
public class CpsModule extends LabelModule {
	private final Deque<Long> left = new ArrayDeque<>();
	private final Deque<Long> right = new ArrayDeque<>();
	private boolean wasLeft;
	private boolean wasRight;

	public CpsModule(int x, int y) {
		super("CPS", x, y);
	}

	@Override
	public void tick(Minecraft mc) {
		long now = System.currentTimeMillis();
		boolean l = mc.options.keyAttack.isDown();
		boolean r = mc.options.keyUse.isDown();
		if (l && !wasLeft) left.add(now);
		if (r && !wasRight) right.add(now);
		wasLeft = l;
		wasRight = r;
		prune(left, now);
		prune(right, now);
	}

	private static void prune(Deque<Long> q, long now) {
		while (!q.isEmpty() && now - q.peekFirst() > 1000) q.pollFirst();
	}

	@Override
	protected String text(Minecraft mc) {
		return left.size() + " | " + right.size() + " CPS";
	}
}
