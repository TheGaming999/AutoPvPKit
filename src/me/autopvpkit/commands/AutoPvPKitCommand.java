package me.autopvpkit.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.data.Kit;
import me.autopvpkit.events.ChangeReason;
import me.autopvpkit.events.PlayerKitChangeEvent;

public class AutoPvPKitCommand implements CommandExecutor {

	private AutoPvPKit plugin;

	public AutoPvPKitCommand(AutoPvPKit plugin) {
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
		if (cmd.getLabel().equalsIgnoreCase("autopvpkit")) {
			if (args.length == 0) {
				sender.sendMessage(colorize("&7&m                                               &7"));
				sender.sendMessage(colorize("&7/apk help &c- &bshows this help page"));
				sender.sendMessage(colorize("&7/apk kits &c- &blists available kits"));
				sender.sendMessage(colorize("&7/apk kit <name> [player] &c- &bselect kit for you or another player"));
				sender.sendMessage(colorize("&7/apk reload &c- &breloads the plugin"));
				sender.sendMessage(colorize("&7&m                                               &7"));
			} else if (args.length == 1) {
				if (args[0].equalsIgnoreCase("kits")) {
					sender.sendMessage(colorize("&7< &8&lKITS&r &7>"));
					plugin.getKitsManager().getKits().values().forEach(kit -> {
						sender.sendMessage(colorize("&7- ") + kit.getDisplayName());
					});
					sender.sendMessage(colorize("&7&m                                      &7"));
				} else if (args[0].equalsIgnoreCase("kit")) {
					sender.sendMessage(
							colorize("&7/apk kit <name> [player] &c- &bselect kit for you or another player"));
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (!sender.hasPermission("autopvpkit.admin")) {
						return true;
					}
					sender.sendMessage(colorize("&eReloading..."));
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						plugin.onDisable();
						plugin.reloadConfig();
						plugin.onEnable();
					});
					sender.sendMessage(colorize("&aPlugin reloaded."));
				} else if (args[0].equalsIgnoreCase("load")) {
					if (!sender.hasPermission("autopvpkit.admin")) {
						return true;
					}
					sender.sendMessage(colorize("&eLoading..."));
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						plugin.reloadConfig();
						plugin.onEnable();
					});
					sender.sendMessage(colorize("&aPlugin data loaded."));
				} else if (args[0].equalsIgnoreCase("save")) {
					if (!sender.hasPermission("autopvpkit.admin")) {
						return true;
					}
					sender.sendMessage(colorize("&eSaving..."));
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						plugin.getPlayerManager().saveSavedPlayerKits();
					});
					sender.sendMessage(colorize("&aPlugin data saved."));
				}
			} else if (args.length == 2) {
				if (args[0].equalsIgnoreCase("kit")) {
					Player p = (Player) sender;
					String kit = matchKit(args[1]);
					if (!plugin.getKitsManager().getKits().containsKey(kit)) {
						p.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-does-not-exist"))
								.replace("%kit%", kit));
						return true;
					}
					if (!p.hasPermission("autopvpkit.kit")) {
						p.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-no-permission"))
								.replace("%kit%", kit));
						return true;
					}
					String ckit = StringUtils.capitalize(kit);
					if (p.hasPermission("autopvpkit.kits." + kit) || p.hasPermission("autopvpkit.kits.*")) {
						PlayerKitChangeEvent ce = new PlayerKitChangeEvent(p, ChangeReason.COMMAND);
						Bukkit.getPluginManager().callEvent(ce);
						if (ce.isCancelled()) {
							return true;
						}
						Kit k = plugin.getAPI().setKit(p, kit, true);
						p.sendMessage(
								colorize(plugin.getConfig().getString("Settings.kit-message")).replace("%kit%", ckit)
										.replace("%kit_displayname%", k.getDisplayName()));
					} else {
						p.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-no-permission"))
								.replace("%kit%", ckit)
								.replace("%kit_displayname%", ckit));
					}
				}
			} else if (args.length == 3) {
				Player p = Bukkit.getPlayer(args[2]);
				if (p == null) {
					sender.sendMessage(colorize(plugin.getConfig().getString("Settings.player-does-not-exist"))
							.replace("%player%", args[2]));
					return true;
				}
				String kit = matchKit(args[1]);
				if (!plugin.getKitsManager().getKits().containsKey(kit)) {
					sender.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-does-not-exist"))
							.replace("%kit%", kit));
					return true;
				}
				if (!sender.hasPermission("autopvpkit.kit.other")) {
					sender.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-other-no-permission")));
					return true;
				}
				String ckit = StringUtils.capitalize(kit);
				if (sender.hasPermission("autopvpkit.kits." + kit) || sender.hasPermission("autopvpkit.kits.*")) {
					PlayerKitChangeEvent ce = new PlayerKitChangeEvent(p, ChangeReason.COMMAND_BY_OTHER);
					Bukkit.getPluginManager().callEvent(ce);
					if (ce.isCancelled()) {
						return true;
					}
					Kit k = plugin.getAPI().setKit(p, kit, true);
					p.sendMessage(colorize(plugin.getConfig().getString("Settings.kit-message")).replace("%kit%", ckit)
							.replace("%kit_displayname%", k.getDisplayName()));
					sender.sendMessage(
							colorize(plugin.getConfig().getString("Settings.kit-message-other")).replace("%kit%", ckit)
									.replace("%kit_displayname%", k.getDisplayName())
									.replace("%player%", p.getName()));
				} else {
					sender.sendMessage(
							colorize(plugin.getConfig().getString("Settings.kit-no-permission")).replace("%kit%", ckit)
									.replace("%kit_displayname%", ckit));
				}
			}
		}
		return true;
	}

}
