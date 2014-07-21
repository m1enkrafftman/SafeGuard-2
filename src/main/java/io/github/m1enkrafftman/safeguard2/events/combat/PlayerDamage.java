package io.github.m1enkrafftman.safeguard2.events.combat;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.checks.combat.SGCheckFightReach;
import io.github.m1enkrafftman.safeguard2.checks.combat.SGCheckFrequency;
import io.github.m1enkrafftman.safeguard2.checks.combat.SGCheckPostMortem;
import io.github.m1enkrafftman.safeguard2.checks.combat.SGCheckSelfHit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {

	private SGCheckSelfHit combatSelf;
	private SGCheckFrequency combatSpeed;
	private SGCheckPostMortem checkPostMortem;
	private SGCheckFightReach checkFightReach;
	
	@EventHandler
	public void onPlayerDamage(EntityDamageByEntityEvent event) {
		combatSelf = new SGCheckSelfHit();
		combatSpeed = new SGCheckFrequency();
		checkPostMortem = new SGCheckPostMortem();
		checkFightReach = new SGCheckFightReach();
		if(event.getDamager() instanceof Player) {
			Player sgPlayer = (Player)event.getDamager();
			combatSelf.check(SGCheckTag.COMBAT_SELFHIT, event);
			checkFightReach.check(SGCheckTag.COMBAT_REACH, event);
			checkPostMortem.check(SGCheckTag.COMBAT_POSTMORTEM, event);
			combatSpeed.check(SGCheckTag.COMBAT_SPEED, event, SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer));
		}
	}
	
	@EventHandler
	public void onPlayerTakeDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player sgPlayer = (Player)event.getEntity();
			SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).onTeleport();
		}
	}
	
}
