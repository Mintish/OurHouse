package io.github.mintish.ourhouse.spigoteventhandlers;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.entity.Animals;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;

public class EntityEventHandlers extends OurhouseEventHandler {

	public EntityEventHandlers(GenericChunkStore chunkStore) {
		super(chunkStore);
	}
	
	@EventHandler
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		Boolean forbiddenEntity = false;
		Entity damagee = event.getEntity();
		if (damagee instanceof ArmorStand 
				|| damagee instanceof Hanging 
				|| damagee instanceof Boat
				|| damagee instanceof Minecart 
				|| damagee instanceof Animals 
				|| damagee instanceof NPC)
			forbiddenEntity = true;
		
		if (!forbiddenEntity)
			return;
			
		Entity damager = event.getDamager();
		Chunk c = event.getEntity().getLocation().getChunk();
		if (damager instanceof Player) {			
			Player p = (Player) damager;
			if (!chunkStore.canBuild(p.getUniqueId(), c.getX(), c.getZ())) {
				event.setCancelled(true);
				p.sendMessage(ChatColor.RED + "You don't have permission to damage entities here.");
			}
		} else if (damager instanceof TNTPrimed) {
			if (chunkStore.owner(c.getX(), c.getZ()) != null)
				event.setCancelled(true);;
		}
	}
	
	@EventHandler
	public void onEntityExplodeEvent(EntityExplodeEvent event) {
		if (chunkStore.anyForbidden(null, event.blockList()))
			event.setCancelled(true);
	}
}
