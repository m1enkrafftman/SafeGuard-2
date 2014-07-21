package io.github.m1enkrafftman.safeguard2.events;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerConnection implements Listener {
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent event) {
		SafeGuard2.getSafeGuard().getPlayerMap().put(event.getPlayer(), new PlayerThread(event.getPlayer()));
		SafeGuard2.getSafeGuard().getPlayerMap().get(event.getPlayer()).start();
		SafeGuard2.getSafeGuard().getPlayerMap().get(event.getPlayer()).onTeleport();
		SafeGuard2.getSafeGuard().getOutput().log("Player " + event.getPlayer().getDisplayName() + " has logged in; thread added and started.");
	}
	
	@EventHandler
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		SafeGuard2.getSafeGuard().getPlayerMap().get(event.getPlayer()).shutoff();
		SafeGuard2.getSafeGuard().getPlayerMap().get(event.getPlayer()).stop();
		SafeGuard2.getSafeGuard().getPlayerMap().remove(event.getPlayer());
		SafeGuard2.getSafeGuard().getOutput().log("Player " + event.getPlayer().getDisplayName() + " has logged out; thread stopped and unloaded.");
	}

}
