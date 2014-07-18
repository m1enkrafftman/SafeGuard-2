package io.github.m1enkrafftman.safeguard2.checks.combat;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class SGCheckSelfHit extends SGCheck {

	@Override
	public void check(SGCheckTag checkTag, Event evt) {
		boolean publish = false;
		EntityDamageByEntityEvent event = (EntityDamageByEntityEvent)evt;
		if(event.getDamager() instanceof Player) {
			if(event.getEntity() instanceof Player) {
				Player damager = (Player)event.getDamager();
				Player sgPlayer = (Player)event.getEntity();
				if(SGPermissions.hasPermission(sgPlayer, PermissionNodes.COMBAT_SELFHIT)) return;
				if(!sgPlayer.equals(damager)) {
					SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).onTeleport();
				}else {
					publish = true;
					SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).addVL(checkTag, 250);
				}
			}
		}
		if(publish == true) {
			this.publishCheck(checkTag, SafeGuard2.getSafeGuard().getPlayerMap().get(event.getEntity()));
		}
	}

}
