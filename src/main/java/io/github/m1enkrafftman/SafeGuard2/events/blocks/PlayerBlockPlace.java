package io.github.m1enkrafftman.SafeGuard2.events.blocks;

import io.github.m1enkrafftman.SafeGuard2.checks.SGCheckTag;
import io.github.m1enkrafftman.SafeGuard2.checks.blockplace.SGCheckPlaceReach;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockPlace implements Listener {

	private SGCheckPlaceReach checkPlaceReach;
	
	@EventHandler
	public void checkBlockBreak(BlockBreakEvent event) {
		checkPlaceReach = new SGCheckPlaceReach();
		checkPlaceReach.check(SGCheckTag.BLOCKPLACE_REACH, event);
	}
	
}
