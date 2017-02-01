package io.github.mintish.ourhouse;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;
import io.github.mintish.ourhouse.chunkstore.IFriendsList;
import io.github.mintish.ourhouse.chunkstore.OwnerFriends;

public class CommandOurhouse implements CommandExecutor {

	public CommandOurhouse(GenericChunkStore chunkStore, Integer chunkPrice, Server server) {
		this.chunkStore = chunkStore;
		this.server = server;
		this.chunkPrice = chunkPrice;
	}
	
	private GenericChunkStore chunkStore;
	private Server server;
	private Integer chunkPrice;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Boolean handled = false;
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equals("ourhouse")) {
				if (args.length >= 1)
				{
					Chunk c = p.getLocation().getChunk();
					switch(args[0]) {
						case "claim":
							commandClaim(p, c);
							handled = true;
							break;
						case "adminRemove":
							adminRemove(p, c);
							handled = true;
							break;
						case "abandon":
							commandAbandon(p, c);
							handled = true;
							break;
						case "info":
							commandInfo(p, c);
							handled = true;
							break;
						case "addFriend":
							if (args.length < 2)
								break;
							commandAddFriend(args[1], p);
							handled = true;
							break;
						case "removeFriend":
							if (args.length < 2)
								break;
							commandRemoveFriend(args[1], p);
							handled = true;
							break;
						default:
							break;
					}
				}
			}
		}
		return handled; 
	}

	private void adminRemove(Player p, Chunk c) {
		if (p.isOp()) {
			UUID owner = chunkStore.owner(c.getX(), c.getZ());
			if (owner != null) {
				chunkStore.abandon(c.getX(), c.getZ(), owner);
				p.sendMessage(ChatColor.GREEN + "Chunk owner removed.");
			} else
				p.sendMessage(ChatColor.RED + "Couldn't find owner for chunk.");
			
		} else
			p.sendMessage(ChatColor.RED + "You don't have permission.");
	}

	private void commandRemoveFriend(String friendName, Player p) {
		IFriendsList friends = chunkStore.getOwnerFriends(p.getUniqueId());
		if (friends == null) {
			friends = new OwnerFriends();
			chunkStore.setOwnerFriends(p.getUniqueId(), friends);
		}
		@SuppressWarnings("deprecation") Player remfriend = server.getPlayer(friendName);
		if (remfriend != null) {
			friends.removeFriend(remfriend.getUniqueId());
			p.sendMessage(ChatColor.GREEN + "Removed " + friendName + " from friend list.");
		}
		else p.sendMessage(ChatColor.RED + "No player by that name.");
	}

	private void commandAddFriend(String friendName, Player p) {
		IFriendsList friends = chunkStore.getOwnerFriends(p.getUniqueId());
		if (friends == null) {
			friends = new OwnerFriends();
			chunkStore.setOwnerFriends(p.getUniqueId(), friends);
		}
		@SuppressWarnings("deprecation") Player friend = server.getPlayer(friendName);
		if (friend != null) {
			friends.addFriend(friend.getUniqueId());
			p.sendMessage(ChatColor.GREEN + "Added " + friendName + " to friend list.");
		}
		else p.sendMessage(ChatColor.RED + "No player by that name.");
	}

	private void commandInfo(Player p, Chunk c) {
		if (chunkStore.isOwner(p.getUniqueId(), c.getX(), c.getZ()))
			p.sendMessage(ChatColor.GREEN + "You own this chunk.");
		else
			p.sendMessage(ChatColor.GREEN + "You do not own this chunk.");
	}

	private void commandAbandon(Player p, Chunk c) {
		if (chunkStore.abandon(c.getX(), c.getZ(), p.getUniqueId()))
			p.sendMessage(ChatColor.GREEN + "Chunk abandoned!");
		else
			p.sendMessage(ChatColor.RED + "Cannot abandon this chunk; you don't own it.");
	}

	private void commandClaim(Player p, Chunk c) {
		if (chunkStore.owner(c.getX(), c.getZ()) == null) {
			if (buyChunk(p)) {
				if (chunkStore.claim(c.getX(), c.getZ(), p.getUniqueId()))
					p.sendMessage(ChatColor.GREEN + "Chunk claimed!");
			} else
				p.sendMessage(ChatColor.RED + "Cannot claim this chunk; you don't have enough emeralds! Chunk price is "
						+ chunkPrice.toString() + " emeralds.");
		} else
			p.sendMessage(ChatColor.RED + "Cannot claim this chunk; someone already owns it.");
	}
	
	private Boolean buyChunk(Player player) {
		Boolean bought = false;
		int amountTaken = tryTakeEmeralds(player, chunkPrice);
		if (amountTaken == chunkPrice)
			bought = true;
		else
			giveBackEmeralds(player, amountTaken);
		return bought;
	}
	
	private Integer tryTakeEmeralds(Player player, Integer emeralds) {
		HashMap<Integer, ItemStack> remainder = player.getInventory().removeItem(new ItemStack(Material.EMERALD, chunkPrice));
		int remainderAmount = 0;
		for (ItemStack itemStack : remainder.values())
			remainderAmount += itemStack.getAmount();
		int amountTaken = emeralds - remainderAmount;
		return amountTaken;
	}

	private void giveBackEmeralds(Player player, Integer emeralds) {
		player.getInventory().addItem(new ItemStack(Material.EMERALD, emeralds));
	}

}
