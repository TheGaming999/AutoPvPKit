package me.autopvpkit.listeners;

import java.util.HashSet;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.data.Kit;

public class RespawnListener implements Listener {

	private AutoPvPKit plugin;
	public RespawnListener(AutoPvPKit plugin) {this.plugin = plugin;};
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent e) {
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
		if(!plugin.isChangeOnRespawn()) {
			return;
		}
		Player p = e.getPlayer();
		if(plugin.isDisabledWorld(p.getWorld())) {
			return;
		}
		String name = p.getName();
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
