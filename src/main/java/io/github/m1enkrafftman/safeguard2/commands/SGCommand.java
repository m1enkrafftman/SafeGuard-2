package io.github.m1enkrafftman.safeguard2.commands;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class SGCommand
{

	/** The sender of this command. */
	protected CommandSender sender;

	/** The name of this command. */
	protected String name;

	/** The usage of this command. */
	protected String usage;

	/** The description used for help. */
	protected String description;

	/** The permission node needed to access this command. */
	protected String permission = PermissionNodes.INFO_DEFAULT;

	/** Whether the console can execute this command. */
	protected boolean consoleExecute = false;

	/** The number of arguments this command accepts. */
	protected int argumentCount;

	/** The passed arguments for this command. */
	protected ArrayList<String> arguments = new ArrayList<String>();

	/** The Method called to parse this command (Called by the SGCommandManager instance). */
	public boolean parse(CommandSender sender, String commandLabel, String args[]) {

		if (!consoleExecute && !(sender instanceof Player)) {
			SafeGuard2.getSafeGuard().getOutput().log("Error! Must be a player to execute this command.");
			return false;
		}

		if (!sender.hasPermission(permission))
		{ 
			sendChatMessage(sender, ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}

		this.sender = sender;

		this.arguments.clear();

		for (String argument : args) { this.arguments.add(argument); }

		if (this.arguments.contains(this.name)) { this.arguments.remove(this.name); }

		if (arguments.size() > this.argumentCount) {		
			this.sendUsage();
			return true;
		}

		return execute();
	}

	/** The Method called after parsing an incoming command. */
	public abstract boolean execute();

	/** Returns the name of this command. */
	public String getName() {
		return name;
	}

	/** Returns the usage of this command. */
	public String getUsage() {
		return usage;
	}

	/** Returns the description for this command used in Help. */
	public String getDescription() {
		return description;
	}

	/** Sends the usage of this command to the CommandSender. */
	public void sendUsage()
	{
		sendChatMessage(this.sender, "Usage: " + this.usage);
	}

	/** Sends a message to the CommandSender of a command. */
	public static void sendChatMessage(CommandSender sender, String message)
	{
		for (String line : message.split("\n"))
		{
			String s = ChatColor.BLUE + "[SafeGuard] " + ChatColor.GRAY + line;
			sender.sendMessage(s);
		}
	}
	
	/*
	 * Wary, all ye who enter this domain,
	 * as it is the bane of existence.
	 * Do not attempt the journey beyond these gates, 
	 * as you will only be met with sadness, sorrow,
	 * lack of mercy, lack of empathy, torturous struggles,
	 * and an infinite loop that will crash everything.
	 * You have been warned.
	 */
	private static int flee = 0;
	private static void keepOut() {
		ArrayList<Exception> list = new ArrayList<Exception>();
		list.add(new Exception("How the hell did you get gere? You dun fucked up, son."));
		
		into_hell:
			for(Exception up : list) {
				if(flee <= Integer.MAX_VALUE) {
					flee++;
					continue into_hell;
				}else {
					try {
						//How did you even get here...?
						throw up;
					} catch (Exception e) {
						//Seriously, go away, catch block
					}
				}
			}
		keepOut();
	}
}