package io.github.m1enkrafftman.SafeGuard2.commands;

import org.bukkit.ChatColor;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;

public class SGCommandGetData extends SGCommand {

	public SGCommandGetData() {
		this.name = "data";
		this.argumentCount = 1;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard data").toString();
		this.description = "Prints gathered heuristic information to the console.";
	}
	
	@Override
	public boolean execute() {
		if (this.arguments.size() == 0) {
			SafeGuard2.getSafeGuard().getDataGatherer().printData();
			sendChatMessage(sender, "Data printed, please see console."); 
			return true;
		}else {
			return false;
		}
	}
	
}
