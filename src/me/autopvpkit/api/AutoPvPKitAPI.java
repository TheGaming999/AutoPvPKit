package me.autopvpkit.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.data.Kit;
import me.autopvpkit.data.KitsManager;
import me.autopvpkit.data.SavedItemSlots;

public class AutoPvPKitAPI {

	private AutoPvPKit apk;
	
	public AutoPvPKitAPI() {apk = AutoPvPKit.getInstance();}
	
	/**
	 * 
	 * @param player
	 * @param name kit name
	 */
	public Kit setKit(final Player player, String name) {
		KitsManager km = apk.getKitsManager();
		Kit kit = km.getKit(apk.getAutoPvPKitCommand().matchKit(name));
		final PlayerInventory pinv = player.getInventory();
		ItemStack helmet = kit.getHelmet();
		if(helmet != null) {
			pinv.setHelmet(helmet);
		}
		ItemStack chestplate = kit.getChestplate();
		if(chestplate != null) {
			pinv.setChestplate(chestplate);
		}
		ItemStack leggings = kit.getLeggings();
		if(leggings != null) {
			pinv.setLeggings(leggings);
		}
		ItemStack boots = kit.getBoots();
		if(boots != null) {
			pinv.setBoots(boots);
		}
		kit.getItems().entrySet().forEach(entry -> {
			pinv.setItem(entry.getKey(), entry.getValue());
		});
		String playerName = player.getName();
		apk.getLastSelectedKits().put(playerName, kit);
		apk.hasKit.add(playerName);
		return kit;
	}
	
	/**
	 * 
	 * @param player
	 * @param name kit name
	 * @param clearInventory clears player inventory
	 * @description
	 * saved kits will not be ignored here.
	 */
	public Kit setKit(final Player player, String name, final boolean clearInventory) {
		KitsManager km = apk.getKitsManager();
		Kit kit = km.getKit(apk.getAutoPvPKitCommand().matchKit(name));
		final PlayerInventory pinv = player.getInventory();
		String playerName = player.getName();
		String kitName = kit.getName();
		if(clearInventory) {
		pinv.clear();
		}
		ItemStack helmet = kit.getHelmet();
		if(helmet != null) {
			pinv.setHelmet(helmet);
		}
		ItemStack chestplate = kit.getChestplate();
		if(chestplate != null) {
			pinv.setChestplate(chestplate);
		}
		ItemStack leggings = kit.getLeggings();
		if(leggings != null) {
			pinv.setLeggings(leggings);
		}
		ItemStack boots = kit.getBoots();
		if(boots != null) {
			pinv.setBoots(boots);
		}
	    if(apk.getPlayerManager().hasSavedKit(playerName, kitName)) {
	    	SavedItemSlots sis = apk.getPlayerManager().getSavedPlayerKit(playerName, kitName);
	    	sis.getSavedItemSlots().entrySet().forEach(entry -> {
	    		pinv.setItem(entry.getKey(), entry.getValue());
	    	});
	    } else {
		kit.getItems().entrySet().forEach(entry -> {
			pinv.setItem(entry.getKey(), entry.getValue());
		});
	    }
		apk.getLastSelectedKits().put(playerName, kit);
		apk.hasKit.add(playerName);
		return kit;
	}
	
	/**
	 * 
	 * @param player
	 * set kit by checking the permission, no kit will be given if the player doesn't have a permission for any kit. saved kits are ignored
	 */
	public Kit setKit(final Player player) {
		KitsManager km = apk.getKitsManager();
		String name = null;
		boolean hasPerm = false;
		for(String kitName : km.getKits().keySet()) {
			if(player.hasPermission("autopvpkit.kits." + kitName)) {
				hasPerm = true;
				name = kitName;
			}
		}
		if(!hasPerm || name == null) {
			return null;
		}
		Kit kit = km.getKit(apk.getAutoPvPKitCommand().matchKit(name));
		final PlayerInventory pinv = player.getInventory();
		ItemStack helmet = kit.getHelmet();
		if(helmet != null) {
			pinv.setHelmet(helmet);
		}
		ItemStack chestplate = kit.getChestplate();
		if(chestplate != null) {
			pinv.setChestplate(chestplate);
		}
		ItemStack leggings = kit.getLeggings();
		if(leggings != null) {
			pinv.setLeggings(leggings);
		}
		ItemStack boots = kit.getBoots();
		if(boots != null) {
			pinv.setBoots(boots);
		}
		kit.getItems().entrySet().forEach(entry -> {
			pinv.setItem(entry.getKey(), entry.getValue());
		});
		return kit;
	}
	
	/**
	 * 
	 * @param player
	 * @param clearInventory clear's player inventory before selecting a kit.
	 * @description
	 * set kit by checking the permission, no kit will be given if the player doesn't have a permission for any kit. saved kits will be put in count.
	 */
	public Kit setKit(final Player player, final boolean clearInventory) {
		KitsManager km = apk.getKitsManager();
		String name = null;
		boolean hasPerm = false;
		for(String kitName : km.getKits().keySet()) {
			if(player.hasPermission("autopvpkit.kits." + kitName)) {
				hasPerm = true;
				name = kitName;
			}
		}
		if(!hasPerm || name == null) {
			return null;
		}
		Kit kit = km.getKit(apk.getAutoPvPKitCommand().matchKit(name));
		String kitName = kit.getName();
		String playerName = player.getName();
		final PlayerInventory pinv = player.getInventory();
		if(clearInventory) {
			pinv.clear();
		}
		ItemStack helmet = kit.getHelmet();
		if(helmet != null) {
			pinv.setHelmet(helmet);
		}
		ItemStack chestplate = kit.getChestplate();
		if(chestplate != null) {
			pinv.setChestplate(chestplate);
		}
		ItemStack leggings = kit.getLeggings();
		if(leggings != null) {
			pinv.setLeggings(leggings);
		}
		ItemStack boots = kit.getBoots();
		if(boots != null) {
			pinv.setBoots(boots);
		}
	    if(apk.getPlayerManager().hasSavedKit(playerName, kitName)) {
	    	SavedItemSlots sis = apk.getPlayerManager().getSavedPlayerKit(playerName, kitName);
	    	sis.getSavedItemSlots().entrySet().forEach(entry -> {
	    		pinv.setItem(entry.getKey(), entry.getValue());
	    	});
	    } else {
		kit.getItems().entrySet().forEach(entry -> {
			pinv.setItem(entry.getKey(), entry.getValue());
		});
	    }
	    apk.getLastSelectedKits().put(playerName, kit);
	    apk.hasKit.add(playerName);
	    return kit;
	}
	
	/**
	 * 
	 * @param player
	 * @return the last kit player selected
	 */
	public Kit getPlayerLastKit(final Player player) {
		return apk.getPlayerLastSelectedKit(player.getName());
	}
	
}
