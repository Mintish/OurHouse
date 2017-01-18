package io.github.mintish.ourhouse.spigoteventhandlers;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;

public class PlayerEventHandlers extends OurhouseEventHandler {

	public PlayerEventHandlers(GenericChunkStore chunkStore) {
		super(chunkStore);
	}

	@EventHandler
	public void onPlayerBucketEmptyEvent(PlayerBucketEmptyEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			Chunk c = event.getBlockClicked().getChunk();
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to empty buckets here.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerBucketFillEvent(PlayerBucketFillEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			Chunk c = event.getBlockClicked().getChunk();
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to fill buckets here.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerArmorStandManipulateEvent(PlayerArmorStandManipulateEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			Chunk c = event.getRightClicked().getLocation().getChunk();
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to manipulate armor stands here.");
			}
		}
	}
	
}
