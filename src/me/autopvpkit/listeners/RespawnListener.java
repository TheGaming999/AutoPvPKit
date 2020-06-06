package me.autopvpkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.events.ChangeReason;
import me.autopvpkit.events.PlayerKitChangeEvent;

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
		PlayerKitChangeEvent ce = new PlayerKitChangeEvent(p, ChangeReason.RESPAWN);
		Bukkit.getPluginManager().callEvent(ce);
		if(ce.isCancelled()) {
			return;
		}
		plugin.getAPI().setKit(p, true);
		p.updateInventory();
		}, 5);
	}
	
}
