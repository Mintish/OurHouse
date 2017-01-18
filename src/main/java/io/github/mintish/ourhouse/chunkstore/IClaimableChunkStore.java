package io.github.mintish.ourhouse.chunkstore;

import java.util.UUID;

public interface IClaimableChunkStore {
	Boolean claim(int x, int z, UUID playerId);
	
	Boolean abandon(int x, int z, UUID playerId);
}
