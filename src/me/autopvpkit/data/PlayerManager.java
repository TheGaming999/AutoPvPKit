package me.autopvpkit.data;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
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

	public PlayerManager(AutoPvPKit plugin) {
		playerItems = new HashMap<>();
		this.setSavedPlayerKits(new LinkedHashMap<>());
		this.plugin = plugin;
	}

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
			ConfigurationSection kitNames = plugin.getSavedKitsConfig()
					.getConfigurationSection("players." + playerName);
			SavedItemSlots sis = new SavedItemSlots();
			kitNames.getKeys(false).forEach(kitName -> {
				ConfigurationSection items = kitNames.getConfigurationSection(kitName);
				Map<Integer, ItemStack> savedItemSlotsMap = new HashMap<>();
				items.getKeys(false).stream().filter(itemSlot -> itemSlot.length() < 4).forEach(itemSlot -> {
					ItemStack is = items.getItemStack(itemSlot, new ItemStack(Material.AIR));
					savedItemSlotsMap.put(Integer.valueOf(itemSlot), is);
				});
				sis.getSavedItemSlots().put(kitName, savedItemSlotsMap);
			});
			savedPlayerKits.put(playerName, sis);
		});
	}

	public Entry<String, String> singleSplit(String string, String splitter) {
		return new AbstractMap.SimpleEntry<String, String>(string.substring(0, string.lastIndexOf(splitter) + 1),
				string.substring(string.lastIndexOf(splitter) + 1));
	}

	public void saveSavedPlayerKits() {
		savedPlayerKits.keySet().forEach(name -> {
			SavedItemSlots sis = savedPlayerKits.get(name);
			sis.getSavedItemSlots().keySet().forEach(kit -> {
				plugin.getSavedKitsConfig().set("players." + name + "." + kit, null);
				sis.getKitSavedItemSlots(kit).forEach((slot, itemStack) -> {
					plugin.getSavedKitsConfig().set("players." + name + "." + kit + "." + slot, itemStack);
				});
			});
		});
		plugin.saveSavedKitsConfig();
	}

	public void registerSavedPlayerKit(final Player player) {
		final String name = player.getName();
		if (plugin.hasKit.contains(name)) {
			Kit kit = plugin.getPlayerLastSelectedKit(name);
			Map<Integer, ItemStack> kitItems = kit.getItems();
			SavedItemSlots sis = getSavedPlayerKits().containsKey(name) ? getSavedPlayerKits().get(name)
					: new SavedItemSlots();
			Map<Integer, ItemStack> newSlots = new HashMap<>();
			for (int i = 0; i < player.getInventory().getContents().length; i++) {
				ItemStack is = player.getInventory().getItem(i);
				if (is != null && is.getType() != Material.AIR && kitItems.containsValue(is)) {
					newSlots.put(i, is);
				}
			}
			sis.getSavedItemSlots().put(kit.getName(), newSlots);
			getSavedPlayerKits().put(name, sis);
		}
	}

	public Map<String, SavedItemSlots> getSavedPlayerKits() {
		return savedPlayerKits;
	}

	/**
	 * 
	 * @param name    player's name
	 * @param kitName kit name
	 * @return saved item slots which contains the slot number and the item stack
	 */
	public Map<Integer, ItemStack> getSavedPlayerKit(String name, String kitName) {
		return savedPlayerKits.get(name).getKitSavedItemSlots(kitName);
	}

	public void removeSavedPlayerKit(String name, String kitName) {
		savedPlayerKits.get(name).getSavedItemSlots().remove(kitName);
	}

	/**
	 * 
	 * @param name    player name
	 * @param kitName
	 * @return
	 */
	public boolean hasSavedKit(String name, String kitName) {
		return hasSavedKit(name) && savedPlayerKits.get(name).getSavedItemSlots().containsKey(kitName);
	}

	/**
	 * 
	 * @param name player name
	 * @return
	 */
	public boolean hasSavedKit(String name) {
		return savedPlayerKits.containsKey(name);
	}

	public void setSavedPlayerKits(Map<String, SavedItemSlots> savedPlayerKits) {
		this.savedPlayerKits = savedPlayerKits;
	}

}
