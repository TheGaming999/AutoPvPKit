package me.autopvpkit.listeners;

import java.util.Iterator;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.utils.NBTEditor;

public class DeathDropListener implements Listener {

	private AutoPvPKit plugin;

	public DeathDropListener(AutoPvPKit plugin) {
		this.plugin = plugin;
	}

	public void unregister() {
		PlayerDeathEvent.getHandlerList().unregister(this);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeathDrop(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if (plugin.isDisabledWorld(p.getWorld())) return;
		if (!plugin.isDisableKitDropsOnDeath()) return;
		String name = p.getName();
		if (!plugin.hasKit.contains(name)) return;
		Iterator<ItemStack> dropsIterator = e.getDrops().iterator();
		while (dropsIterator.hasNext()) {
			if (NBTEditor.contains(dropsIterator.next(), "autopvpkit")) dropsIterator.remove();
		}
		plugin.hasKit.remove(name);
	}

}
