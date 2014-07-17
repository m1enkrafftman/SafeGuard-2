package io.github.m1enkrafftman.SafeGuard2.utils.checks.movement;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;
import io.github.m1enkrafftman.SafeGuard2.core.PermissionNodes;
import io.github.m1enkrafftman.SafeGuard2.core.SGPermissions;
import io.github.m1enkrafftman.SafeGuard2.heuristics.DataConfiguration;
import io.github.m1enkrafftman.SafeGuard2.utils.MathHelper;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.SGCheck;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.SGCheckTag;
import io.github.m1enkrafftman.SafeGuard2.utils.player.PlayerThread;

import org.bukkit.entity.Player;

public class SGCheckSpeed extends SGCheck {

	private static double biggestDelta = 0.0;
	
	@Override
	public void check(float millisDiff, SGCheckTag checkTag, PlayerThread thread) {
		double delta = MathHelper.getHorizontalDistance(thread.getPlayer().getLocation(), 
				thread.getLastLocation());
		Player sgPlayer = thread.getPlayer();
		if(onGround(sgPlayer)) 
		{
			if(sgPlayer.isSprinting())
				SafeGuard2.getSafeGuard().getDataGatherer().addSprint(delta);
			else if(sgPlayer.isSneaking())
				SafeGuard2.getSafeGuard().getDataGatherer().addSneak(delta);
			else if(sgPlayer.isFlying())
				SafeGuard2.getSafeGuard().getDataGatherer().addFly(delta);
			else
				SafeGuard2.getSafeGuard().getDataGatherer().addWalk(delta);
		}
		if(SGPermissions.hasPermission(thread.getPlayer(), PermissionNodes.MOVEMENT_SPEED)) return;
		boolean publish = false;
		//TODO: Delta time set
		double multi = 1.0;
		//TODO: Web check
		
		if(thread.isOnIce()) multi *= 1.325;
		
		//TODO: Potions
		//multi *= getSpeedAmplifier(thread.getPlayer());
		
		double deltaVL = 0.0;
		DataConfiguration data = SafeGuard2.getSafeGuard().getDataConfig();
		if(onGround(sgPlayer)) 
		{
			if(delta > biggestDelta) {
				biggestDelta = delta;
			}
			if(sgPlayer.isSprinting()) {
				if(delta > data.getSprint()*multi) {
					deltaVL = 10*(delta-data.getSprint());
					thread.addVL(checkTag, 10*(delta-data.getSprint()));
					publish = true;
				}else {
					thread.addVL(checkTag, -10.0);
				}
			}
			else {
				if(delta > data.getWalk()*multi) {
					deltaVL = 10*(delta-data.getWalk());
					thread.addVL(checkTag, 10*(delta-data.getWalk()));
					publish = true;
				}else {
					thread.addVL(checkTag, -10.0);
				}
			}
			if(publish == true) {
				if(deltaVL > 1) {
					this.publishCheck(checkTag, thread);
					thread.resetMove();
				}
			}else {
				thread.lowerVL(checkTag);
				thread.setSafeLocation(sgPlayer.getLocation());
			}
		}
	}

}
