package unsw.components;

import unsw.observable.*;

public class HealthComponent extends ObservableComponent<Integer> {
	
	private int hits;
	
	public HealthComponent (Entity entity, int hits) {
		super(entity);
		this.hits = hits;
	}
	
	public boolean isAlive() {
		return this.hits > 0;
	}

	public void hit(int damage) {
		this.hits -= damage;
		if (this.hits <= 0) this.die();
		this.notifyObservers(this.hits);
	}
	
	public void die() {
		this.hits = 0;
		this.notifyObservers(this.hits);
	}

	int getHits () {
		return this.hits;
	}
}
