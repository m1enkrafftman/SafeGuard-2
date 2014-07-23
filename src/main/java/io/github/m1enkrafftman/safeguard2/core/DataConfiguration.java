package io.github.m1enkrafftman.safeguard2.core;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;

import org.bukkit.configuration.file.FileConfiguration;

public class DataConfiguration {
	
	private SafeGuard2 myPlugin;
	
	private double walkDistance = 1.0;
	private double sprintDistance = 1.4;
	private double sneakDistance = 0.5;
	
	private boolean resetMove = false;
	
	private FileConfiguration myConfig;
	
	private static final String SNEAK_PATH = "safeguard.data.sneak";
	private static final String WALK_PATH = "safeguard.data.walk";
	private static final String SPRINT_PATH = "safeguard.data.sprint";
	
	private static final String RESET_PATH = "safeguard.checks.resetmove";
	
	public DataConfiguration(SafeGuard2 plugin) {
		myPlugin = plugin;
		myConfig = plugin.getConfig();
		loadConfig();
	}
	
	public void loadConfig() {
		if(!myConfig.contains(SNEAK_PATH)) {
			myConfig.set(SNEAK_PATH, sneakDistance);
		}else {
			this.setSneak(myConfig.getDouble(SNEAK_PATH));
		}
		
		if(!myConfig.contains(WALK_PATH)) {
			myConfig.set(WALK_PATH, walkDistance);
		}else {
			this.setWalk(myConfig.getDouble(WALK_PATH));
		}
		
		if(!myConfig.contains(SPRINT_PATH)) {
			myConfig.set(SPRINT_PATH, sprintDistance);
		}else {
			this.setSprint(myConfig.getDouble(SPRINT_PATH));
		}
		if(!myConfig.contains(RESET_PATH)) {
			myConfig.set(RESET_PATH, resetMove);
		}else {
			this.setReset(myConfig.getBoolean(RESET_PATH));
		}
		saveConfig();
	}
	
	public void saveConfig() {
		myPlugin.saveConfig();
	}
	
	public void reconfigure() {
		walkDistance = myPlugin.getDataGatherer().walk()*1.2;
		sneakDistance = myPlugin.getDataGatherer().sneak()*1.125;
		sprintDistance = myPlugin.getDataGatherer().sprint()*1.2;
		myConfig.set(SNEAK_PATH, sneakDistance);
		myConfig.set(WALK_PATH, walkDistance);
		myConfig.set(SPRINT_PATH, sprintDistance);
		myConfig.set(RESET_PATH, resetMove);
		saveConfig();
	}
	
	public void setWalk(double d) {
		walkDistance = d;
	}
	
	public void setSprint(double d) {
		sprintDistance = d;
	}
	
	public void setSneak(double d) {
		sneakDistance = d;
	}
	
	public double getWalk() {
		return walkDistance;
	}
	
	public double getSneak() {
		return sneakDistance;
	}
	
	public double getSprint() {
		return sprintDistance;
	}
	
	public void setReset(boolean b) {
		resetMove = b;
	}
	
	public boolean getReset() {
		return resetMove;
	}
	
}
