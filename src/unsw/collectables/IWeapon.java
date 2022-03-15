package unsw.collectables;

import unsw.components.HealthComponent;
import unsw.observable.*;

public interface IWeapon extends IObservable<Integer> {
	 public void attack (HealthComponent health);
	 public default void activate () {};
}
