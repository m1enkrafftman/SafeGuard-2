package io.github.m1enkrafftman.safeguard2;

import io.github.m1enkrafftman.safeguard2.commands.SGCommandManager;
import io.github.m1enkrafftman.safeguard2.core.DataConfiguration;
import io.github.m1enkrafftman.safeguard2.events.PlayerConnection;
import io.github.m1enkrafftman.safeguard2.events.PlayerTeleport;
import io.github.m1enkrafftman.safeguard2.events.blocks.PlayerBlockBreak;
import io.github.m1enkrafftman.safeguard2.events.blocks.PlayerBlockPlace;
import io.github.m1enkrafftman.safeguard2.events.combat.PlayerDamage;
import io.github.m1enkrafftman.safeguard2.heuristics.DataGatherer;
import io.github.m1enkrafftman.safeguard2.utils.FileManager;
import io.github.m1enkrafftman.safeguard2.utils.OutputManager;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class SafeGuard2 extends JavaPlugin {
	
	private Map<Player, PlayerThread> myThreadMap;
	private OutputManager myOutputManager;
	private static SafeGuard2 safeguard2;
	private SGCommandManager sgCommandManager;
	private DataConfiguration myDataConfig;
	private DataGatherer myGatherer;
	private FileManager myFileManager;
	
	public static boolean IS_TEST = false;
		
	@Override
	public void onEnable() {
		myOutputManager = new OutputManager(this);
		myThreadMap = Collections.synchronizedMap(new HashMap<Player, PlayerThread>());
		loadListeners();
		handleThreadLoading();
		myGatherer = new DataGatherer(this);
		myDataConfig = new DataConfiguration(this);
		safeguard2 = this;
		updateConfig();
		getCommand("safeguard").setExecutor(sgCommandManager = new SGCommandManager());
		myOutputManager.log("Version " + this.getDescription().getVersion() + " has successfully loaded.");
	}
	
	public void updateConfig() {
		myDataConfig.loadConfig();	
	}
	
	private void loadListeners() {
		myOutputManager.log("Loading listeners...");
		this.getServer().getPluginManager().registerEvents(new PlayerConnection(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerTeleport(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerDamage(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerBlockBreak(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerBlockPlace(), this);
	}
	
	private void handleThreadLoading() {
		myOutputManager.log("Loading threads...");
		myThreadMap.clear();
		for(Player p : this.getServer().getOnlinePlayers()) {
			PlayerThread toAdd = new PlayerThread(p);
			toAdd.setName("SGThread-"+p.getName());
			myThreadMap.put(p, toAdd);
		}
		myOutputManager.log("Starting threads...");
		for(PlayerThread playerThread : myThreadMap.values()) {
			playerThread.start();
		}
	}
	
	public void flush() {
		myOutputManager.log("Flushing...");
		stopAllThreads();
		this.handleThreadLoading();
		myOutputManager.log("Flushed.");
	}
	
	private void stopAllThreads() {
		for(PlayerThread t : myThreadMap.values()) {
			t.shutoff();
		}
	}
	
	@Override
	public void onDisable() {
		stopAllThreads();
		myThreadMap.clear();
		myOutputManager.log("Version " + this.getDescription().getVersion() + " has successfully unloaded.");
	}

	public static boolean isTracking(Player sgPlayer) {
		return safeguard2.getPlayerMap().containsKey(sgPlayer);
	}
	
	public OutputManager getOutput() {
		return this.myOutputManager;
	}
	
	public Map<Player, PlayerThread> getPlayerMap() {
		return this.myThreadMap;
	}
	
	public static synchronized SafeGuard2 getSafeGuard() {
		return safeguard2;
	}
	
	public DataGatherer getDataGatherer() {
		return myGatherer;
	}
	
	public DataConfiguration getDataConfig() {
		return myDataConfig;
	}
	
	public SGCommandManager getCommandManager() {
		return this.sgCommandManager;
	}
	
}
