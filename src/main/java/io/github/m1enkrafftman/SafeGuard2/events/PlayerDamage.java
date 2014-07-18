package io.github.m1enkrafftman.SafeGuard2.events;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;
import io.github.m1enkrafftman.SafeGuard2.checks.SGCheckTag;
import io.github.m1enkrafftman.SafeGuard2.checks.combat.SGCheckSelfHit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

	private SGCheckSelfHit combatSelf;
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		combatSelf = new SGCheckSelfHit();
		combatSelf.check(SGCheckTag.COMBAT_SELFHIT, event);
	}
	
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player sgPlayer = (Player)event.getEntity();
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).onTeleport();
		}
	}
	
}
