package unsw.goals;

import java.util.Set;
import java.util.HashSet;

import unsw.components.Entity;
import unsw.components.HealthComponent;

public class EnemiesGoalManager implements IGoalManager {
	private Set<Entity> aliveEnemies = new HashSet<>();

	public boolean isGoalCompleted () {
		return this.aliveEnemies.isEmpty();
	}
	
	public void addEnemy (Entity enemy) {
		this.aliveEnemies.add(enemy);
		HealthComponent health =
			enemy.<HealthComponent>getComponent(HealthComponent.class);
		health.addObserver((Integer hits) -> {
			if (hits <= 0) this.aliveEnemies.remove(enemy);
		});
	}
}
