package me.autopvpkit.events;

public enum ChangeReason {
	COMMAND, COMMAND_BY_OTHER, JOIN, WORLD_CHANGE, RESPAWN, REGION_ENTRY, UNKNOWN;

	public ChangeReason getChangeReason() {
		return this;
	}

}
