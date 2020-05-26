package me.autopvpkit;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.autopvpkit.commands.AutoPvPKitCommand;
import me.autopvpkit.data.KitsManager;
import me.autopvpkit.data.PlayerManager;
import me.autopvpkit.hooks.WorldGuardHook;
import me.autopvpkit.listeners.DeathDropListener;
import me.autopvpkit.listeners.ItemDropListener;
import me.autopvpkit.listeners.JoinListener;
import me.autopvpkit.listeners.RespawnListener;
import me.autopvpkit.listeners.WorldChangeListener;

public class AutoPvPKit extends JavaPlugin {

	private KitsManager kitsManager;
	private PlayerManager playerManager;
	private Set<String> disabledWorlds;
	private boolean changeOnSpawn;
	private boolean changeOnRespawn;
	private boolean changeOnWorldChange;
	private boolean disableKitDrops;
	private boolean disableKitDropsOnDeath;
	
	private boolean worldGuardHook_;
	private boolean changeOnWorldGuardRegion;
	private String worldGuardRegion;
	
	private JoinListener joinListener;
	private RespawnListener respawnListener;
	private WorldChangeListener worldChangeListener;
	private ItemDropListener itemDropListener;
	private DeathDropListener deathDropListener;
	private AutoPvPKitCommand autoPvpKitCommand;
	private WorldGuardHook worldGuardHook;
	
	public Set<String> hasKit;
	
	public void onEnable() {
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		disabledWorlds = new HashSet<>();
		hasKit = new HashSet<>();
		// for silly users
		getConfig().getStringList("Settings.disabled-worlds").forEach(world_ -> {
			Bukkit.getWorlds().forEach(world -> {
				if(world_.equalsIgnoreCase(world.getName())) {
					disabledWorlds.add(world.getName());
				}
			});
		});
		//
		changeOnSpawn = getConfig().getBoolean("Settings.change-on-spawn");
		changeOnRespawn = getConfig().getBoolean("Settings.change-on-respawn");
		changeOnWorldChange = getConfig().getBoolean("Settings.change-on-world-change");
		disableKitDrops = getConfig().getBoolean("Settings.disable-kit-drops");
		disableKitDropsOnDeath = getConfig().getBoolean("Settings.disable-kit-drops-on-death");
		worldGuardHook_ = getConfig().getBoolean("Settings.worldguard-hook");
		changeOnWorldGuardRegion = getConfig().getBoolean("WorldGuard-Settings.change-on-worldguard-region");
		worldGuardRegion = getConfig().getString("WorldGuard-Settings.worldguard-region");
		
        registerListeners();
		if(disableKitDrops || disableKitDropsOnDeath) {
			playerManager = new PlayerManager();
		}
		if(worldGuardHook_ && Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
			worldGuardHook = new WorldGuardHook(this);
		}
		kitsManager = new KitsManager(this);
		autoPvpKitCommand = new AutoPvPKitCommand(this);
		getCommand("autopvpkit").setExecutor(autoPvpKitCommand);
		Bukkit.getConsoleSender().sendMessage(colorize("&e[&9AutoPvPKit&e] &aLoading kits..."));
		kitsManager.loadKits();
		Bukkit.getConsoleSender().sendMessage(colorize("&e[&9AutoPvPKit&e] &aEnabled."));
	}
	
	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage(colorize("&e[&9AutoPvPKit&e] &cDisabled."));
	}
	
	public String colorize(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public void registerListeners() {
		if(changeOnSpawn) {
			joinListener = new JoinListener(this);
			Bukkit.getPluginManager().registerEvents(joinListener, this);
		}
		if(changeOnRespawn) {
			respawnListener = new RespawnListener(this);
			Bukkit.getPluginManager().registerEvents(respawnListener, this);
		}
		if(changeOnWorldChange) {
			worldChangeListener = new WorldChangeListener(this);
			Bukkit.getPluginManager().registerEvents(worldChangeListener, this);
		}
		if(disableKitDrops) {
			itemDropListener = new ItemDropListener(this);
			Bukkit.getPluginManager().registerEvents(itemDropListener, this);
		}
		deathDropListener = new DeathDropListener(this);
	    Bukkit.getPluginManager().registerEvents(deathDropListener, this);
	}
	
	public boolean isChangeOnSpawn() {
		return this.changeOnSpawn;
	}
	
	public boolean isChangeOnRespawn() {
		return this.changeOnRespawn;
	}
	
	public boolean isChangeOnWorldChange() {
		return this.changeOnWorldChange;
	}
	
	public boolean isDisableKitDrops() {
		return this.disableKitDrops;
	}
	
	public boolean isDisableKitDropsOnDeath() {
		return this.disableKitDropsOnDeath;
	}
	
	public Set<String> getDisabledWorlds() {
		return this.disabledWorlds;
	}
	
	public boolean isDisabledWorld(World world) {
		return this.disabledWorlds.contains(world.getName());
	}
	
	public KitsManager getKitsManager() {
		return this.kitsManager;
	}
	
	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}
	
	public WorldGuardHook getWorldGuardHook() {
		return this.worldGuardHook;
	}

	public boolean isChangeOnWorldGuardRegion() {
		return changeOnWorldGuardRegion;
	}

	public void setChangeOnWorldGuardRegion(boolean changeOnWorldGuardRegion) {
		this.changeOnWorldGuardRegion = changeOnWorldGuardRegion;
	}

	public String getWorldGuardRegion() {
		return worldGuardRegion;
	}

	public void setWorldGuardRegion(String worldGuardRegion) {
		this.worldGuardRegion = worldGuardRegion;
	}
	
}
