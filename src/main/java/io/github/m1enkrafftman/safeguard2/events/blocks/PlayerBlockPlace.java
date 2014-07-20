package io.github.m1enkrafftman.safeguard2.events.blocks;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.checks.blockplace.SGCheckPlaceReach;
import io.github.m1enkrafftman.safeguard2.checks.blockplace.SGCheckPlaceSpeed;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBlockPlace implements Listener {

	private SGCheckPlaceReach checkPlaceReach;
	private SGCheckPlaceSpeed checkPlaceSpeed;
	
	@EventHandler
	public void checkBlockBreak(BlockPlaceEvent event) {
		checkPlaceReach = new SGCheckPlaceReach();
		checkPlaceSpeed = new SGCheckPlaceSpeed();
		checkPlaceReach.check(SGCheckTag.BLOCKPLACE_REACH, event);
		checkPlaceSpeed.check(SGCheckTag.BLOCKPLACE_SPEED, event, 
				SafeGuard2.getSafeGuard().getPlayerMap().get(event.getPlayer()));
	}
	
}
