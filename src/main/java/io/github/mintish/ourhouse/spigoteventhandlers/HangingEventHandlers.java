package io.github.mintish.ourhouse.spigoteventhandlers;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;

public final class HangingEventHandlers extends OurhouseEventHandler {
	
	public HangingEventHandlers(GenericChunkStore chunkStore) {
		super(chunkStore);
	}

	@EventHandler
	public void onHangingBreakEvent(HangingBreakEvent event) {
		Chunk c = event.getEntity().getLocation().getChunk();
		RemoveCause cause = event.getCause();
		if (chunkStore.owner(c.getX(), c.getZ()) != null 
				&& cause.equals(RemoveCause.EXPLOSION)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHangingBreakByEntityEvent(HangingBreakByEntityEvent event) {
		Entity remover = event.getRemover();
		Chunk c = event.getEntity().getLocation().getChunk();
		if (remover instanceof Player) {
			Player p = (Player) remover;
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to break entities here.");
			}
		}
	}
}
