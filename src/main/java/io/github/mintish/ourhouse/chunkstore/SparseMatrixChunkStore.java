package io.github.mintish.ourhouse.chunkstore;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.block.Block;

import io.github.mintish.ourhouse.chunkstore.sparsematrix.Location;
import io.github.mintish.ourhouse.chunkstore.sparsematrix.SparseMatrix;

public class SparseMatrixChunkStore extends GenericChunkStore {
	
	private static final long serialVersionUID = 100L;
	
	public SparseMatrixChunkStore() {
		chunks = new SparseMatrix();
		friends = new HashMap<UUID, IFriendsList>();
	}
	
	private SparseMatrix chunks;
	private HashMap<UUID, IFriendsList> friends;
	
	@Override
	public Boolean canBuild(UUID playerId, int x, int z) {
		boolean canBuild = false;
		UUID owner = owner(x, z);
		if (owner != null) {
			boolean isFriend = getOwnerFriends(owner).isFriend(playerId);
			canBuild = isFriend || playerId.equals(owner);
		}
		else
			canBuild = true;
		return canBuild;
	}

	@Override
	public Boolean anyForbidden(UUID playerId, List<Block> blockList) {
		boolean containsForbidden = false;
		for(Block b : blockList)
			if (!owner(b.getChunk().getX(), b.getChunk().getZ()).equals(playerId)) {
				containsForbidden = true;
				break;
			}
		return containsForbidden;
	}

	@Override
	public Boolean isOwner(UUID playerId, int x, int z) {
		UUID owner = owner(x, z);
		boolean equal = false;
		if (owner != null)
			equal = owner.equals(playerId);
		return equal;
	}

	@Override
	public UUID owner(int x, int z) {
		OwnedChunk chunk = chunks.get(new Location(x, z));
		UUID owner = null;
		if (chunk != null)
			owner = chunk.getPlayerId();
		return owner;
	}

	@Override
	public Boolean claim(int x, int z, UUID playerId) {
		boolean claimed = false;
		if (owner(x, z) == null) {
			chunks.put(new Location(x, z), new OwnedChunk(x, z, playerId));
			claimed = true;
		}
		return claimed;
	}

	@Override
	public Boolean abandon(int x, int z, UUID playerId) {
		boolean removed = false;
		if (isOwner(playerId, x, z)) {
			chunks.remove(new Location(x, z));
			removed = true;
		}
		return removed;
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
