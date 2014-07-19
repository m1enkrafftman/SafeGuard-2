package io.github.m1enkrafftman.safeguard2.checks.movement;

import org.bukkit.entity.Player;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.heuristics.DataConfiguration;
import io.github.m1enkrafftman.safeguard2.utils.MathHelper;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

public class SGCheckSneak extends SGCheck {
	
	private boolean lastTickSneaking = false;
	
	@Override
	public void check(float millisDiff, SGCheckTag checkTag, PlayerThread thread, boolean cooldown) {
		if(SGPermissions.hasPermission(thread.getPlayer(), PermissionNodes.MOVEMENT_SPEED)) return;
		if(thread.getPlayer().isInsideVehicle()) return;
		double penetration; //Ouch
		penetration = MathHelper.getHorizontalDistance(thread.getPlayer().getLocation(), 
				thread.getLastLocation());
		boolean publish = false;
		DataConfiguration data = SafeGuard2.getSafeGuard().getDataConfig();
		Player sgPlayer = thread.getPlayer();
		double multi = getSpeedMultiplier(thread);
		if(sgPlayer.isSneaking()) {
			if(penetration > data.getSneak()*multi && lastTickSneaking) {
				thread.addVL(checkTag, (10*(penetration-data.getSneak())) + 5);
				publish = true;
			}else {
				thread.lowerVL(checkTag);
			}
			this.lastTickSneaking = true;
		}else {
			this.lastTickSneaking = false;
		}

		if(cooldown == true) publish = false;
		if(publish == true) {
			this.publishCheck(checkTag, thread);
			thread.resetMove();
		}else {
			thread.setSafeLocation(sgPlayer.getLocation());
		}

	}

}
