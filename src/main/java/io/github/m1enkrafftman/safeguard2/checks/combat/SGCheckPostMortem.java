package io.github.m1enkrafftman.safeguard2.checks.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;

public class SGCheckPostMortem extends SGCheck {

	@Override
	public void check(SGCheckTag checkTag, Event evt) {
		boolean publish = false;
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)evt;
		if(event.getDamager() instanceof Player) {
			Player sgPlayer = (Player)event.getDamager();
			if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.COMBAT_POSTMORTEM)) return;
			if(sgPlayer.getHealth() <= 0) {
				publish = true;
				SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).addVL(checkTag, 25);
				event.setCancelled(true);
			}else {
				SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).lowerVL(checkTag);
			}
		}
		if(publish == true) {
			this.publishCheck(checkTag, SafeGuard2.getSafeGuard().getPlayerMap().get(event.getEntity()));
		}
	}
	
}
