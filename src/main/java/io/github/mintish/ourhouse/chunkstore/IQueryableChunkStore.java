package io.github.mintish.ourhouse.chunkstore;

import java.util.List;
import java.util.UUID;
import org.bukkit.block.Block;

public interface IQueryableChunkStore {
	Boolean canBuild(UUID playerId, int x, int z);
	
	Boolean anyForbidden(UUID playerId, List<Block> blockList);
	
	Boolean isOwner(UUID playerId, int x, int z);
	
	UUID owner(int x, int z);
}
