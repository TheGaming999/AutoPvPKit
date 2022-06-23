package me.autopvpkit.data;

import org.bukkit.enchantments.Enchantment;

import me.autopvpkit.utils.XEnchantment;

public class EnchantmentReader {

	private static String parseEnchantment(String enchant) {
		return XEnchantment.matchXEnchantment(enchant.toUpperCase()).get().getEnchant().getName();
	}

	public static String matchStringEnchantment(String enchant) {
		return parseEnchantment(enchant);
	}

	public static Enchantment matchEnchantment(String enchant) {
		return XEnchantment.matchXEnchantment(enchant.toUpperCase()).get().getEnchant();
	}
}
