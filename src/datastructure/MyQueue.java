package datastructure;

public class MyQueue<T> {

	private LinkList<T> list = new LinkList<>();

	private int size = 0;

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public boolean offer(T data) {
		return true;
	}

}
