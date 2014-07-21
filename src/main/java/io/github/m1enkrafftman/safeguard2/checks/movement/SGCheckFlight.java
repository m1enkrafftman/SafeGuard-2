package io.github.m1enkrafftman.safeguard2.checks.movement;

import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.SGMovementUtil;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

public class SGCheckFlight extends SGCheck {
	
	private double myLastYMove = 0.0;
	
	public static double FLIGHT_TICK_LIMIT = 6;
	
	@Override
	public void check(float millisDif, SGCheckTag tag, PlayerThread thread) {
		boolean publish = false;
		if(SGMovementUtil.isAboveStairs(thread.getPlayer()) || SGCheck.inLiquid(thread.getPlayer())
				|| SGCheck.isOnFence(thread.getPlayer()) || SGCheck.isOnLadder(thread.getPlayer()) 
				|| SGCheck.isOnSnow(thread.getPlayer())) return;
		if(thread.getPlayer().isInsideVehicle()) return;
		if(isCreative(thread.getPlayer()) && isCreativeFlight(thread.getPlayer())) return;
		
		if(SGPermissions.hasPermission(thread.getPlayer(), PermissionNodes.MOVEMENT_FLIGHT)) return;
		
		double verticalMoveDelta = SGMovementUtil.getDistanceY(thread.getLastLocation(), thread.getPlayer().getLocation(), false);
		//boolean onGround = (thread.getPlayer().isSneaking() ? onGroundSneak(thread.getPlayer()) : onGround(thread.getPlayer()));
		boolean onGround = onGroundSneak(thread.getPlayer());
		if(!onGround && verticalMoveDelta >= 0.0) {
			thread.addFlightTick();
			if(thread.getFlightTicks() > FLIGHT_TICK_LIMIT) {
				thread.addVL(tag, (verticalMoveDelta * 10) + 5);
				publish = true;
			}
		}else {
			thread.resetFlightTicks();
			thread.lowerVL(tag);
		}
		if((thread.getFlightTicks() < FLIGHT_TICK_LIMIT) && 
				SGMovementUtil.getFalling(thread.getPlayer()) 
				&& !onGround(thread.getPlayer())) {
			thread.lowerVL(tag);
			return;
		}
		
		this.myLastYMove = verticalMoveDelta;
		if(publish == true) {
			super.publishCheck(tag, thread);
		}
	}

}
