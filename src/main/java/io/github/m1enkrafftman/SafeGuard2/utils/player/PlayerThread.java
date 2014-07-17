package io.github.m1enkrafftman.SafeGuard2.utils.player;

import io.github.m1enkrafftman.SafeGuard2.utils.SGBlockUtil;
import io.github.m1enkrafftman.SafeGuard2.utils.Timer;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.SGCheckTag;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.movement.SGCheckFlight;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.movement.SGCheckInvalidMove;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.movement.SGCheckSneak;
import io.github.m1enkrafftman.SafeGuard2.utils.checks.movement.SGCheckSpeed;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class PlayerThread extends Thread {
	
	private Player myPlayer;
	private boolean myRun;
	private Location myLastLocation;
	private Location myLastSafeLocation;
	private Timer myTimer;
	
	private int myTeleportTicks;
	
	private Map<SGCheckTag, Double> myVlMap;
	
	private static final float CHECK_DELTA = 100F;
	
	private long lastOnIce = 0L;
	
	private double nextExpectedY = 0.0D;
	
	private long lastBlockHitTime = 0L;
	private long lastBlockBreakTime = 0L;
	private long lastBlockPlaceTime = 0L;
	
	private long lastAirTime = 0L;
	private long lastFlightStateTime = 0L;
	
	private int blocksBrokenFreq = 0;
	private long lastBlocksBrokenFreq = 0;
	
	private Location fellFrom = null;
	private Location fellTo = null;
	private boolean falling = false;
	
	private int initialHealth = 0;
	private int finalHealth = 0;
	
	private int myFlightTicks;
	
	private SGCheckSpeed checkMovementSpeed;
	private SGCheckFlight checkMovementFlight;
	private SGCheckSneak checkMovementSneak;
	private SGCheckInvalidMove checkMovementInvalid;
	
	public PlayerThread(Player player) {
		myTeleportTicks = 0;
		myPlayer = player;
		myRun = true;
		myLastLocation = myPlayer.getLocation();
		myLastSafeLocation = myPlayer.getLocation();
		myTimer = new Timer();
		myVlMap = new HashMap<SGCheckTag, Double>();
		myFlightTicks = 0;
		this.initChecks();
		this.populateVlMap();
	}
	
	private void initChecks() {
		checkMovementSpeed = new SGCheckSpeed();
		checkMovementFlight = new SGCheckFlight();
		checkMovementSneak = new SGCheckSneak();
		checkMovementInvalid = new SGCheckInvalidMove();
	}
	
	private void populateVlMap() {
		for(SGCheckTag tag : SGCheckTag.values()) {
			this.myVlMap.put(tag, 0.0);
		}
	}
	
	public void addFlightTick() {
		this.myFlightTicks++;
	}
	
	public void resetFlightTicks() {
		this.myFlightTicks = 0;
	}
	
	public int getFlightTicks() {
		return this.myFlightTicks;
	}
	
	public void shutoff() {
		myRun = false;
	}
	
	@Override
	public void run() {
		while(myRun) {
			if(myTeleportTicks < 3) myTeleportTicks++;
			float diffMillis = myTimer.diffMillis();
			if(myTimer.canCheck(CHECK_DELTA))
				runChecks(diffMillis);
		}
	}
	
	private void runChecks(float millisDif) {
		Location newLocation = myPlayer.getLocation();
		this.runMovementChecks(millisDif);
		myLastLocation = newLocation;
	}
	
	private void runMovementChecks(float diffMillis) {
		if(myTeleportTicks > 2) {
			checkMovementSpeed.check(diffMillis, SGCheckTag.MOVEMENT_SPEED, this);
			checkMovementFlight.check(diffMillis, SGCheckTag.MOVEMENT_FLIGHT, this);
			checkMovementSneak.check(diffMillis, SGCheckTag.MOVEMENT_SNEAK, this);
			//checkMovementInvalid.check(diffMillis, SGCheckTag.MOVEMENT_INVALID, this);
		}
	}
	
	public void addVL(SGCheckTag tag, double delta) {
		this.myVlMap.put(tag, myVlMap.get(tag) + delta < 0 ? 0 : myVlMap.get(tag) + delta);
	}
	
	public void lowerVL(SGCheckTag tag) {
		this.myVlMap.put(tag, myVlMap.get(tag) * 0.25);
	}
	
	public Location getLastLocation() {
		return myLastLocation;
	}
	
	public Player getPlayer() {
		return myPlayer;
	}
	
	public void resetMove() {
		this.onTeleport();
		myPlayer.teleport(myLastSafeLocation);
		myLastLocation = myLastSafeLocation;
	}
	
	public void onTeleport() {
		myTeleportTicks = 0;
		myLastLocation = myPlayer.getLocation();
		myLastSafeLocation = myPlayer.getLocation();
	}
	
	/** Sets the initial health upon falling */
	public void setFallInitialHealth(int health) {
		this.initialHealth = health;
	}

	/** Sets the final health after falling */
	public void setFallFinalHealth(int health) {
		this.finalHealth = health;
	}

	/** Sets the last time on ice */
	public void setLastTimeOnIce(long lastTimeOnIce) {
		this.lastOnIce = lastTimeOnIce;
	}

	/** Sets the last time the flight state was changed */
	public void setFlightStateTime(long lastFlightStateChangedTime) {
		this.lastFlightStateTime = lastFlightStateChangedTime;
	}

	public long getFlightStateTime() {
		return (this.lastFlightStateTime);
	}

	/** Returns the last time on ice */
	public long getLastTimeOnIce() {
		return (this.lastOnIce);
	}

	/** Gets the final health after falling */
	public int getFallFinalHealth() {
		return (this.finalHealth);
	}

	/** Gets the initial health upon falling */
	public int getFallInitialHealth() {
		return (this.initialHealth);
	}

	/** Sets the player to a state of falling based on the provided flag */
	public void setFalling(boolean isFalling) {
		this.falling = isFalling;
	}

	/** Is the player falling? */
	public boolean isFalling() {
		return (this.falling);
	}

	/** Resets all values related to falling */
	public void resetFallingValues() {
		this.falling = false;
	}

	/** Returns the current safe location of the player. */
	public Location getSafeLocation() {
		return (this.myLastSafeLocation);
	}

	/** Sets the location fell from */
	public void setFellFrom(Location fellFromLocation) {
		this.fellFrom = fellFromLocation;
	}

	/** Sets the location fell to */
	public void setFellTo(Location fellToLocation) {
		this.fellTo = fellToLocation;
	}

	/** Gets the location fell from */
	public Location getFellFrom() {
		if(fellFrom == null)return getPlayer().getLocation();
		return (this.fellFrom);
	}

	/** Gets the location the player fell from */
	public Location getFellTo() {
		if(fellTo == null)return getPlayer().getLocation();
		return (this.fellTo);
	}

	public void incrementBlocksFreq() {
		this.blocksBrokenFreq++;
	}

	public int getBlocksFreq() {
		return (this.blocksBrokenFreq);
	}
	public void resetBlocksFreq() {
		this.blocksBrokenFreq = 0;
	}

	public void setLastBlockBrokenFreq(long lastTime) {
		this.lastBlocksBrokenFreq = lastTime;
	}

	public long getLastBlockBrokenFreq() {
		return (this.lastBlocksBrokenFreq);
	}

	/** Returns the time in milliseconds the player last placed a block*/
	public long getLastPlaceTime() {
		return this.lastBlockPlaceTime;
	}

	public long getLastAirTime() {
		return (this.lastAirTime);
	}

	public void setLastAirTime(long time) {
		this.lastAirTime = time;
	}

	/** Sets the time in milliseconds the player last placed a block*/
	public void setLastPlaceTime(long lastBlockPlaceTime) {
		this.lastBlockPlaceTime = lastBlockPlaceTime;
	}

	/** Returns the last time this player hit a block. */
	public long getLastBlockHitTime() {
		return (this.lastBlockHitTime);
	}

	/** Sets the last time this player hit a block. */
	public void setLastBlockHitTime(long lastBlockHitTime) {
		this.lastBlockHitTime = lastBlockHitTime;
	}

	/** Returns the last time this player broke a block. */
	public long getLastBlockBreakTime() {
		return (this.lastBlockBreakTime);
	}

	/** Sets the last time this player broke a block. */
	public void setLastBlockBreakTime(long lastBlockBreakTime) {
		this.lastBlockBreakTime = lastBlockBreakTime;
	}

	/**
	 * Checks to see if the player is above stairs.
	 * @param block
	 * @return
	 */
	public boolean isAboveStairs(){
		final Function<Block, Boolean> func = (Block block) -> SGBlockUtil.isStair(block);
		//Checks the various blockfaces and retrives the relative block to check.
		final Block block = myPlayer.getLocation().getBlock();
		final Block altBlock = myPlayer.getLocation().add(0, 0.5, 0).getBlock();
		return isInMaterial(block, func) || isInMaterial(altBlock, func);
	}

	/** Returns the violation level of the player for the specified tag truncated to a max of two decimal places. */
	public double getVLTruncated(SGCheckTag tag) {
		return Math.round(this.myVlMap.get(tag).doubleValue() * Math.pow(10, (double) 2)) / Math.pow(10, (double)2);
	}

	/** Returns the next expected Y position of the player. */
	public double getNextExpectedY() {
		return (this.nextExpectedY);
	}

	/** Sets the next expected Y position of the player. */
	public void setNextExpectedY(double nextExpectedY) {
		this.nextExpectedY = nextExpectedY;
	}
	
	/** Returns true if any of the adjacent blocks matches a certain material. */
	private boolean isInMaterial(Block block, Function<Block, Boolean> op)
	{
		for (Block adjacent : SGBlockUtil.getAdjacentBlocks(block)) {
			if(op.apply(adjacent))
				return true;
		}
		return false;
	}
	
	/** Returns true if any of the adjacent blocks matches a certain material. */
	private boolean isOnMaterial(Function<Block, Boolean> op)
	{
		final Block block = myPlayer.getLocation().getBlock();
		final Block blockLower = myPlayer.getLocation().subtract(0, 0.1, 0).add(0.5, 0, 0).getBlock();
		final Block blockLowest = myPlayer.getLocation().subtract(0, 0.2, 0).add(0.5, 0, 0).getBlock();
		return isInMaterial(block, op) || isInMaterial(blockLower, op) || isInMaterial(blockLowest, op);
	}

	/**
	 * Checks to see if the player is on a vine, or a ladder.
	 * @param player
	 * @return boolean
	 */
	public boolean isClimbing() {
		final Function<Block, Boolean> func = (Block block) -> SGBlockUtil.isClimbable(block);
		return isInMaterial(myPlayer.getLocation().getBlock(), func);
	}


	/**
	 * Checks to see if the player is on a vine, or a ladder.
	 * @param player
	 * @return boolean
	 */
	public boolean isOnLily() {
		final Function<Block, Boolean> func = (Block block) -> SGBlockUtil.isLily(block);
		return isOnMaterial(func);
	}


	/**
	 * Checks to see if the player is on snow.
	 * @param player
	 * @return boolean
	 */
	public boolean isOnSnow() {
		final Function<Block, Boolean> func = (Block block) -> SGBlockUtil.isSnow(block);
		return isOnMaterial(func);
	}

	/**
	 * Checks to see if the player is on ice.
	 * @param player
	 * @return boolean
	 */
	public boolean isOnIce() {
		final Function<Block, Boolean> func = (Block block) -> SGBlockUtil.isIce(block);
		return isOnMaterial(func);
	}

	public double getVL(SGCheckTag tag) {
		return (this.myVlMap.get(tag).doubleValue());
	}

	public void setSafeLocation(Location location) {
		this.myLastSafeLocation = location;		
	}

}
