package io.github.mintish.ourhouse.chunkstore;

import java.io.Serializable;

public abstract class GenericChunkStore implements IQueryableChunkStore, IClaimableChunkStore, IFriendableChunkStore, Serializable {

	private static final long serialVersionUID = 100L;
}
