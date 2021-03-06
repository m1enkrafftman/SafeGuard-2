package io.github.m1enkrafftman.safeguard2.checks.movement;

import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.MathHelper;
import io.github.m1enkrafftman.safeguard2.utils.SGBlockUtil;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SGCheckInvalidMove extends SGCheck {
	
	/*
	 * I really hope you don't like going through open doors,
	 * because now you can't!
	 */
	
	@Override
	public void check(float millisDiff, SGCheckTag checkTag, PlayerThread thread) {
		if(SGPermissions.hasPermission(thread.getPlayer(), PermissionNodes.MOVEMENT_INVALID)) return;
		boolean publish = false;
		Player sgPlayer = thread.getPlayer();
		Location to = sgPlayer.getLocation();
		//double delta = MathHelper.getHorizontalDistance(to, thread.getLastLocation());
		
		/*
		if(!SGBlockUtil.isPassable(thread, to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getBlock().getTypeId())){
			if(!sgPlayer.getEyeLocation().getBlock().isEmpty() && !to.getBlock().isEmpty()) {
				publish = true;
				thread.addVL(checkTag, 10);
			}
		}else {
			thread.lowerVL(checkTag);
		}
		*/
		if(thread.getPlayer().isSprinting() && thread.getPlayer().isSneaking()) {
			publish = true;
			thread.addVL(checkTag, 5);
		}
		
		if(publish == true) {
			this.publishCheck(checkTag, thread);
			thread.setLocation(thread.getSafeLocation());
		}else {
			thread.lowerVL(checkTag);
		}
		if(publish == true) {
			this.publishCheck(checkTag, thread);
			thread.resetMove();
		}else {
			thread.setSafeLocation(sgPlayer.getLocation());
		}
		
	}
	
}
