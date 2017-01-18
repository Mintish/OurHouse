package io.github.mintish.ourhouse.chunkstore;

import java.io.Serializable;
import java.util.UUID;

public class OwnedChunk implements Serializable {
	private static final long serialVersionUID = 100L;
	private int x;
	private int z;
	private UUID playerId;
	
	public OwnedChunk(int x, int z, UUID playerId) {
		this.x = x;
		this.z = z;
		this.playerId = playerId;
	}
	
	public Boolean test(int x, int z) {
		return this.x == x && this.z == z;
	}
	
	public Boolean isOwner(UUID playerId) {
		return this.playerId.equals(playerId);
	}
	
	public UUID getPlayerId() {
		return this.playerId;
	}
}
