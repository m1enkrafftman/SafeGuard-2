package io.github.m1enkrafftman.SafeGuard2.utils.checks.blockbreak;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.util.Vector;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;
import io.github.m1enkrafftman.SafeGuard2.core.PermissionNodes;
import io.github.m1enkrafftman.SafeGuard2.core.SGPermissions;
import io.github.m1enkrafftman.SafeGuard2.utils.MathHelper;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.SGCheck;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.SGCheckTag;

public class SGCheckBreakDirection extends SGCheck {

	@Override
	public void check(SGCheckTag checkTag, Event evt) {
		if(!(evt instanceof BlockBreakEvent)) return;
		BlockBreakEvent event = (BlockBreakEvent) evt;
		
		Player sgPlayer = event.getPlayer();
		if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.BLOCK_DIRECTION)) return;
		
		Location blockLoc = event.getBlock().getLocation().add(0.5, 0.6, 0.5);
		Location playerLoc = sgPlayer.getLocation().add(0, sgPlayer.getEyeHeight(), 0);
		
		Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0);
		Vector expectedRotation = MathHelper.getRotation(playerLoc, blockLoc);
		Vector deltaRotation = MathHelper.subtractAngles(playerRotation, expectedRotation);
		
		double horizontalDistance = MathHelper.getHorizontalDistance(playerLoc, blockLoc);
		double verticalDistance = MathHelper.getVerticalDistance(playerLoc, blockLoc);
		double distance = MathHelper.getDistance3D(playerLoc, blockLoc);
		
		double offset = 0.0D;
		offset += Math.abs(deltaRotation.getX() * horizontalDistance * distance);
		offset += Math.abs(deltaRotation.getY() * verticalDistance * distance);
		offset /= 100D;
		
		if (offset > 1) {
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).addVL(checkTag, offset);
			this.publishCheck(checkTag, SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer));
		} else {
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).lowerVL(checkTag);
		}
	}
	
}
