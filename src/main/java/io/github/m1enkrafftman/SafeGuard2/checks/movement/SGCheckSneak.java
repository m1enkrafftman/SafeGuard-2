package io.github.m1enkrafftman.SafeGuard2.checks.movement;

import org.bukkit.entity.Player;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;
import io.github.m1enkrafftman.SafeGuard2.checks.SGCheck;
import io.github.m1enkrafftman.SafeGuard2.checks.SGCheckTag;
import io.github.m1enkrafftman.SafeGuard2.core.PermissionNodes;
import io.github.m1enkrafftman.SafeGuard2.core.SGPermissions;
import io.github.m1enkrafftman.SafeGuard2.heuristics.DataConfiguration;
import io.github.m1enkrafftman.SafeGuard2.utils.MathHelper;
import io.github.m1enkrafftman.SafeGuard2.utils.player.PlayerThread;

public class SGCheckSneak extends SGCheck {
	
	private boolean lastTickSneaking = false;
	
	@Override
	public void check(float millisDiff, SGCheckTag checkTag, PlayerThread thread) {
		if(SGPermissions.hasPermission(thread.getPlayer(), PermissionNodes.MOVEMENT_SPEED)) return;
		double penetration; //Ouch
		penetration = MathHelper.getHorizontalDistance(thread.getPlayer().getLocation(), 
				thread.getLastLocation());
		boolean publish = false;
		DataConfiguration data = SafeGuard2.getSafeGuard().getDataConfig();
		Player sgPlayer = thread.getPlayer();
		if(onGround(sgPlayer)) 
		{
			if(sgPlayer.isSneaking()) {
				lastTickSneaking = true;
				if(penetration > data.getSneak() && lastTickSneaking) {
					thread.addVL(checkTag, 10*(penetration-data.getSneak()));
					publish = true;
				}else {
					thread.lowerVL(checkTag);
				}
			}
				
			if(publish == true) {
				this.publishCheck(checkTag, thread);
				thread.resetMove();
			}else {
				thread.setSafeLocation(sgPlayer.getLocation());
			}
		}
	}

}
