package io.github.m1enkrafftman.safeguard2.checks.blockbreak;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.MathHelper;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

public class SGCheckBreakReach extends SGCheck {
	
	@Override
	public void check(SGCheckTag checkTag, Event evt) {
		if(!(evt instanceof BlockBreakEvent)) return;
		BlockBreakEvent event = (BlockBreakEvent)evt;
		boolean publish = false;
		Player sgPlayer = event.getPlayer();
		if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.BLOCK_REACH)) return;
		
		double down; //syndrome
		down = MathHelper.getDistance3D(event.getBlock().getLocation(), sgPlayer.getLocation());
		
		if(down > getReachDistance(sgPlayer)) {
			publish = true;
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).addVL(checkTag, 10*(down - getReachDistance(sgPlayer)));
		}
		if(publish == true) {
			this.publishCheck(checkTag, SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer));
		}else {
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).lowerVL(checkTag);
		}
	}

}
