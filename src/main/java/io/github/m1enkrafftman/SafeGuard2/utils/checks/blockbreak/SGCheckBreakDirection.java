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
		BlockBreakEvent event = (BlockBreakEvent)evt;
		Player sgPlayer = event.getPlayer();
		if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.BLOCK_DIRECTION)) return;
		
		double offset = 0.0D;
		
		Location blockLoc = event.getBlock().getLocation().add(0.5, 0.6, 0.5);
		Location playerLoc = sgPlayer.getLocation().add(0, sgPlayer.getEyeHeight(), 0);
		
		Vector playerRotation = new Vector(playerLoc.getYaw(), playerLoc.getPitch(), 0);
		Vector expectedRotation = MathHelper.getRotation(playerLoc, blockLoc);
		
		double deltaYaw = MathHelper.clamp180(playerRotation.getX() - expectedRotation.getX());
		double deltaPitch = MathHelper.clamp180(playerRotation.getY() - expectedRotation.getY());
		
		double horizontalDistance = MathHelper.getHorizontalDistance(playerLoc, blockLoc);
		double distance = MathHelper.getDistance3D(playerLoc, blockLoc);
		
		double offsetX = deltaYaw * horizontalDistance * distance;
		double offsetY = deltaPitch * Math.abs(blockLoc.getY() - playerLoc.getY()) * distance;
		
		offset += Math.abs(offsetX);
		offset += Math.abs(offsetY);
		
		if (offset > 100) {
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).addVL(checkTag, offset / 100);
			this.publishCheck(checkTag, SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer));
		} else {
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).lowerVL(checkTag);
		}
	}
	
}
