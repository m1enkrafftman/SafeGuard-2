package io.github.m1enkrafftman.safeguard2.checks.movement;

import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.MathHelper;
import io.github.m1enkrafftman.safeguard2.utils.SGMovementUtil;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.entity.Player;

public class SGCheckWaterwalk extends SGCheck {
	
	private static final int IN_LIQUID_BUFFER = 7;
	
	private static final double LEGAL_DELTA = 0.35;
	private static final double LEGAL_DELTA_VERTICAL = 0.35;
	
	@Override
	public void check(float millisDif, SGCheckTag checkTag, PlayerThread thread) {
		Player sgPlayer = thread.getPlayer();
		if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.MOVEMENT_WATER)) return;
		boolean publish = false;
		double delta = MathHelper.getHorizontalDistance(thread.getPlayer().getLocation(), thread.getLastLocation());
		double vert = SGMovementUtil.getDistanceY(thread.getPlayer().getLocation(), thread.getLastLocation(), false);
		
		delta = thread.getPlayer().isSprinting() ? delta*0.75 : delta;
		
		if(inLiquid(sgPlayer)) {
			thread.addLiquidTick();
			if(thread.getLiquidTicks() > IN_LIQUID_BUFFER) {
				if(delta > LEGAL_DELTA) {
					thread.addVL(checkTag, (10*(delta - LEGAL_DELTA) + 5));
					publish = true;
				}
				if(vert > LEGAL_DELTA_VERTICAL) {
					thread.addVL(checkTag, (10*(vert - LEGAL_DELTA_VERTICAL) + 5));
					publish = true;
				}
			}
		}else if (onGround(sgPlayer)){
			thread.resetLiquidTicks();
			thread.lowerVL(checkTag);
		}
		
		if(publish == true) {
			this.publishCheck(checkTag, thread);
		}
		
	}

}
