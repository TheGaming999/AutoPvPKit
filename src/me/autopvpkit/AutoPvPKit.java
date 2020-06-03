package me.autopvpkit;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import me.autopvpkit.commands.AutoPvPKitCommand;
import me.autopvpkit.commands.SaveKitCommand;
import me.autopvpkit.data.Kit;
import me.autopvpkit.data.KitsManager;
import me.autopvpkit.data.PlayerManager;
import me.autopvpkit.data.SavedItemSlots;
import me.autopvpkit.hooks.WorldGuardHook;
import me.autopvpkit.listeners.DeathDropListener;
import me.autopvpkit.listeners.ItemDropListener;
import me.autopvpkit.listeners.JoinListener;
import me.autopvpkit.listeners.RespawnListener;
import me.autopvpkit.listeners.WorldChangeListener;
import me.autopvpkit.utils.ConfigManager;

public class AutoPvPKit extends JavaPlugin {

	private KitsManager kitsManager;
	private PlayerManager playerManager;
	private ConfigManager configManager;
	private Set<String> disabledWorlds;
	private FileConfiguration config;
	private FileConfiguration savedKits;
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
	private SavedItemSlots savedItemSlots;
	private SaveKitCommand saveKitCommand;
	
	
	private static AutoPvPKit instance;
	public Set<String> hasKit;
	private Map<String, Kit> lastSelectedKits;
	
	public void onEnable() {
		lastSelectedKits = new ConcurrentHashMap<>();
		configManager = new ConfigManager(this);
		config = configManager.loadConfig("config.yml");
		savedKits = configManager.loadConfig("saved_kits.yml");
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
			
		}

		if(worldGuardHook_ && Bukkit.getPluginManager().isPluginEnabled("WorldGuard")) {
			worldGuardHook = new WorldGuardHook(this);
		}
		kitsManager = new KitsManager(this);
		autoPvpKitCommand = new AutoPvPKitCommand(this);
		getCommand("autopvpkit").setExecutor(autoPvpKitCommand);
		saveKitCommand = new SaveKitCommand(this);
		getCommand("savekit").setExecutor(saveKitCommand);
		Bukkit.getConsoleSender().sendMessage(colorize("&e[&9AutoPvPKit&e] &aLoading kits..."));
		kitsManager.loadKits();
		savedItemSlots = new SavedItemSlots();
		playerManager = new PlayerManager(this);
		playerManager.loadSavedPlayerKits();
		Bukkit.getConsoleSender().sendMessage(colorize("&e[&9AutoPvPKit&e] &aEnabled."));
		instance = this;
	}
	
	public void onDisable() {
		playerManager.saveSavedPlayerKits();
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

	public FileConfiguration getConfig() {
		return config;
	}

	public void setConfig(FileConfiguration config) {
		this.config = config;
	}

	public FileConfiguration getSavedKitsConfig() {
		return savedKits;
	}

	public void setSavedKitsConfig(FileConfiguration savedKits) {
		this.savedKits = savedKits;
	}
	
	public void reloadConfig() {
		this.configManager.reloadConfig("config.yml");
	}
	
	public void saveConfig() {
		this.configManager.saveConfig("config.yml");
	}
	
	public void reloadSavedKitsConfig() {
		this.configManager.reloadConfig("saved_kits.yml");
	}
	
	public void saveSavedKitsConfig() {
		this.configManager.saveConfig("saved_kits.yml");
	}

	public SavedItemSlots getSavedItemSlots() {
		return savedItemSlots;
	}

	public void setSavedItemSlots(SavedItemSlots savedItemSlots) {
		this.savedItemSlots = savedItemSlots;
	}

	/**
	 * 
	 * @param name player's name
	 * @return
	 */
	public Kit getPlayerLastSelectedKit(String name) {
		return lastSelectedKits.get(name);
	}
	
	public Map<String, Kit> getLastSelectedKits() {
		return lastSelectedKits;
	}

	public void setLastSelectedKits(Map<String, Kit> lastSelectedKits) {
		this.lastSelectedKits = lastSelectedKits;
	}
	
	public SaveKitCommand getSaveKitCommand() {
		return this.saveKitCommand;
	}

	public static AutoPvPKit getInstance() {
		return instance;
	}

	public static void setInstance(AutoPvPKit instance) {
		AutoPvPKit.instance = instance;
	}
	
}
