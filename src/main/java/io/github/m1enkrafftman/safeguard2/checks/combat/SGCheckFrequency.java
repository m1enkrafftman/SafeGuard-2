package io.github.m1enkrafftman.safeguard2.checks.combat;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SGCheckFrequency extends SGCheck {
	
	private static final float FIGHT_MS_LIMIT = 100F;
	
	@Override
	public void check(SGCheckTag checkTag, Event event, PlayerThread thread) {
		EntityDamageByEntityEvent evt = (EntityDamageByEntityEvent)event;

		Player sgPlayer = thread.getPlayer();
		if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.COMBAT_SPEED)) return;
		boolean publish = false;

		if(!thread.getFightTimer().canCheckManual(FIGHT_MS_LIMIT)) {
			thread.addVL(checkTag, 5);
			publish = true;
		}

		if(publish == true) {
			this.publishCheck(checkTag, thread);
			evt.setCancelled(true);
		}else {
			thread.getFightTimer().updateLastTime();
			thread.lowerVL(checkTag);
		}
	}

}
