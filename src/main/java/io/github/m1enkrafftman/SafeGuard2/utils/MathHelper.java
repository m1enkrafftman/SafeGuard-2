package io.github.m1enkrafftman.SafeGuard2.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class MathHelper {
	
	// No need for Math.abs, Math.sqrt can't be negative
	public static double getDistance3D(Location one, Location two) {
		double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
		double ySqr = (two.getY() - one.getY()) * (two.getY() - one.getY());
		double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
		return Math.sqrt(xSqr + ySqr + zSqr);
	}
	
	public static double getHorizontalDistance(Location one, Location two) {
		double xSqr = (two.getX() - one.getX()) * (two.getX() - one.getX());
		double zSqr = (two.getZ() - one.getZ()) * (two.getZ() - one.getZ());
		return Math.sqrt(xSqr + zSqr);
	}
	
	public static double getVerticalDistance(Location one, Location two) {
		return Math.abs(one.getY() - two.getY());
	}
	
	public static double clamp180(double theta) {
		theta %= 360;
        if (theta >= 180.0D) theta -= 360.0D;
        if (theta < -180.0D) theta += 360.0D;
        return theta;
	}
	
	public static Vector subtractAngles(Vector one, Vector two) {
		double x = clamp180(one.getX() - two.getX());
		double y = clamp180(one.getY() - two.getY());
		double z = clamp180(one.getZ() - two.getZ());
		return new Vector(x, y, z);
	}
	
	public static Vector getRotation(Location one, Location two) {
		double dx = two.getX() - one.getX();
		double dy = two.getY() - one.getY();
		double dz = two.getZ() - one.getZ();
        double distanceXZ = Math.sqrt(dx * dx + dz * dz);
		float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(dy, distanceXZ) * 180.0D / Math.PI);
        return new Vector(yaw, pitch, 0);
	}
}
