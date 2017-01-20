package io.github.mintish.ourhouse.spigoteventhandlers;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;

public class BlockEventHandlers extends OurhouseEventHandler  {
	
	public BlockEventHandlers(GenericChunkStore chunkStore) {
		super(chunkStore);
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			Chunk c = event.getBlock().getChunk();
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to break blocks here.");
			}
		}
	}
	
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		if (p != null) {
			Chunk c = event.getBlock().getChunk();
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to place blocks here.");
			}
		}
	}
	
	@EventHandler
	public void onBlockExplodeEvent(BlockExplodeEvent event) {
		if (chunkStore.anyForbidden(null, event.blockList()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockSpreadEvent(BlockSpreadEvent event) {
		Chunk c = event.getBlock().getChunk();
		if (event.getNewState().getType() == Material.FIRE)
			if (chunkStore.owner(c.getX(), c.getZ()) != null)
				event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBurnEvent(BlockBurnEvent event) {
		Chunk c = event.getBlock().getChunk();
		if (chunkStore.owner(c.getX(), c.getZ()) != null)
			event.setCancelled(true);
	}	
	
	@EventHandler
	public void onBlockIgniteEvent(BlockIgniteEvent event) {
		Chunk c = event.getBlock().getChunk();
		Player p = event.getPlayer();
		if (p != null)
			if (chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				return;
			} else {
				p.sendMessage(ChatColor.RED + "You don't have permission to ignite blocks here.");
			}
		
		
		if (chunkStore.owner(c.getX(), c.getZ()) != null)
			event.setCancelled(true);
	}
}
