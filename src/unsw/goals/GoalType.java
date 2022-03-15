package unsw.goals;

public enum GoalType {
	EXIT, ENEMIES, BOULDERS, TREASURE;

	public static GoalType from (String value) {
		return GoalType.valueOf(value.toUpperCase());
	}
}
