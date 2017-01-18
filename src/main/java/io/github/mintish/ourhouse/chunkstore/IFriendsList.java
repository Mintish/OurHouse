package io.github.mintish.ourhouse.chunkstore;

import java.util.UUID;

public interface IFriendsList {
	Boolean isFriend(UUID playerId);

	void addFriend(UUID playerId);
	
	void removeFriend(UUID playerId);
}
