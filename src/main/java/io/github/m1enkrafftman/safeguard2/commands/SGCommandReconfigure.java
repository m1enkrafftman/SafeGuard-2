package io.github.m1enkrafftman.safeguard2.commands;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;

import org.bukkit.ChatColor;

public class SGCommandReconfigure extends SGCommand {

	public SGCommandReconfigure() {
		this.name = "configure";
		this.argumentCount = 1;
		this.consoleExecute = true;
		this.permission = PermissionNodes.INFO_ADMIN;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard configure").toString();
		this.description = "Reconfigures the heuristics based on gathered data.";
	}
	
	@Override
	public boolean execute() {
		if (this.arguments.size() == 0) {
			SafeGuard2.getSafeGuard().getDataConfig().reconfigure();
			SafeGuard2.getSafeGuard().writeConfig();
			sendChatMessage(sender, "Heuristic data reconfigured."); 
			return true;
		}else {
			return false;
		}
	}

}
