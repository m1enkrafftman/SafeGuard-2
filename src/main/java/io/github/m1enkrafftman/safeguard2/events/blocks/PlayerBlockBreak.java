package io.github.m1enkrafftman.safeguard2.events.blocks;

import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.checks.blockbreak.SGCheckBreakReach;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBlockBreak implements Listener {
	
	private SGCheckBreakReach checkBreakReach;
	
	@EventHandler
	public void checkBlockBreak(BlockBreakEvent event) {
		checkBreakReach = new SGCheckBreakReach();
		checkBreakReach.check(SGCheckTag.BLOCKBREAK_REACH, event);
	}

}
