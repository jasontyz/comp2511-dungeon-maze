package unsw.observable;

import java.util.Set;
import java.util.HashSet;

public class Observable<T> implements IObservable<T> {

	Set<IObserver<T>> observers = new HashSet<>();

	// -------------------------------------------------------------
	
	public void addObserver (IObserver<T> observer) {
		this.observers.add(observer);
	}

	public void removeObserver (IObserver<T> observer) {
		this.observers.remove(observer);
	}

	public void notifyObservers (T data) {
		for (IObserver<T> observer : this.observers)
			observer.<T>update(data);
	}
}
