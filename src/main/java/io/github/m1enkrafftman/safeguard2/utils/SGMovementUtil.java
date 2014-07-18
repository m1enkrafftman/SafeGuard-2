package io.github.m1enkrafftman.safeguard2.utils;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class SGMovementUtil {
	
	private static SafeGuard2 safeguard = SafeGuard2.getSafeGuard();

	/** Returns the distance between two location X axis coordinates. */
	public static double getDistanceX(Location to, Location from,
			boolean absolute) {
		return (absolute ? (Math.abs(to.getX() - from.getX()))
				: (to.getX() - from.getX()));
	}

	/** Returns the distance between two location Y axis coordinates. */
	public static double getDistanceY(Location to, Location from,
			boolean absolute) {
		return (absolute ? (Math.abs(to.getY() - from.getY()))
				: (to.getY() - from.getY()));
	}

	/** Returns the distance between two location Z axis coordinates. */
	public static double getDistanceZ(Location to, Location from,
			boolean absolute) {
		return (absolute ? (Math.abs(to.getZ() - from.getZ()))
				: (to.getZ() - from.getZ()));
	}

	/** Returns the horizontal distance between two location coordinate sets. */
	public static double getDistanceHorizontal(Location to, Location from) {
		return (Math.sqrt((getDistanceX(to, from, true)
				* getDistanceX(to, from, true) + getDistanceZ(to, from, true)
				* getDistanceZ(to, from, true))));
	}

	/** Returns the vertical distance between two location coordinate sets. */
	public static double getDistanceVertical(Location to, Location from) {
		return (Math.sqrt(getDistanceY(to, from, true)
				* getDistanceY(to, from, true)));
	}
	
	public static boolean getFalling(Player sgPlayer) {
		return getFalling(sgPlayer.getLocation(), SafeGuard2.getSafeGuard().getPlayerMap().get(sgPlayer).getLastLocation());
	}

	/** Returns TRUE if Location difference is negative. */
	public static boolean getFalling(Location to, Location from) {
		return (getDistanceY(to, from, false) < 0);
	}

	/**
	 * Checks to see if the player is above stairs.
	 * 
	 * @param block
	 * @return
	 */
	public static boolean isAboveStairs(final Player sgPlayer) {
		final Block block = sgPlayer.getLocation().getBlock();
		final Block altBlock = sgPlayer.getLocation().add(0, 0.5, 0).getBlock();
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

	public static String getDirection(double rot) {
        if (0 <= rot && rot < 22.5) {
                return "North";
        } else if (22.5 <= rot && rot < 67.5) {
                return "Northeast";
        } else if (67.5 <= rot && rot < 112.5) {
                return "East";
        } else if (112.5 <= rot && rot < 157.5) {
                return "Southeast";
        } else if (157.5 <= rot && rot < 202.5) {
                return "South";
        } else if (202.5 <= rot && rot < 247.5) {
                return "Southwest";
        } else if (247.5 <= rot && rot < 292.5) {
                return "West";
        } else if (292.5 <= rot && rot < 337.5) {
                return "Northwest";
        } else if (337.5 <= rot && rot < 360.0) {
                return "North";
        } else {
                return null;
        }
	}	
}
