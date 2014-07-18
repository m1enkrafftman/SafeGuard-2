package io.github.m1enkrafftman.safeguard2.events;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerTeleport implements Listener {
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(event.getCause() == TeleportCause.UNKNOWN) return;
		SafeGuard2.getSafeGuard().getPlayerMap()
			.get(event.getPlayer()).onTeleport();
	}
	
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent event) {
		SafeGuard2.getSafeGuard().getPlayerMap()
			.get(event.getPlayer()).onTeleport();
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		SafeGuard2.getSafeGuard().getPlayerMap()
			.get(event.getPlayer()).onTeleport();
	}

}
