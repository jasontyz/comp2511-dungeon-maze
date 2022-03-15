package unsw.observable;

public interface IObservable<T> {
	public void addObserver (IObserver<T> observer);
	public void removeObserver (IObserver<T> observer);
	public void notifyObservers (T data);
}


