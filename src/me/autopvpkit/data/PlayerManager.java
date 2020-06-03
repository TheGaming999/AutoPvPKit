package me.autopvpkit.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.autopvpkit.AutoPvPKit;

public class PlayerManager {
    
	private Map<String, Set<ItemStack>> playerItems;
    private Map<String, SavedItemSlots> savedPlayerKits;
	
	private AutoPvPKit plugin;
	
	public PlayerManager(AutoPvPKit plugin) {playerItems = new HashMap<>(); this.setSavedPlayerKits(new LinkedHashMap<>()); this.plugin = plugin;}
	
	public Map<String, Set<ItemStack>> getPlayerItems() {
		return this.playerItems;
	}
	
	public Set<ItemStack> getPlayerItems(String playerName) {
		return this.playerItems.get(playerName);
	}
	
	public void setPlayerItems(String playerName, Set<ItemStack> itemStacks) {
		playerItems.put(playerName, itemStacks);
	}
	
	/**
	 * :(
	 */
	public void loadSavedPlayerKits() {
		plugin.getSavedKitsConfig().getConfigurationSection("players").getKeys(false).forEach(playerName -> {
			ConfigurationSection kitNames = plugin.getSavedKitsConfig().getConfigurationSection("players." + playerName);
			kitNames.getKeys(false).forEach(kitName -> {
				ConfigurationSection items = kitNames.getConfigurationSection(kitName);
				SavedItemSlots sis = new SavedItemSlots();
				items.getKeys(false).stream().filter(itemSlot -> itemSlot.length() < 4).forEach(itemSlot -> {
					ItemStack is = plugin.getKitsManager().getKit(kitName).getItems().get(Integer.valueOf(itemSlot));
					sis.getSavedItemSlots().put(Integer.valueOf(itemSlot), is);
				});
				savedPlayerKits.put(playerName + "#" +kitName, sis);
			});
		});
	}

	public void saveSavedPlayerKits() {
		savedPlayerKits.keySet().forEach(line -> {
			SavedItemSlots sis = savedPlayerKits.get(line);
			String kit = line.substring(line.lastIndexOf("#") + 1);
			String name = line.replace("#" + kit, "");
			sis.getSavedItemSlots().keySet().forEach(slot -> {
				String itemSlot = String.valueOf(slot);
				plugin.getSavedKitsConfig().set("players." + name + "." + kit + "." + itemSlot, sis.getItemStackFromSlot(slot));
			});
		});
		plugin.saveSavedKitsConfig();
	}
	
	public void registerSavedPlayerKit(final Player player) {
		final String name = player.getName();
		if(plugin.hasKit.contains(name)) {
			Kit kit = plugin.getPlayerLastSelectedKit(name);
			SavedItemSlots sis = new SavedItemSlots();
			for (int i = 0; i < player.getInventory().getContents().length; i++) {
				ItemStack is = player.getInventory().getItem(i);
				if(is != null && is.getType() != Material.AIR) {
					sis.getSavedItemSlots().put(i, is);
				}
			}
			this.getSavedPlayerKits().put(name + "#" + kit.getName(), sis);
		}
	}
	
	public Map<String, SavedItemSlots> getSavedPlayerKits() {
		return savedPlayerKits;
	}

	/**
	 * 
	 * @param name player's name
	 * @param kitName kit name
	 * @return saved item slots which contains the slot number and the item stack
	 */
	public SavedItemSlots getSavedPlayerKit(String name, String kitName) {
		return savedPlayerKits.get(name + "#" + kitName);
	}
	
	public void setSavedPlayerKits(Map<String, SavedItemSlots> savedPlayerKits) {
		this.savedPlayerKits = savedPlayerKits;
	}
	
}
