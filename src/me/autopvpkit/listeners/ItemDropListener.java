package me.autopvpkit.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.utils.NBTEditor;

public class ItemDropListener implements Listener {

	private AutoPvPKit plugin;

	public ItemDropListener(AutoPvPKit plugin) {
		this.plugin = plugin;
	}

	public void unregister() {
		PlayerDropItemEvent.getHandlerList().unregister(this);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (plugin.isDisabledWorld(p.getWorld())) {
			return;
		}
		Item droppedItem = e.getItemDrop();
		ItemStack itemStack = droppedItem.getItemStack();
		if (droppedItem == null || itemStack.getType() == Material.AIR) {
			return;
		}
		if (NBTEditor.contains(itemStack, "autopvpkit")) {
			e.setCancelled(true);
			return;
		}
	}

}
