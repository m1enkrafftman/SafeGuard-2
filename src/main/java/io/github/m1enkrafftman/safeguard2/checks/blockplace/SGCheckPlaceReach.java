package io.github.m1enkrafftman.safeguard2.checks.blockplace;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.MathHelper;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class SGCheckPlaceReach extends SGCheck {

	@Override
	public void check(SGCheckTag checkTag, Event evt) {
		if(!(evt instanceof BlockPlaceEvent)) return;
		BlockPlaceEvent event = (BlockPlaceEvent)evt;
		boolean publish = false;
		Player sgPlayer = event.getPlayer();
		if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.BLOCK_REACH)) return;
		
		double down; //with the sickness
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
