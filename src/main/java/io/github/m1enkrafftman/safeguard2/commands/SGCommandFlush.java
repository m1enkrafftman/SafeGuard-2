package io.github.m1enkrafftman.safeguard2.commands;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;

import org.bukkit.ChatColor;

public class SGCommandFlush extends SGCommand {
	
	public SGCommandFlush() {
		this.name = "flush"; // Man, the quality of these command names are going... down the drain.
		this.argumentCount = 1;
		this.consoleExecute = true;
		this.permission = PermissionNodes.INFO_ADMIN;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard flush").toString();
		this.description = "Flushes and re-initializes player threads.";
	}
	
	@Override
	public boolean execute() {
		if (this.arguments.size() == 0) {
			SafeGuard2.getSafeGuard().flush();
			sendChatMessage(sender, "Player threads flushed and re-initialized."); 
			return true;
		}else {
			return false;
		}
	}

}
