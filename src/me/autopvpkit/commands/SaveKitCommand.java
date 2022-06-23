package me.autopvpkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.data.Kit;
import me.autopvpkit.events.PlayerKitSaveEvent;

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
		for (String kits : plugin.getKitsManager().getKits().keySet()) {
			if (kits.equalsIgnoreCase(kitName)) {
				kit = kits;
			}
		}
		return kit;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			return true;
		}
		if (!sender.hasPermission("autopvpkit.savekit")) {
			sender.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-save-no-permission")));
			return true;
		}
		final Player p = (Player) sender;
		final String name = p.getName();
		if (!plugin.getLastSelectedKits().containsKey(name)) {
			p.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-save-no-selected-kit")));
			return true;
		}
		Kit kit = plugin.getPlayerLastSelectedKit(name);
		final String kitName = kit.getName();
		if (args.length == 1) {
			if (plugin.getPlayerManager().hasSavedKit(name, kitName)) {
				plugin.getPlayerManager().removeSavedPlayerKit(name, kitName);
			}
			sender.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-save-removed")));
			return true;
		}

		PlayerKitSaveEvent se = new PlayerKitSaveEvent(p, kit);
		Bukkit.getPluginManager().callEvent(se);
		if (se.isCancelled()) {
			return true;
		}
		try {
			plugin.getPlayerManager().registerSavedPlayerKit(p);
			sender.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-save")).replace("%kit%", kitName)
					.replace("%kit_displayname%", kit.getDisplayName()));
		} catch (NullPointerException ex) {
			sender.sendMessage(colorize("&cKIT SAVING FAILED! please notify the administrators."));
			ex.printStackTrace();
		}
		return true;
	}

}
