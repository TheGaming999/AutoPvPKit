package me.autopvpkit.listeners;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import me.autopvpkit.AutoPvPKit;

public class ItemDropListener implements Listener {

	private AutoPvPKit plugin;
	
	public ItemDropListener(AutoPvPKit plugin) {this.plugin = plugin;}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(plugin.isDisabledWorld(p.getWorld())) {
			return;
		}
		Item droppedItem = e.getItemDrop();
		if(plugin.getPlayerManager().getPlayerItems(p.getName()).contains(droppedItem.getItemStack())) {
			e.setCancelled(true);
			return;
		}
	}
	
}
