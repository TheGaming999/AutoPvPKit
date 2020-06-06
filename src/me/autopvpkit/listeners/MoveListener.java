package me.autopvpkit.listeners;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.events.ChangeReason;
import me.autopvpkit.events.PlayerKitChangeEvent;

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
				PlayerKitChangeEvent ce = new PlayerKitChangeEvent(p, ChangeReason.REGION_ENTRY);
				Bukkit.getPluginManager().callEvent(ce);
				if(ce.isCancelled()) {
					return;
				}
				plugin.getAPI().setKit(p, true);
			    p.updateInventory();
			}
		}
	}
	
}
