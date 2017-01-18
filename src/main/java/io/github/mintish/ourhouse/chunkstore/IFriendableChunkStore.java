package io.github.mintish.ourhouse.chunkstore;

import java.util.UUID;

public interface IFriendableChunkStore {
	IFriendsList getOwnerFriends(UUID playerId);
	
	void setOwnerFriends(UUID playerId, IFriendsList friendsList);
}
