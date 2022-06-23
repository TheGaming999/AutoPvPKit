package me.autopvpkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.events.ChangeReason;
import me.autopvpkit.events.PlayerKitChangeEvent;

public class WorldChangeListener implements Listener {

	private AutoPvPKit plugin;

	public WorldChangeListener(AutoPvPKit plugin) {
		this.plugin = plugin;
	}

	public void unregister() {
		PlayerChangedWorldEvent.getHandlerList().unregister(this);
	}

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent e) {
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (!plugin.isChangeOnWorldChange()) {
				return;
			}
			Player p = e.getPlayer();
			if (plugin.isDisabledWorld(p.getWorld())) {
				return;
			}
			PlayerKitChangeEvent ce = new PlayerKitChangeEvent(p, ChangeReason.WORLD_CHANGE);
			Bukkit.getPluginManager().callEvent(ce);
			if (ce.isCancelled()) {
				return;
			}
			plugin.getAPI().setKit(p, true);
			p.updateInventory();
		}, 5);
	}

}
