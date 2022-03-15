package unsw.components;

import unsw.observable.*;

abstract class ObservableComponent<T>
	extends Component
	implements IObservable<T> {

	IObservable<T> observable = new Observable<>();

	ObservableComponent (Entity entity) {
		super(entity);
	}

	// -------------------------------------------------------------

	public void addObserver (IObserver<T> observer) {
		this.observable.addObserver(observer);
	}

	public void removeObserver (IObserver<T> observer) {
		this.observable.removeObserver(observer);
	}

	public void notifyObservers (T data) {
		this.observable.notifyObservers(data);
	}
}
