package me.autopvpkit.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.autopvpkit.AutoPvPKit;

public class DeathDropListener implements Listener {

	private AutoPvPKit plugin;
	
	public DeathDropListener(AutoPvPKit plugin) {this.plugin = plugin;}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDeathDrop(PlayerDeathEvent e) {
		Player p = e.getEntity();
		if(plugin.isDisabledWorld(p.getWorld())) {
			return;
		}
		if(!plugin.isDisableKitDropsOnDeath()) {
			return;
		}
		String name = p.getName();
		ExperienceOrb eo = (ExperienceOrb)p.getWorld().spawnEntity(p.getLocation(), EntityType.EXPERIENCE_ORB);
		p.getWorld().spawnEntity(p.getLocation().add(1, 0, 0), eo.getType());
		p.getWorld().spawnEntity(p.getLocation().add(0, 0, 1), eo.getType());
		e.getDrops().clear();
		//e.getDrops().forEach(stack -> {
          // if(plugin.getPlayerManager().getPlayerItems(name).contains(stack)) {
        	 //  p.getInventory().remove(stack);
          // }
		//});
		this.plugin.hasKit.remove(name);
	}
	
}
