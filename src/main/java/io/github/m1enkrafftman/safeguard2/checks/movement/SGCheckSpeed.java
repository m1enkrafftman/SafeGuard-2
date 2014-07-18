package io.github.m1enkrafftman.safeguard2.checks.movement;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.heuristics.DataConfiguration;
import io.github.m1enkrafftman.safeguard2.utils.MathHelper;
import io.github.m1enkrafftman.safeguard2.utils.SGBlockUtil;
import io.github.m1enkrafftman.safeguard2.utils.SGMovementUtil;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.entity.Player;

public class SGCheckSpeed extends SGCheck {

	/* 
	 * I am aware that this whole thing is just awful. But it works,
	 * so piss off.
	 */
	
	private static double biggestDelta = 0.0;
	
	@Override
	public void check(float millisDiff, SGCheckTag checkTag, PlayerThread thread, boolean cooldown) {
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
		DataConfiguration data = SafeGuard2.getSafeGuard().getDataConfig();
		boolean publish = false;
		double multi = 1.0;
		
		if(thread.isOnIce()) multi *= 1.325;
		
		boolean inWeb = SGBlockUtil.isInWeb(thread); 
		
		if(inWeb) multi *= 0.12;
		
		//TODO: Potions
		multi *= getSpeedAmplifier(thread.getPlayer());
		
		double deltaVL = 0.0;
		if(onGround(sgPlayer)) 
		{
			if(delta > biggestDelta) {
				biggestDelta = delta;
			}
			if(sgPlayer.isSprinting()) {
				if(delta > data.getSprint()*multi) {
					deltaVL = 10*(delta-(data.getSprint()*multi));
					thread.addVL(checkTag, 10*(delta-data.getSprint()));
					publish = true;
				}
			} else {
				if(delta > data.getWalk()*multi) {
					deltaVL = 10*(delta-(data.getWalk()*multi));
					thread.addVL(checkTag, 10*(delta-data.getWalk()));
					publish = true;
				}
			}
			if(cooldown == true) publish = false;
	
		}else {
			if(sgPlayer.isSprinting()) {
				if(delta > data.getSprint()*multi) {
					deltaVL = 10*(delta-(data.getSprint()*multi));
					thread.addVL(checkTag, 10*(delta-data.getSprint()));
					publish = true;
				}
			} else {
				if(delta > data.getWalk()*multi) {
					deltaVL = 10*(delta-(data.getWalk()*multi));
					thread.addVL(checkTag, 10*(delta-data.getWalk()));
					publish = true;
				}
			}
			if(cooldown == true) publish = false;
		}
		
		if(publish == true) {
			if(inWeb) thread.addVL(checkTag, 10);
			if(deltaVL > 1) {
				this.publishCheck(checkTag, thread);
				thread.resetMove();
			}

		} else {
			thread.lowerVL(checkTag);
			thread.setSafeLocation(sgPlayer.getLocation());
		}
	}

}
