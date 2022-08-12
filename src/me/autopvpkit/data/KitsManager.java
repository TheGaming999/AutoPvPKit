package me.autopvpkit.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.autopvpkit.AutoPvPKit;
import me.autopvpkit.utils.NBTEditor;
import me.autopvpkit.utils.XMaterial;

public class KitsManager {

	private AutoPvPKit plugin;
	private FileConfiguration config;
	private Map<String, Kit> kits;

	public KitsManager(AutoPvPKit plugin) {
		this.plugin = plugin;
		this.config = this.plugin.getConfig();
		this.kits = new LinkedHashMap<>();
	}

	private String colorize(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	private List<String> colorize(List<String> list) {
		List<String> newList = new ArrayList<>();
		list.forEach(line -> {
			newList.add(colorize(line));
		});
		return newList;
	}

	private Material material(String material) {
		return XMaterial.matchXMaterial(material).orElse(XMaterial.AIR).parseMaterial();
	}

	public void loadKits() {
		kits.clear();
		ConfigurationSection kitsPath = config.getConfigurationSection("Kits");
		kitsPath.getKeys(false).forEach(kitName -> {
			Kit kit = new Kit(kitName);
			kit.setDisplayName(colorize(kitsPath.getString(kitName + ".display-name")));
			// HELMET
			ItemStack helmetStack = null;
			String helmetItem = kitsPath.getString(kitName + ".helmet.item");
			int helmetAmount = kitsPath.getInt(kitName + ".helmet.amount");
			short helmetData = (short) kitsPath.getInt(kitName + ".helmet.data");
			helmetStack = new ItemStack(material(helmetItem), helmetAmount, helmetData);
			ItemMeta helmetMeta = helmetStack.getItemMeta();
			String helmetName = kitsPath.isSet(kitName + ".helmet.name")
					? colorize(kitsPath.getString(kitName + ".helmet.name")) : null;
			List<String> helmetLore = kitsPath.isSet(kitName + ".helmet.lore")
					? colorize(kitsPath.getStringList(kitName + ".helmet.lore")) : null;
			List<String> helmetEnchantments = kitsPath.isSet(kitName + ".helmet.enchantments")
					? kitsPath.getStringList(kitName + ".helmet.enchantments") : null;
			List<String> helmetFlags = kitsPath.isSet(kitName + ".helmet.flags")
					? kitsPath.getStringList(kitName + ".helmet.flags") : null;
			if (helmetName != null) {
				helmetMeta.setDisplayName(helmetName);
			}
			if (helmetLore != null) {
				helmetMeta.setLore(helmetLore);
			}
			if (helmetEnchantments != null) {
				helmetEnchantments.forEach(enchantWithLVL -> {
					String[] enchantSplitter = enchantWithLVL.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(enchantSplitter[0]);
					int lvl = Integer.parseInt(enchantSplitter[1]);
					helmetMeta.addEnchant(enchant, lvl, true);
				});
			}
			if (helmetFlags != null) {
				helmetFlags.forEach(flag -> {
					helmetMeta.addItemFlags(ItemFlagReader.matchItemFlag(flag));
				});
			}
			helmetStack.setItemMeta(helmetMeta);
			if (plugin.isDisableKitDrops() || plugin.isDisableKitDropsOnDeath())
				helmetStack = NBTEditor.set(helmetStack, true, "autopvpkit");
			kit.setHelmet(helmetStack);
			// CHESTPLATE
			ItemStack chestplateStack = null;
			String chestplateItem = kitsPath.getString(kitName + ".chestplate.item");
			int chestplateAmount = kitsPath.getInt(kitName + ".chestplate.amount");
			short chestplateData = (short) kitsPath.getInt(kitName + ".chestplate.data");
			chestplateStack = new ItemStack(material(chestplateItem), chestplateAmount, chestplateData);
			ItemMeta chestplateMeta = chestplateStack.getItemMeta();
			String chestplateName = kitsPath.isSet(kitName + ".chestplate.name")
					? colorize(kitsPath.getString(kitName + ".chestplate.name")) : null;
			List<String> chestplateLore = kitsPath.isSet(kitName + ".chestplate.lore")
					? colorize(kitsPath.getStringList(kitName + ".chestplate.lore")) : null;
			List<String> chestplateEnchantments = kitsPath.isSet(kitName + ".chestplate.enchantments")
					? kitsPath.getStringList(kitName + ".chestplate.enchantments") : null;
			List<String> chestplateFlags = kitsPath.isSet(kitName + ".chestplate.flags")
					? kitsPath.getStringList(kitName + ".chestplate.flags") : null;
			if (chestplateName != null) {
				chestplateMeta.setDisplayName(chestplateName);
			}
			if (chestplateLore != null) {
				chestplateMeta.setLore(chestplateLore);
			}
			if (chestplateEnchantments != null) {
				chestplateEnchantments.forEach(enchantWithLVL -> {
					String[] enchantSplitter = enchantWithLVL.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(enchantSplitter[0]);
					int lvl = Integer.parseInt(enchantSplitter[1]);
					chestplateMeta.addEnchant(enchant, lvl, true);
				});
			}
			if (chestplateFlags != null) {
				chestplateFlags.forEach(flag -> {
					chestplateMeta.addItemFlags(ItemFlagReader.matchItemFlag(flag));
				});
			}
			chestplateStack.setItemMeta(chestplateMeta);
			if (plugin.isDisableKitDrops() || plugin.isDisableKitDropsOnDeath())
				chestplateStack = NBTEditor.set(chestplateStack, true, "autopvpkit");
			kit.setChestplate(chestplateStack);
			// LEGGINGS
			ItemStack leggingsStack = null;
			String leggingsItem = kitsPath.getString(kitName + ".leggings.item");
			int leggingsAmount = kitsPath.getInt(kitName + ".leggings.amount");
			short leggingsData = (short) kitsPath.getInt(kitName + ".leggings.data");
			leggingsStack = new ItemStack(material(leggingsItem), leggingsAmount, leggingsData);
			ItemMeta leggingsMeta = leggingsStack.getItemMeta();
			String leggingsName = kitsPath.isSet(kitName + ".leggings.name")
					? colorize(kitsPath.getString(kitName + ".leggings.name")) : null;
			List<String> leggingsLore = kitsPath.isSet(kitName + ".leggings.lore")
					? colorize(kitsPath.getStringList(kitName + ".leggings.lore")) : null;
			List<String> leggingsEnchantments = kitsPath.isSet(kitName + ".leggings.enchantments")
					? kitsPath.getStringList(kitName + ".leggings.enchantments") : null;
			List<String> leggingsFlags = kitsPath.isSet(kitName + ".leggings.flags")
					? kitsPath.getStringList(kitName + ".leggings.flags") : null;
			if (leggingsName != null) {
				leggingsMeta.setDisplayName(leggingsName);
			}
			if (leggingsLore != null) {
				leggingsMeta.setLore(leggingsLore);
			}
			if (leggingsEnchantments != null) {
				leggingsEnchantments.forEach(enchantWithLVL -> {
					String[] enchantSplitter = enchantWithLVL.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(enchantSplitter[0]);
					int lvl = Integer.parseInt(enchantSplitter[1]);
					leggingsMeta.addEnchant(enchant, lvl, true);
				});
			}
			if (leggingsFlags != null) {
				leggingsFlags.forEach(flag -> {
					leggingsMeta.addItemFlags(ItemFlagReader.matchItemFlag(flag));
				});
			}
			leggingsStack.setItemMeta(leggingsMeta);
			if (plugin.isDisableKitDrops() || plugin.isDisableKitDropsOnDeath())
				leggingsStack = NBTEditor.set(leggingsStack, true, "autopvpkit");
			kit.setLeggings(leggingsStack);
			// BOOTS
			ItemStack bootsStack = null;
			String bootsItem = kitsPath.getString(kitName + ".boots.item");
			int bootsAmount = kitsPath.getInt(kitName + ".boots.amount");
			short bootsData = (short) kitsPath.getInt(kitName + ".boots.data");
			bootsStack = new ItemStack(material(bootsItem), bootsAmount, bootsData);
			ItemMeta bootsMeta = bootsStack.getItemMeta();
			String bootsName = kitsPath.isSet(kitName + ".boots.name")
					? colorize(kitsPath.getString(kitName + ".boots.name")) : null;
			List<String> bootsLore = kitsPath.isSet(kitName + ".boots.lore")
					? colorize(kitsPath.getStringList(kitName + ".boots.lore")) : null;
			List<String> bootsEnchantments = kitsPath.isSet(kitName + ".boots.enchantments")
					? kitsPath.getStringList(kitName + ".boots.enchantments") : null;
			List<String> bootsFlags = kitsPath.isSet(kitName + ".boots.flags")
					? kitsPath.getStringList(kitName + ".boots.flags") : null;
			if (bootsName != null) {
				bootsMeta.setDisplayName(bootsName);
			}
			if (bootsLore != null) {
				bootsMeta.setLore(bootsLore);
			}
			if (bootsEnchantments != null) {
				bootsEnchantments.forEach(enchantWithLVL -> {
					String[] enchantSplitter = enchantWithLVL.split(" ");
					Enchantment enchant = EnchantmentReader.matchEnchantment(enchantSplitter[0]);
					int lvl = Integer.parseInt(enchantSplitter[1]);
					bootsMeta.addEnchant(enchant, lvl, true);
				});
			}
			if (bootsFlags != null) {
				bootsFlags.forEach(flag -> {
					bootsMeta.addItemFlags(ItemFlagReader.matchItemFlag(flag));
				});
			}
			bootsStack.setItemMeta(bootsMeta);
			if (plugin.isDisableKitDrops() || plugin.isDisableKitDropsOnDeath())
				bootsStack = NBTEditor.set(bootsStack, true, "autopvpkit");
			kit.setBoots(bootsStack);
			ConfigurationSection itemsPath = kitsPath.getConfigurationSection(kitName);
			itemsPath.getKeys(false).forEach(itemSlot -> {
				if (itemSlot.length() < 4) {
					ItemStack normalStack = null;
					String normalItem = kitsPath.getString(kitName + "." + itemSlot + ".item");
					int normalAmount = kitsPath.getInt(kitName + "." + itemSlot + ".amount");
					short normalData = (short) kitsPath.getInt(kitName + "." + itemSlot + ".data");
					normalStack = new ItemStack(material(normalItem), normalAmount, normalData);
					ItemMeta normalMeta = normalStack.getItemMeta();
					String normalName = kitsPath.isSet(kitName + "." + itemSlot + ".name")
							? colorize(kitsPath.getString(kitName + "." + itemSlot + ".name")) : null;
					List<String> normalLore = kitsPath.isSet(kitName + "." + itemSlot + ".lore")
							? colorize(kitsPath.getStringList(kitName + "." + itemSlot + ".lore")) : null;
					List<String> normalEnchantments = kitsPath.isSet(kitName + "." + itemSlot + ".enchantments")
							? kitsPath.getStringList(kitName + "." + itemSlot + ".enchantments") : null;
					List<String> normalFlags = kitsPath.isSet(kitName + "." + itemSlot + ".flags")
							? kitsPath.getStringList(kitName + "." + itemSlot + ".flags") : null;
					if (normalName != null) {
						normalMeta.setDisplayName(normalName);
					}
					if (normalLore != null) {
						normalMeta.setLore(normalLore);
					}
					if (normalEnchantments != null) {
						normalEnchantments.forEach(enchantWithLVL -> {
							String[] enchantSplitter = enchantWithLVL.split(" ");
							Enchantment enchant = EnchantmentReader.matchEnchantment(enchantSplitter[0]);
							int lvl = Integer.parseInt(enchantSplitter[1]);
							normalMeta.addEnchant(enchant, lvl, true);
						});
					}
					if (normalFlags != null) {
						normalFlags.forEach(flag -> {
							normalMeta.addItemFlags(ItemFlagReader.matchItemFlag(flag));
						});
					}
					normalStack.setItemMeta(normalMeta);
					if (plugin.isDisableKitDrops() || plugin.isDisableKitDropsOnDeath())
						normalStack = NBTEditor.set(normalStack, true, "autopvpkit");
					kit.addItem(Integer.parseInt(itemSlot), normalStack);
				}
			});
			this.kits.put(kitName, kit);
		});

	}

	public Map<String, Kit> getKits() {
		return this.kits;
	}

	public Kit getKit(String name) {
		return this.kits.get(name);
	}

}
