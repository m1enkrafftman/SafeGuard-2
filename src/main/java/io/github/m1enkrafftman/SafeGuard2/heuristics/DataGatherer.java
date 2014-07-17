package io.github.m1enkrafftman.SafeGuard2.heuristics;

import io.github.m1enkrafftman.SafeGuard2.SafeGuard2;

public class DataGatherer {
	
	private double walk, sprint, sneak, fall, fly = 0.0;
	private int wCount, sCount, snCount, fCount, flCount = 0;
	private SafeGuard2 myPlugin;
	
	private double highWalk, highSneak, highSprint = 0;;
	
	public DataGatherer(SafeGuard2 plugin) {
		myPlugin = plugin;
	}

	public void addWalk(double delta) {
		if(delta > highWalk) highWalk = delta;
	}
	
	public void addSprint(double delta) {
		if(delta > highSprint) highSprint = delta;
	}
	
	public void addSneak(double delta) {
		if(delta > highSneak) highSneak = delta;
	}
	
	public void addFall(double delta) {
		fall += delta;
		fCount++;
	}
	
	public void addFly(double delta) {
		fly += delta;
		flCount++;
	}
	
	public double walk() {
		return highWalk;
	}
	
	public double sneak() {
		return highSneak;
	}
	
	public double sprint() {
		return highSprint;
	}
	
	public void printData() {
		myPlugin.getOutput().log("Walk Avg: " + walk());
		myPlugin.getOutput().log("Sprint Avg: " + sprint());
		myPlugin.getOutput().log("Sneak Avg: " + sneak());
		//myPlugin.getOutput().log("Fall Avg: " + (fall/fCount));
		//myPlugin.getOutput().log("Fly Avg: " + (fly/flCount));
	}
	
}
