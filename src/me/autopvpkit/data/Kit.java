package me.autopvpkit.data;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class Kit {

	private String name;
	private String displayName;
	private ItemStack helmet;
	private ItemStack chestplate;
	private ItemStack leggings;
	private ItemStack boots;
	private Map<Integer, ItemStack> items;
	
	public Kit(String name) {this.name = name; this.items = new HashMap<>();}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public String getDisplayName() {
		return this.displayName;
	}
	
	public void setHelmet(ItemStack itemstack) {
		this.helmet = itemstack;
	}
	
	public ItemStack getHelmet() {
		return this.helmet;
	}
	
	public void setChestplate(ItemStack itemstack) {
		this.chestplate = itemstack;
	}
	
	public ItemStack getChestplate() {
		return this.chestplate;
	}
	
	public void setLeggings(ItemStack itemstack) {
		this.leggings = itemstack;
	}
	
	public ItemStack getLeggings() {
		return this.leggings;
	}
	
	public void setBoots(ItemStack itemstack) {
		this.boots = itemstack;
	}
	
	public ItemStack getBoots() {
		return this.boots;
	}
	
	public void setItems(Map<Integer, ItemStack> items) {
		this.items = items;
	}
	
	public Map<Integer, ItemStack> getItems() {
		return this.items;
	}
	
	public ItemStack addItem(int slot, ItemStack itemstack) {
		return items.put(slot, itemstack);
	}
	
	public ItemStack delItem(int slot, ItemStack itemstack) {
		return items.remove(slot);
	}
	
	public void clearItems() {
		this.items.clear();
	}
	
	
}
