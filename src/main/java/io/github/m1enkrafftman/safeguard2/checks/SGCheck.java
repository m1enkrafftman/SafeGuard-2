package io.github.m1enkrafftman.safeguard2.checks;

import io.github.m1enkrafftman.safeguard2.SafeGuard2;
import io.github.m1enkrafftman.safeguard2.core.PermissionNodes;
import io.github.m1enkrafftman.safeguard2.core.SGPermissions;
import io.github.m1enkrafftman.safeguard2.utils.SGBlockUtil;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SGCheck {
	
	public SGCheck() {
		
	}
	
	public void check(float millisDif, SGCheckTag checkTag, PlayerThread thread){};
	public void check(float millisDif, SGCheckTag checkTag, PlayerThread thread, boolean cooldown){};
	public void check(SGCheckTag checkTag, Event event){};
	
	public void publishCheck(SGCheckTag tag, PlayerThread player) {
		if(player.getVLTruncated(tag) <= 0.99) return;
		for(Player p : SafeGuard2.getSafeGuard().getServer().getOnlinePlayers()) {
			boolean send = (SGPermissions.hasPermission(p, PermissionNodes.INFO_ALERTS));
			String output = "VL: " + ChatColor.DARK_RED + (int)(player.getVLTruncated(tag))
					+ ChatColor.GRAY
					+  " [" + ChatColor.AQUA + player.getPlayer().getName()
					+ ChatColor.GRAY + "] - [" + ChatColor.GREEN
					+ tag.toString() + ChatColor.GRAY +
					"]";
			if(send) {
				sendChatMessage(p, output);
			}
			SafeGuard2.getSafeGuard().getOutput().log(ChatColor.stripColor(output));
		}
	}
	
	public static void sendChatMessage(Player sender, String message)
	{
		for (String line : message.split("\n"))
		{
			String s = ChatColor.BLUE + "["
					+ ChatColor.BLUE + "SafeGuard" + ChatColor.BLUE + "] " 
					+ ChatColor.GRAY + line;
			sender.sendMessage(s);
		}
	}
	
	/** Returns the player reach distance. */
	public static double getReachDistance(Player sgPlayer) {
		return (sgPlayer.getGameMode() == GameMode.CREATIVE ? 7.5 : 6.5);
	}

	/** Returns the player speed amplifier. */
	public static float getSpeedAmplifier(Player sgPlayerHandle) {
		float toReturn = 1F;
		
		for (PotionEffect effect : sgPlayerHandle.getActivePotionEffects()) {
			if (effect.getType() == PotionEffectType.SPEED) {
				toReturn *= 1.0f + (0.2f * (effect.getAmplifier() + 1));
			}
			if (effect.getType() == PotionEffectType.SLOW) {
				toReturn *= 1.0f - (0.15f * (effect.getAmplifier() + 1));
			}
		}
		return toReturn;
	}

	/** Returns the player jump amplifier. */
	public static float getJumpAmplifier(Player sgPlayerHandle)	{
		return (sgPlayerHandle.hasPotionEffect(PotionEffectType.JUMP) ? 3.0F : 1.0F);
	}

	/** Returns whether the player is in creative flight. */
	public static boolean isCreativeFlight(Player sgPlayer) {
		return (sgPlayer.getGameMode() == GameMode.CREATIVE || sgPlayer.getAllowFlight());
	}

	/** Returns whether the player is in creative flight. */
	public static boolean isCreative(Player sgPlayer) {
		return (sgPlayer.getGameMode() == GameMode.CREATIVE);
	}

	/** Returns whether the player is sprinting. */
	public static boolean isSprinting(Player sgPlayer) {
		return sgPlayer.isSprinting();
	}

	/** Returns whether the player is dead. */
	public static boolean isDead(Player sgPlayer) {
		return (sgPlayer.getHealth() <= 0 || sgPlayer.isDead());
	}

	/** Returns whether the player is on ice. */
	public static boolean onIce(Player sgPlayer) {
		return (sgPlayer.getEyeLocation().subtract(0,1.85,0).getBlock().getType().equals(Material.ICE) ? true : false);
	}

	/** Returns whether the player is in liquid. */
	public static boolean inLiquid(Player sgPlayer) {
		return (sgPlayer.getEyeLocation().subtract(0,1.85,0).getBlock().isLiquid() ? true : false);
	}

	/** Returns whether the player is on the ground. */
	public static boolean onGround(Player sgPlayer) {
		Block under = (sgPlayer.getLocation().subtract(0, 0.20D, 0).getBlock());
		boolean airborne = under.isEmpty() && under.getRelative(BlockFace.NORTH).isEmpty()
				&& under.getRelative(BlockFace.NORTH_EAST).isEmpty() 
				&& under.getRelative(BlockFace.EAST).isEmpty()
				&& under.getRelative(BlockFace.SOUTH_EAST).isEmpty()
				&& under.getRelative(BlockFace.SOUTH).isEmpty()
				&& under.getRelative(BlockFace.SOUTH_WEST).isEmpty()
				&& under.getRelative(BlockFace.WEST).isEmpty()
				&& under.getRelative(BlockFace.NORTH_WEST).isEmpty();
		return ((airborne && !isAboveStairs(sgPlayer) && !isOnFence(sgPlayer)) ? false : true);
	}

	/** Returns whether the player is above stairs. */
	public static boolean isAboveStairs(Player sgPlayer){
		final Block block = sgPlayer.getLocation().getBlock();
		final Block altBlock = sgPlayer.getLocation().add(0,0.5,0).getBlock();
		return SGBlockUtil.isStair(block.getRelative(BlockFace.NORTH))
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
				|| SGBlockUtil.isStair(altBlock.getRelative(BlockFace.NORTH_EAST));
	}

	/** Returns whether the player is above stairs. */
	public static boolean isOnLadder(Player sgPlayer){
		//Because fuck you, I'm spiderman
		final Block block = sgPlayer.getLocation().getBlock();
		final Block altBlock = sgPlayer.getLocation().add(0,0.5,0).getBlock();
		return SGBlockUtil.isLadder(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isLadder(block.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isLadder(altBlock.getRelative(BlockFace.NORTH_EAST));
	}

	/** Returns whether the player is on a fence. */
	public static boolean isOnFence(Player sgPlayer){
		final Block block = sgPlayer.getLocation().subtract(0,1,0).getBlock();
		final Block blockOnJump = sgPlayer.getLocation().subtract(0,2,0).getBlock();
		return SGBlockUtil.isFence(block)
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isFence(block.getRelative(BlockFace.NORTH_EAST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isFence(blockOnJump.getRelative(BlockFace.NORTH_EAST));
	}

	/** Returns whether the player is on snow. */
	public static boolean isOnSnow(Player sgPlayer){
		final Block block = sgPlayer.getLocation().subtract(0,1,0).getBlock();
		return SGBlockUtil.isSnow(block)
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.EAST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.WEST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH_WEST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH_WEST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.SOUTH_EAST))
				|| SGBlockUtil.isSnow(block.getRelative(BlockFace.NORTH_EAST));
	}

}
