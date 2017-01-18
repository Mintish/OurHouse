package io.github.mintish.ourhouse.chunkstore;

import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;

public class OwnerFriends implements IFriendsList, Serializable {

	private static final long serialVersionUID = 100L;

	public OwnerFriends() {
		this.friends = new HashSet<UUID>(); 
	}
	
	private HashSet<UUID> friends;
	
	public Boolean isFriend(UUID playerId) {
		return friends.contains(playerId);
	}

	public void addFriend(UUID playerId) {
		if (!friends.contains(playerId))
			friends.add(playerId);
	}
	
	public void removeFriend(UUID playerId) {
		if (friends.contains(playerId))
			friends.remove(playerId);
	}
	
}
