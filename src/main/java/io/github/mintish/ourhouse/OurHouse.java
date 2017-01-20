package io.github.mintish.ourhouse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.mintish.ourhouse.chunkstore.GenericChunkStore;
import io.github.mintish.ourhouse.chunkstore.SparseMatrixChunkStore;
import io.github.mintish.ourhouse.spigoteventhandlers.BlockEventHandlers;
import io.github.mintish.ourhouse.spigoteventhandlers.EntityEventHandlers;
import io.github.mintish.ourhouse.spigoteventhandlers.HangingEventHandlers;
import io.github.mintish.ourhouse.spigoteventhandlers.InventoryEventHandlers;
import io.github.mintish.ourhouse.spigoteventhandlers.PlayerEventHandlers;

public class OurHouse extends JavaPlugin {
	
	private GenericChunkStore chunkStore;
	
	@Override
	public void onEnable() {
		this.getLogger().info(">> Loading OurHouse <<");
		try {
			if (!getDataFolder().exists())
				getDataFolder().mkdirs();
			File configFile = new File(getDataFolder(), "config.yml");
			if (!configFile.exists()) {
				getLogger().info("Couldn't find config.yml; creating default config file.");
				saveDefaultConfig();
			}
		} catch (Exception e) { 
			this.getLogger().log(Level.SEVERE, e.getMessage(), e);	
		}
		
		try (FileInputStream inFile = new FileInputStream(new File(getDataFolder(), "OwnedChunks.store"))) {
	         try (ObjectInputStream in = new ObjectInputStream(inFile)) {
	        	 this.chunkStore = (GenericChunkStore) in.readObject();
	         }
		} catch (FileNotFoundException e) {
			this.getLogger().log(Level.WARNING, e.getMessage(), e);
		} catch (IOException e) {
			this.getLogger().log(Level.SEVERE, e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			this.getLogger().log(Level.SEVERE, e.getMessage(), e);
		} finally {
			if (this.chunkStore == null)
				this.chunkStore = new SparseMatrixChunkStore();
		}
		
		Integer chunkPrice = this.getConfig().getInt("chunk-price");
				
		this.getServer().getPluginManager().registerEvents(new HangingEventHandlers(chunkStore), this);
		this.getServer().getPluginManager().registerEvents(new BlockEventHandlers(chunkStore), this);
		this.getServer().getPluginManager().registerEvents(new InventoryEventHandlers(chunkStore), this);
		this.getServer().getPluginManager().registerEvents(new EntityEventHandlers(chunkStore), this);
		this.getServer().getPluginManager().registerEvents(new PlayerEventHandlers(chunkStore), this);
		this.getCommand("ourhouse").setExecutor(new CommandOurhouse(chunkStore, chunkPrice, this.getServer()));
		this.getLogger().info(">> OurHouse Enabled <<");
	}
	
	@Override
	public void onDisable() {
		try (FileOutputStream outFile = new FileOutputStream(new File(getDataFolder(), "OwnedChunks.store"))) {
	         try (ObjectOutputStream out = new ObjectOutputStream(outFile)) {
	        	 out.writeObject(chunkStore);
	         } 
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, e.getMessage(), e);
		}
		getLogger().info(">> OurHouse Disabled <<");
	}
}