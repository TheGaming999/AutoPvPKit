package me.autopvpkit.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class SavedItemSlots {
	
	private Map<Integer, ItemStack> savedItemSlots;
	
	
	public SavedItemSlots() {this.savedItemSlots = new HashMap<>();}
	
	public Map<Integer, ItemStack> getSavedItemSlots() {
		return this.savedItemSlots;
	}
	
	public ItemStack getItemStackFromSlot(Integer slot) {
        return savedItemSlots.get(slot);
	}
	
	
}
