package io.github.m1enkrafftman.SafeGuard2.commands;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;

import org.bukkit.ChatColor;

public class SGCommandHelp extends SGCommand {

	public SGCommandHelp() {
		this.name = "help";
		this.argumentCount = 1;
		this.usage = new StringBuilder().append(ChatColor.GOLD).append("/safeguard help").toString();
		this.description = "Displays SafeGuard commands.";
	}
	
	@Override
	public boolean execute() {
		if (this.arguments.size() == 0)
		{
			StringBuilder sgHelpMessage = new StringBuilder()
				.append(ChatColor.YELLOW).append("--------- ").append(ChatColor.BLUE).append(ChatColor.RESET).append(" Help: Index (1/1)").append(ChatColor.YELLOW).append(" ------------\n")
				.append(ChatColor.GRAY).append("Use /safeguard help <page> to get more help.\n").append(ChatColor.RESET);

			for (SGCommand command : SafeGuard2.getSafeGuard().getCommandManager().sgCommands) {
				sgHelpMessage.append(command.getUsage()).append(": ").append(ChatColor.RESET).append(command.getDescription()).append("\n");
			}

			sendChatMessage(this.sender, sgHelpMessage.toString());

			return true;

		} else {

			return true;
		}
	}

}
