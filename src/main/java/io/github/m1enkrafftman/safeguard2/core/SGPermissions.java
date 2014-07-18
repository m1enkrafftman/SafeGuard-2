package io.github.m1enkrafftman.safeguard2.core;

import org.bukkit.entity.Player;

public class SGPermissions {

	public static boolean hasPermission(Player player, String permission) {
		return (player.hasPermission(permission) || player.isOp());
	}
	
}
