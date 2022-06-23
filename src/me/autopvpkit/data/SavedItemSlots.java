package me.autopvpkit.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class SavedItemSlots {

	private Map<String, Map<Integer, ItemStack>> savedItemSlots;

	public SavedItemSlots() {
		this.savedItemSlots = new HashMap<>();
	}

	public Map<String, Map<Integer, ItemStack>> getSavedItemSlots() {
		return this.savedItemSlots;
	}

	public ItemStack getItemStackFromSlot(String kitName, int slot) {
		return savedItemSlots.get(kitName).get(slot);
	}

	public Map<Integer, ItemStack> getKitSavedItemSlots(String kitName) {
		return savedItemSlots.get(kitName);
	}

}
