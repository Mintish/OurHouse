package io.github.mintish.ourhouse.chunkstore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.Block;

public class StupidChunkStore extends GenericChunkStore {
	
	//TODO: Needs a better chunk store implementation.
	//      Store chunks in a HashMap. If our hash function is suitable, then we shouldn't have many/any collisions.
	//		Think of two signed shorts to represent coords; or roughly 32K X 32K chunks. 
	
	private List<OwnedChunk> chunks;
	private HashMap<UUID, IFriendsList> friends;
	public StupidChunkStore() {
		chunks = new ArrayList<OwnedChunk>();
		friends = new HashMap<UUID, IFriendsList>();
	}
	
	private static final long serialVersionUID = 100L;
	
	@Override
	public Boolean canBuild(UUID playerId, int x, int z) {
		boolean found = false;
		boolean ownedByPlayer = false;
		boolean friendOfOwner = false;
		for (OwnedChunk oc : chunks) {
			if (oc.test(x, z)) {
				found = true;
				ownedByPlayer = oc.isOwner(playerId);
				IFriendsList ownerFriends = getOwnerFriends(oc.getPlayerId());
				if (ownerFriends != null)
					friendOfOwner = ownerFriends.isFriend(playerId);
				break;
			}
		}
		return !found || ownedByPlayer || friendOfOwner;
	}
	
	@Override
	public Boolean isOwner(UUID playerId, int x, int z) {
		boolean ownedByPlayer = false;
		for (OwnedChunk oc : chunks) {
			if (oc.test(x, z)) {
				ownedByPlayer = oc.isOwner(playerId);
				break;
			}
		}
		return ownedByPlayer;
	}
	
	public UUID owner(int x, int z) {
		UUID owner = null;
		for (OwnedChunk oc : chunks) {
			if (oc.test(x, z)) {
				owner = oc.getPlayerId();
				break;
			}
		}
		return owner;
	}
	
	@Override
	public Boolean anyForbidden(UUID playerId, List<Block> blockList) {
		Boolean containsForbidden = false;
		for (Block b : blockList)
			for (OwnedChunk c : chunks)
				if (c.test(b.getChunk().getX(), b.getChunk().getZ())) {
					containsForbidden = true;
				}
		return containsForbidden;
	}

	@Override
	public Boolean claim(int x, int z, UUID playerId) {
		OwnedChunk c = new OwnedChunk(x, z, playerId);
		Boolean added = false;
		Boolean alreadyOwned = false;
		for (OwnedChunk oc : chunks)
			if (oc.test(x, z)) {
				alreadyOwned = true;
				break;
			}
		if (!alreadyOwned) {
			chunks.add(c);
			added = true;
		}
		return added;
	}

	@Override
	public Boolean abandon(int x, int z, UUID playerId) {
		OwnedChunk c = null;
		for (OwnedChunk oc : chunks)
			if (oc.test(x, z) && oc.isOwner(playerId)) {
				c = oc;
				break;
			}
		Boolean chunkRemoved = false;
		if (c != null) {
			chunks.remove(c);
			chunkRemoved = true;
		}
		return chunkRemoved;
	}
	
	@Override
	public IFriendsList getOwnerFriends(UUID playerId) {
		IFriendsList playerFriends = null;
		if (friends == null)
			friends = new HashMap<UUID, IFriendsList>();
		if (friends.containsKey(playerId))
			playerFriends = friends.get(playerId);
		return playerFriends;
	}
	
	@Override
	public void setOwnerFriends(UUID playerId, IFriendsList friendsList) {
		if (friends == null)
			friends = new HashMap<UUID, IFriendsList>();
		friends.put(playerId, friendsList);
	}
}
