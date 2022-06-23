package me.autopvpkit.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.events.ChangeReason;
import me.autopvpkit.events.PlayerKitChangeEvent;

public class RespawnListener implements Listener {

	private AutoPvPKit plugin;
	private Set<UUID> players;

	public RespawnListener(AutoPvPKit plugin) {
		this.plugin = plugin;
		this.players = new HashSet<>();
	}

	public void unregister() {
		PlayerRespawnEvent.getHandlerList().unregister(this);
		PlayerPickupItemEvent.getHandlerList().unregister(this);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onRespawn(PlayerRespawnEvent e) {
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			if (!plugin.isChangeOnRespawn()) return;
			Player p = e.getPlayer();
			if (plugin.isDisabledWorld(p.getWorld())) return;
			PlayerKitChangeEvent ce = new PlayerKitChangeEvent(p, ChangeReason.RESPAWN);
			Bukkit.getPluginManager().callEvent(ce);
			if (ce.isCancelled()) return;
			players.add(p.getUniqueId());
			plugin.getAPI().setKit(p, true);
			p.updateInventory();
		}, 1);
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent e) {
		UUID uniqueId = e.getPlayer().getUniqueId();
		if (players.contains(uniqueId)) {
			e.setCancelled(true);
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				players.remove(uniqueId);
			}, 1);
		}
	}

}
