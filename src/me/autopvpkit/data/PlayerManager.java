package me.autopvpkit.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.inventory.ItemStack;

public class PlayerManager {

	private Map<String, Set<ItemStack>> playerItems;
	
	public PlayerManager() {playerItems = new HashMap<>();}
	
	public Map<String, Set<ItemStack>> getPlayerItems() {
		return this.playerItems;
	}
	
	public Set<ItemStack> getPlayerItems(String playerName) {
		return this.playerItems.get(playerName);
	}
	
	public void setPlayerItems(String playerName, Set<ItemStack> itemStacks) {
		playerItems.put(playerName, itemStacks);
	}
	
}
