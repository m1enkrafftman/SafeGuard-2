package io.github.m1enkrafftman.SafeGuard2.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class OutputManager {
	
	private Plugin myPlugin;
	
	public OutputManager(Plugin plugin) {
		myPlugin = plugin;
	}
	
	//TODO Color, permissions, etc
	public synchronized void printDebug(String message) {
		for(Player p : myPlugin.getServer().getOnlinePlayers()) {
			p.sendMessage("[SafeGuard] " + message);
		}
	}
	
	public synchronized void log(String message) {
		myPlugin.getLogger().info(message);
	}

}
