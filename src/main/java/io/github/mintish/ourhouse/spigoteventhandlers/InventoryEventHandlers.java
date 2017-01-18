package io.github.mintish.ourhouse.spigoteventhandlers;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryOpenEvent;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;

public class InventoryEventHandlers extends OurhouseEventHandler {
	public InventoryEventHandlers(GenericChunkStore chunkStore) {
		super(chunkStore);
	}

	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent event) {
		HumanEntity he = event.getPlayer();
		if (he instanceof Player) {
			Player p = (Player) he;
			Chunk c = event.getInventory().getLocation().getChunk();
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to view block inventory here.");
			}
		}
	}
}
