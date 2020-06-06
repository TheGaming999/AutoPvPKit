package me.autopvpkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.autopvpkit.data.Kit;

public class PlayerKitSaveEvent extends Event implements Cancellable {

	private boolean cancel;
	private Player player;
	private Kit kit;
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerKitSaveEvent(Player player, Kit kit) {
		this.setPlayer(player);
		this.setKit(kit);
	}
	
	public boolean isCancelled() {
		return cancel;
	}

	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	public HandlerList getHandlers() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Kit getKit() {
		return kit;
	}

	public void setKit(Kit kit) {
		this.kit = kit;
	}

}
