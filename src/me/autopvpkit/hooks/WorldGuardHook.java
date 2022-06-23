package me.autopvpkit.hooks;

import org.bukkit.Bukkit;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.listeners.MoveListener;

public class WorldGuardHook {

	private AutoPvPKit plugin;
	private WorldGuardPlugin wg;
	private MoveListener moveListener;

	public WorldGuardHook(AutoPvPKit plugin) {
		this.setPlugin(plugin);
		this.wg = WorldGuardPlugin.inst();
		moveListener = new MoveListener(this.plugin);
		Bukkit.getPluginManager().registerEvents(moveListener, this.plugin);
	}

	public WorldGuardPlugin getWorldGuard() {
		return this.wg;
	}

	public AutoPvPKit getPlugin() {
		return plugin;
	}

	public void setPlugin(AutoPvPKit plugin) {
		this.plugin = plugin;
	}

}
