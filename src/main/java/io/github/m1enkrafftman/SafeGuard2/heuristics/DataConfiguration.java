package io.github.m1enkrafftman.SafeGuard2.heuristics;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;

public class DataConfiguration {
	
	private SafeGuard2 myPlugin;
	
	private double walkDistance = 0.52;
	private double sprintDistance = 0.63;
	private double sneakDistance = 0.32;
	
	public DataConfiguration(SafeGuard2 plugin) {
		myPlugin = plugin;
	}
	
	public void reconfigure() {
		walkDistance = myPlugin.getDataGatherer().walk();
		sneakDistance = myPlugin.getDataGatherer().sneak();
		sprintDistance = myPlugin.getDataGatherer().sprint();;
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
	
}
