package io.github.m1enkrafftman.safeguard2.utils.player;

import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.checks.movement.SGCheckFlight;
import io.github.m1enkrafftman.safeguard2.checks.movement.SGCheckInvalidMove;
import io.github.m1enkrafftman.safeguard2.checks.movement.SGCheckSneak;
import io.github.m1enkrafftman.safeguard2.checks.movement.SGCheckSpeed;
import io.github.m1enkrafftman.safeguard2.checks.movement.SGCheckVertical;
import io.github.m1enkrafftman.safeguard2.checks.movement.SGCheckWaterwalk;
import io.github.m1enkrafftman.safeguard2.utils.SGBlockUtil;
import io.github.m1enkrafftman.safeguard2.utils.Timer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class PlayerThread extends Thread {
	
	private Player myPlayer;
	private boolean myRun;
	private Location myLastLocation;
	private Location myLastSafeLocation;
	
	private Timer myTimer;
	private Timer myTeleportTimer;
	private Timer myFightTimer;
	private Timer myPlaceTimer;
	
	private Map<SGCheckTag, Double> myVlMap;
	
	private static final float CHECK_DELTA = 100F;
	private static final float TP_COOLDOWN = 500F;
	
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
	private int myLiquidTicks;
	private int myLadderTicks;
	
	private boolean myCooldown;
	
	private SGCheckSpeed checkMovementSpeed;
	private SGCheckFlight checkMovementFlight;
	private SGCheckSneak checkMovementSneak;
	private SGCheckInvalidMove checkMovementInvalid;
	private SGCheckWaterwalk checkWaterwalk;
	private SGCheckVertical checkMovementVertical;
	
	public PlayerThread(Player player) {
		myPlayer = player;
		myRun = true;
		myLastLocation = myPlayer.getLocation();
		myLastSafeLocation = myPlayer.getLocation();
		myTimer = new Timer();
		myTeleportTimer = new Timer();
		myFightTimer = new Timer();
		myPlaceTimer = new Timer();
		myVlMap = new HashMap<SGCheckTag, Double>();
		myFlightTicks = 0;
		myLiquidTicks = 0;
		myLadderTicks = 0;
		myCooldown = false;
		this.initChecks();
		this.populateVlMap();
	}
	
	private void initChecks() {
		checkMovementSpeed = new SGCheckSpeed();
		checkMovementFlight = new SGCheckFlight();
		checkMovementSneak = new SGCheckSneak();
		checkMovementInvalid = new SGCheckInvalidMove();
		checkWaterwalk = new SGCheckWaterwalk();
		checkMovementVertical = new SGCheckVertical();
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
	
	public void addLiquidTick() {
		this.myLiquidTicks++;
	}
	
	public void resetLiquidTicks() {
		this.myLiquidTicks = 0;
	}
	
	public int getLiquidTicks() {
		return this.myLiquidTicks;
	}
	
	public void addLadderTick() {
		this.myLadderTicks++;
	}
	
	public void resetLadderTicks() {
		this.myLadderTicks = 0;
	}
	
	public int getLadderTicks() {
		return this.myLadderTicks;
	}
	
	public void shutoff() {
		myRun = false;
	}
	
	public Timer getFightTimer() {
		return myFightTimer;
	}
	
	public Timer getPlaceTimer() {
		return myPlaceTimer;
	}
	
	@Override
	public void run() {
		while(myRun) {
			float diffMillis = myTimer.diffMillis();
			boolean tpCooldown = myTeleportTimer.canCheckManual(TP_COOLDOWN);
			myCooldown = tpCooldown ? false : true;
			if(myTimer.canCheck(CHECK_DELTA) && tpCooldown) {
				runChecks(diffMillis);
			}
		}
	}
	
	private void runChecks(float millisDif) {
		Location newLocation = myPlayer.getLocation();
		this.runMovementChecks(millisDif);
		myLastLocation = newLocation;
	}
	
	private void runMovementChecks(float diffMillis) {
		checkMovementSpeed.check(diffMillis, SGCheckTag.MOVEMENT_SPEED, this, myCooldown);
		checkMovementFlight.check(diffMillis, SGCheckTag.MOVEMENT_FLIGHT, this);
		checkMovementSneak.check(diffMillis, SGCheckTag.MOVEMENT_SNEAK, this, myCooldown);
		checkWaterwalk.check(diffMillis, SGCheckTag.MOVEMENT_WATER, this);
		checkMovementVertical.check(diffMillis, SGCheckTag.MOVEMENT_VERTICAL, this, myCooldown);
		checkMovementInvalid.check(diffMillis, SGCheckTag.MOVEMENT_INVALID, this);
	}
	
	public void addVL(SGCheckTag tag, double delta) {
		this.myVlMap.put(tag, myVlMap.get(tag) + delta < 0 ? 0 : myVlMap.get(tag) + delta);
	}
	
	public void lowerVL(SGCheckTag tag) {
		this.myVlMap.put(tag, myVlMap.get(tag) * 0.25);
	}
	
	public Location getLastLocation() {
		return (myTeleportTimer.canCheckManual(TP_COOLDOWN) ? myLastLocation : myPlayer.getLocation());
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
		myTeleportTimer.updateLastTime();
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
		//Checks the various blockfaces and retrives the relative block to check.
		final Block block = myPlayer.getLocation().getBlock();
		final Block altBlock = myPlayer.getLocation().add(0, 0.5, 0).getBlock();
		return (SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH_EAST)));
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

	/**
	 * Checks to see if the player is on a vine, or a ladder.
	 * @param player
	 * @return boolean
	 */
	public boolean isClimbing() {
		//Checks the various blockfaces and retrives the relative block to check.
		final Block block = myPlayer.getLocation().getBlock();
		return (SGBlockUtil.isClimbable(block) || SGBlockUtil.isClimbable(block.getRelative(BlockFace.NORTH)) || SGBlockUtil.isClimbable(block.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isClimbable(block.getRelative(BlockFace.EAST)) || SGBlockUtil.isClimbable(block.getRelative(BlockFace.WEST)) || SGBlockUtil.isClimbable(block.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isClimbable(block.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isClimbable(block.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isClimbable(block.getRelative(BlockFace.NORTH_EAST)));
	}


	/**
	 * Checks to see if the player is on a vine, or a ladder.
	 * @param player
	 * @return boolean
	 */
	public boolean isOnLily() {
		//Checks the various blockfaces and retrives the relative block to check.
		final Block block = myPlayer.getLocation().getBlock();
		//Checks on jump
		final Block blockLower = myPlayer.getLocation().subtract(0, 0.1, 0).add(0.5, 0, 0).getBlock();
		final Block blockLowest = myPlayer.getLocation().subtract(0, 0.2, 0).add(0.5, 0, 0).getBlock();
		//Returns if any
		return (SGBlockUtil.isLily(block) || SGBlockUtil.isLily(blockLower) || SGBlockUtil.isLily(blockLowest) || SGBlockUtil.isLily(block.getRelative(BlockFace.NORTH)) || SGBlockUtil.isLily(block.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isLily(block.getRelative(BlockFace.EAST)) || SGBlockUtil.isLily(block.getRelative(BlockFace.WEST)) || SGBlockUtil.isLily(block.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isLily(block.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isLily(block.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isLily(block.getRelative(BlockFace.NORTH_EAST)))
				|| SGBlockUtil.isLily(blockLower.getRelative(BlockFace.NORTH)) || SGBlockUtil.isLily(blockLower.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isLily(blockLower.getRelative(BlockFace.EAST)) || SGBlockUtil.isLily(blockLower.getRelative(BlockFace.WEST)) || SGBlockUtil.isLily(blockLower.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isLily(blockLower.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isLily(blockLower.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isLily(blockLower.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.NORTH)) || SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.EAST)) || SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.WEST)) || SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isLily(blockLowest.getRelative(BlockFace.NORTH_EAST));
	}


	/**
	 * Checks to see if the player is on snow.
	 * @param player
	 * @return boolean
	 */
	public boolean isOnSnow() {
		//Checks the various blockfaces and retrives the relative block to check.
		final Block block = myPlayer.getLocation().getBlock();
		//Checks on jump
		final Block blockLower = myPlayer.getLocation().subtract(0, 0.1, 0).add(0.5, 0, 0).getBlock();
		final Block blockLowest = myPlayer.getLocation().subtract(0, 0.2, 0).add(0.5, 0, 0).getBlock();
		//Returns if any
		return (SGBlockUtil.isSnow(block) || SGBlockUtil.isSnow(blockLower) || SGBlockUtil.isSnow(blockLowest) || SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH)) || SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isSnow(block.getRelative(BlockFace.EAST)) || SGBlockUtil.isSnow(block.getRelative(BlockFace.WEST)) || SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH_EAST)))
				|| SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.NORTH)) || SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.EAST)) || SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.WEST)) || SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isSnow(blockLower.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.NORTH)) || SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.EAST)) || SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.WEST)) || SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isSnow(blockLowest.getRelative(BlockFace.NORTH_EAST));
	}

	/**
	 * Checks to see if the player is on ice.
	 * @param player
	 * @return boolean
	 */
	public boolean isOnIce() {
		//Checks the various blockfaces and retrives the relative block to check.
		final Block block = myPlayer.getLocation().getBlock();
		//Checks on jump
		final Block blockLower = myPlayer.getLocation().subtract(0, 0.1, 0).add(0.5, 0, 0).getBlock();
		final Block blockLowest = myPlayer.getLocation().subtract(0, 0.2, 0).add(0.5, 0, 0).getBlock();
		//Returns if any
		return (SGBlockUtil.isIce(block) || SGBlockUtil.isIce(blockLower) || SGBlockUtil.isIce(blockLowest) || SGBlockUtil.isIce(block.getRelative(BlockFace.NORTH)) || SGBlockUtil.isIce(block.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isIce(block.getRelative(BlockFace.EAST)) || SGBlockUtil.isIce(block.getRelative(BlockFace.WEST)) || SGBlockUtil.isIce(block.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isIce(block.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isIce(block.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isIce(block.getRelative(BlockFace.NORTH_EAST)))
				|| SGBlockUtil.isIce(blockLower.getRelative(BlockFace.NORTH)) || SGBlockUtil.isIce(blockLower.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isIce(blockLower.getRelative(BlockFace.EAST)) || SGBlockUtil.isIce(blockLower.getRelative(BlockFace.WEST)) || SGBlockUtil.isIce(blockLower.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isIce(blockLower.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isIce(blockLower.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isIce(blockLower.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.NORTH)) || SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.SOUTH)) || SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.EAST)) || SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.WEST)) || SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.SOUTH_WEST)) || SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.NORTH_WEST))||  SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.SOUTH_EAST)) || SGBlockUtil.isIce(blockLowest.getRelative(BlockFace.NORTH_EAST));
	}

	public double getVL(SGCheckTag tag) {
		return (this.myVlMap.get(tag).doubleValue());
	}

	public void setSafeLocation(Location location) {
		this.myLastSafeLocation = location;		
	}

}
