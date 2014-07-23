package io.github.m1enkrafftman.safeguard2.checks.movement;

import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.SGMovementUtil;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.entity.Player;

public class SGCheckVertical extends SGCheck {
	
	private static final int ON_LADDER_BUFFER = 7;
	private static final double LEGAL_LADDER_VERTICAL = 0.35;
	private static final double LEGAL_DELTA_VERTICAL = 1.325;
	
	@Override
	public void check(float millisDif, SGCheckTag tag, PlayerThread thread, boolean cooldown) {
		boolean publish = false;
		if(thread.getPlayer().isInsideVehicle()) return;
		if(isCreative(thread.getPlayer()) && isCreativeFlight(thread.getPlayer())) return;
		
		if(SGPermissions.hasPermission(thread.getPlayer(), PermissionNodes.MOVEMENT_VERTICAL)) return;
		Player sgPlayer = thread.getPlayer();
		double verticalMoveDelta = SGMovementUtil.getDistanceY(thread.getPlayer().getLocation(), thread.getLastLocation(), false);
		
		if(isOnLadder(sgPlayer)) {
			thread.addLadderTick();
			if(thread.getLadderTicks() > ON_LADDER_BUFFER) {
				if(verticalMoveDelta > LEGAL_LADDER_VERTICAL) {
					publish = true;
					thread.addVL(tag, (10*(verticalMoveDelta-LEGAL_LADDER_VERTICAL) + 5));
				}
			}
		}else {
			thread.resetLadderTicks();
			if(verticalMoveDelta >= LEGAL_DELTA_VERTICAL) {
				publish = true;
				thread.addVL(tag, (10*(verticalMoveDelta-LEGAL_DELTA_VERTICAL) + 5));
			}
		}
		if(cooldown == true) publish = false;
		if(publish == true) {
			this.publishCheck(tag, thread);
			thread.setLocation(thread.getSafeLocation());
		}else {
			thread.lowerVL(tag);
		}
	}

}
