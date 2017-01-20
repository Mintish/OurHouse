package io.github.mintish.ourhouse.chunkstore.sparsematrix;

import java.io.Serializable;

public class Location implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 100L;

	public Location(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	private int x;
	private int z;
	
	public int getX() {
		return this.x;
	}
	
	public int getZ() {
		return this.z;
	}
	
	@Override 
	public int hashCode() {
		int hash = 17;
		hash += 27 * (this.x + 17);
		hash += 27 * (this.z + 17);
		return hash;
	}
	
	public boolean equals(Object other) {
		if (other == null)
			return false;
		
		boolean equal = false;
		if (other instanceof Location) {
			Location loc = (Location) other;
			equal = this.x == loc.x && this.z == loc.z; 
		}
		return equal;
	}
}
