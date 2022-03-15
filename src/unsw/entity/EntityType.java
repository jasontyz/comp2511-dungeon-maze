package unsw.entity;

public enum EntityType {
	PLAYER,
	WALL,
	EXIT,
	TREASURE,
	DOOR,
	KEY,
	BOULDER,
	SWITCH,
	PORTAL,
	ENEMY,
	SWORD,
	INVINCIBILITY,
	TEST;

	public static EntityType from (String value) {
		return valueOf(value.toUpperCase());
	}
};
