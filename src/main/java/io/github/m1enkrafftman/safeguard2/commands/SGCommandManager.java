package io.github.m1enkrafftman.safeguard2.commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SGCommandManager implements CommandExecutor {

	/** The commands of SafeGuard. */
	public ArrayList<SGCommand> sgCommands = new ArrayList<SGCommand>();

	/** Construct a new SGCommandManager instance. */
	public SGCommandManager() {
		loadCommands();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
	{

		if (cmd.getName().equalsIgnoreCase("safeguard")) {

			if (args.length == 0) { args = new String[]{"help"}; }

			for (SGCommand command : sgCommands) {
				if (command.getName().equalsIgnoreCase(args[0])) {
					return command.parse(sender, commandLabel, args);
				}
			}
		}

		return false;
	}

	/** Create all sub-command instances. */
	public void loadCommands()
	{
		this.sgCommands.add(new SGCommandGetData());
		this.sgCommands.add(new SGCommandHelp());
		this.sgCommands.add(new SGCommandInfo());
		this.sgCommands.add(new SGCommandReconfigure());
		this.sgCommands.add(new SGCommandFlush());
	}
}