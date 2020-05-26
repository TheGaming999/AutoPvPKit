package me.autopvpkit.listeners;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.data.Kit;

public class MoveListener implements Listener {

	private AutoPvPKit plugin;
	private String worldGuardRegion;
	private Set<String> hasKit;
	
	public MoveListener(AutoPvPKit plugin) {this.plugin = plugin;
	this.worldGuardRegion = this.plugin.getWorldGuardRegion();
	this.hasKit = this.plugin.hasKit;}
	
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		if(!plugin.isChangeOnWorldGuardRegion()) {
			return;
		}
		// let's cache
		Location from = e.getFrom();
		Location to = e.getTo();
		int x = to.getBlockX();
		int y = to.getBlockY();
		int z= to.getBlockZ();
		Player p = e.getPlayer();
		String name = p.getName();
		World w = p.getWorld();
		// check for a leg movement not head movement to prevent lag.
		if(from.getBlockX() != x || from.getBlockY() != y || from.getBlockZ() != z) {
			if(this.hasKit.contains(name)) {
				return;
			}
			if(!plugin.getWorldGuardHook().getWorldGuard().getRegionManager(w).hasRegion(worldGuardRegion)) {
				return;
			}
			if(plugin.getWorldGuardHook().getWorldGuard().getRegionManager(w).getRegion(worldGuardRegion).contains(x, y, z)) {
				if(plugin.isDisabledWorld(w)) {
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
						items.keySet().forEach(slot -> {
							pinv.setItem(slot, items.get(slot));
						});
					}
				});
				p.updateInventory();
				this.hasKit.add(name);
			}	
		}
	}
	
}
