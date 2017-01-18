package io.github.mintish.ourhouse.spigoteventhandlers;

import org.bukkit.event.Listener;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;

public abstract class OurhouseEventHandler implements Listener {
	public OurhouseEventHandler(GenericChunkStore chunkStore) {
		this.chunkStore = chunkStore;
	}
	
	protected GenericChunkStore chunkStore;
}
