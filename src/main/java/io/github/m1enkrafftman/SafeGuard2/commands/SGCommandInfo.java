package io.github.m1enkrafftman.SafeGuard2.commands;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;
import io.github.m1enkrafftman.SafeGuard2.core.PermissionNodes;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.SGCheckTag;
import io.github.m1enkrafftman.SafeGuard2.utils.player.PlayerThread;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SGCommandInfo extends SGCommand {

	public SGCommandInfo() {
		this.name = "info";
		this.argumentCount = 1;
		this.permission = PermissionNodes.INFO_ADMIN;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard info <player>").toString();
		this.description = "Displays the current violations of a player.";
	}

	@Override
	public boolean execute() {

		if (this.arguments.size() == 0) { this.sendUsage(); return true; }

		StringBuilder sgInfoMessage = new StringBuilder()
			.append(ChatColor.YELLOW).append("--------- ").append(ChatColor.DARK_BLUE).append(ChatColor.RESET).append(" Violations").append(ChatColor.YELLOW).append(" -------------------\n")
			.append(ChatColor.GREEN).append(this.arguments.get(0)).append("\n").append(ChatColor.RESET);

		if (hasPlayer(this.arguments.get(0))) {

			for (SGCheckTag tag : SGCheckTag.values()) {
				if(getPlayer(this.arguments.get(0)).getVL(tag) > 0)
					sgInfoMessage.append(ChatColor.GRAY).append(tag.toString()).append(": ").append(ChatColor.DARK_RED).append(
							getPlayer(this.arguments.get(0)).getVL(tag)
							).append("\n");
			}

			sendChatMessage(this.sender, sgInfoMessage.toString());

		} else {
			sendChatMessage(this.sender, ChatColor.RED + "The player " + this.arguments.get(0) + " cannot be found!");
		}

		return true;
	}
	
	private PlayerThread getPlayer(String name) {
		PlayerThread out = null;
		for(PlayerThread p : SafeGuard2.getSafeGuard().getPlayerMap().values()) {
			if(p.getPlayer().getName().equals(name)) out = p;
		}
		return out;
	}
	
	private boolean hasPlayer(String name) {
		return (getPlayer(name) == null ? false : true);
	}
}