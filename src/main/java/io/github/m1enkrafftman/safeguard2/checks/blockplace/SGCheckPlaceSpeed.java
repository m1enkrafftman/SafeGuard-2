package io.github.m1enkrafftman.safeguard2.checks.blockplace;

import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

public class SGCheckPlaceSpeed extends SGCheck {
	
	private static final float PLACE_MS_LIMIT = 80F;

	@Override
	public void check(SGCheckTag checkTag, Event event, PlayerThread thread) {
		BlockPlaceEvent evt = (BlockPlaceEvent)event;

		Player sgPlayer = thread.getPlayer();
		if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.BLOCK_SPEED)) return;
		boolean publish = false;

		if(!thread.getPlaceTimer().canCheckManual(PLACE_MS_LIMIT)) {
			thread.addVL(checkTag, 5);
			publish = true;
		}

		if(publish == true) {
			this.publishCheck(checkTag, thread);
			evt.setCancelled(true);
		}else {
			thread.getPlaceTimer().updateLastTime();
			thread.lowerVL(checkTag);
		}
	}
	
}
