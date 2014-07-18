package io.github.m1enkrafftman.SafeGuard2.checks.movement;

import io.github.m1enkrafftman.SafeGuard2.checks.SGCheck;
import io.github.m1enkrafftman.SafeGuard2.checks.SGCheckTag;
import io.github.m1enkrafftman.SafeGuard2.core.PermissionNodes;
import io.github.m1enkrafftman.SafeGuard2.utils.SGMovementUtil;
import io.github.m1enkrafftman.SafeGuard2.utils.player.PlayerThread;

public class SGCheckFlight extends SGCheck {
	
	private double myLastYMove = 0.0;
	
	//Magic, do not touch
	public static double FLIGHT_TICK_LIMIT = 5;
	
	@Override
	public void check(float millisDif, SGCheckTag tag, PlayerThread thread) {
		boolean publish = false;
		if(SGMovementUtil.isAboveStairs(thread.getPlayer()) || SGCheck.inLiquid(thread.getPlayer())
				|| SGCheck.isOnFence(thread.getPlayer()) || SGCheck.isOnLadder(thread.getPlayer()) 
				|| SGCheck.isOnSnow(thread.getPlayer())) return;
		
		if(isCreative(thread.getPlayer()) && isCreativeFlight(thread.getPlayer())) return;
		
		if(thread.getPlayer().hasPermission(PermissionNodes.MOVEMENT_FLIGHT) || thread.getPlayer().isOp()) return;
		
		double verticalMoveDelta = SGMovementUtil.getDistanceVertical(thread.getLastLocation(), thread.getPlayer().getLocation());
		
		if(!onGround(thread.getPlayer()) && this.myLastYMove >= 0.0) {
			thread.addFlightTick();
			if(thread.getFlightTicks() > FLIGHT_TICK_LIMIT) {
				thread.addVL(tag, (verticalMoveDelta * 10) + 5);
				publish = true;
			}
		}else {
			thread.resetFlightTicks();
			thread.lowerVL(tag);
		}
		if(SGMovementUtil.getFalling(thread.getPlayer()) && !onGround(thread.getPlayer())) {
			thread.lowerVL(tag);
			//TODO: glide check;
			return;
		}
		
		this.myLastYMove = verticalMoveDelta;
		if(publish == true) {
			super.publishCheck(tag, thread);
		}
	}

}
