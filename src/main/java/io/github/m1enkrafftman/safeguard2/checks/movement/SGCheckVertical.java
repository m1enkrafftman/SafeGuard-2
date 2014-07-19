package io.github.m1enkrafftman.safeguard2.checks.movement;

import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.utils.SGMovementUtil;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.entity.Player;

public class SGCheckVertical extends SGCheck {
	
	//Essentially double water speed
	private static final double LEGAL_DELTA_VERTICAL = 0.6;
	
	@Override
	public void check(float millisDif, SGCheckTag tag, PlayerThread thread) {
		boolean publish = false;
		
		if(isCreative(thread.getPlayer()) && isCreativeFlight(thread.getPlayer())) return;
		
		if(thread.getPlayer().hasPermission(PermissionNodes.MOVEMENT_VERTICAL)) return;
		Player sgPlayer = thread.getPlayer();
		double verticalMoveDelta = SGMovementUtil.getDistanceY(thread.getLastLocation(), thread.getPlayer().getLocation(), false);
		
		if(isOnLadder(sgPlayer)) {
			
			if(verticalMoveDelta > LEGAL_DELTA_VERTICAL) {
				publish = true;
				thread.addVL(tag, (10*(verticalMoveDelta-LEGAL_DELTA_VERTICAL) + 5));
			}
			
		}else {
			//TODO: Glide check here
		}
		
		if(publish == true) {
			super.publishCheck(tag, thread);
		}else {
			thread.lowerVL(tag);
		}
	}

}
