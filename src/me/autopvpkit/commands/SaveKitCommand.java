package me.autopvpkit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.data.Kit;

public class SaveKitCommand implements CommandExecutor {

	private AutoPvPKit plugin;
	
	public SaveKitCommand(AutoPvPKit plugin) {
		this.plugin = plugin;
	}
	
	public String colorize(String string) {
		return plugin.colorize(string);
	}
	
	public String matchKit(String kitName) {
		String kit = kitName;
		for(String kits : plugin.getKitsManager().getKits().keySet()) {
			if(kits.equalsIgnoreCase(kitName)) {
				kit = kits;
			}
		}
		return kit;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	    if(!(sender instanceof Player)) {
	    	return true;
	    }
	    if(!sender.hasPermission("autopvpkit.savekit")) {
	    	sender.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-save-no-permission")));
	    	return true;
	    }
	    final Player p = (Player)sender;
	    if(!plugin.getLastSelectedKits().containsKey(p.getName())) {
	    	p.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-save-no-selected-kit")));
	    	return true;
	    }
	    Kit kit = plugin.getPlayerLastSelectedKit(p.getName());
	    try {
	    plugin.getPlayerManager().registerSavedPlayerKit(p);
	    sender.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-save")).replace("%kit%", kit.getName()).replace("%kit_displayname%", kit.getDisplayName()));
	    } catch (NullPointerException ex) {
	    	sender.sendMessage(colorize("&cKIT SAVING FAILED! please notify the administrators."));
	    	ex.printStackTrace();
	    }
		return true;
	}

}
