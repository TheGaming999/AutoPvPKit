package me.autopvpkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerKitChangeEvent extends Event implements Cancellable {

	private boolean cancel;
	private Player player;
	private ChangeReason changeReason;
	private static final HandlerList handlers = new HandlerList();

	public PlayerKitChangeEvent(Player player, ChangeReason changeReason) {
		this.setPlayer(player);
		this.setChangeReason(changeReason);
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public ChangeReason getChangeReason() {
		return changeReason;
	}

	public void setChangeReason(ChangeReason changeReason) {
		this.changeReason = changeReason;
	}

}
