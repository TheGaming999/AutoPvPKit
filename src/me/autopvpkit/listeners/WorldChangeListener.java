package me.autopvpkit.listeners;

import java.util.HashSet;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.data.Kit;


public class WorldChangeListener implements Listener {

	private AutoPvPKit plugin;
	
	public WorldChangeListener(AutoPvPKit plugin) {this.plugin = plugin;}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
		if(!plugin.isChangeOnWorldChange()) {
			return;
		}
		Player p = e.getPlayer();
		String name = p.getName();
		if(plugin.isDisabledWorld(p.getWorld())) {
			return;
		}
		PlayerInventory pinv = p.getInventory();
		pinv.clear();
		plugin.getKitsManager().getKits().keySet().forEach(kit -> {
			if(p.hasPermission("autopvpkit.kits." + kit)) {
				Kit k = plugin.getKitsManager().getKit(kit);
				
				ItemStack helmet = k.getHelmet();
				if(helmet != null) {
					pinv.setHelmet(helmet);
				}
				ItemStack chestplate = k.getChestplate();
				if(chestplate != null) {
					pinv.setChestplate(chestplate);
				}
				ItemStack leggings = k.getLeggings();
				if(leggings != null) {
					pinv.setLeggings(leggings);
				}
				ItemStack boots = k.getBoots();
				if(boots != null) {
					pinv.setBoots(boots);
				}
				Map<Integer, ItemStack> items = k.getItems();
				if(plugin.isDisableKitDrops() || plugin.isDisableKitDropsOnDeath()) {
				Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				plugin.getPlayerManager().setPlayerItems(p.getName(), new HashSet<>(items.values()));
				});
				}				
				if(plugin.getPlayerManager().getSavedPlayerKits().containsKey(name + "#" + k.getName())) {
					plugin.getPlayerManager().getSavedPlayerKit(name, kit).getSavedItemSlots().entrySet().forEach(entry -> {
						int slot = entry.getKey();
						ItemStack itemStack = entry.getValue();
						pinv.setItem(slot, itemStack);
					});
					plugin.hasKit.add(name);
					plugin.getLastSelectedKits().put(name, k);
					return;
				}
				items.keySet().forEach(slot -> {
					pinv.setItem(slot, items.get(slot));
				});
				plugin.hasKit.add(name);
				plugin.getLastSelectedKits().put(name, k);
			}
		});
		p.updateInventory();
		}, 5);
	}
	
}
