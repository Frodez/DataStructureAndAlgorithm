package datastructure;

import java.io.Serializable;
import java.util.NoSuchElementException;

public class ArrayStack<T extends Comparable<T>> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final int BASE_CAPACITY = 15;

	private static final int MAX_CAPACITY = 0x7fffffff;

	private int size = 0;

	private T[] array;

	private boolean permitResize = false;

	@SuppressWarnings("unchecked")
	public ArrayStack() {
		array = (T[]) new Object[BASE_CAPACITY];
	}

	@SuppressWarnings("unchecked")
	public ArrayStack(int capacity) {
		array = (T[]) new Object[capacity];
	}

	@SuppressWarnings("unchecked")
	public ArrayStack(int capacity, boolean permitExpend) {
		array = (T[]) new Object[capacity];
		this.permitResize = permitExpend;
	}

	public int size() {
		return size;
	}

	public int capacity() {
		return array.length;
	}

	public boolean isFull() {
		return size == array.length;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	private void checkAdd(int... array) {
		int check = MAX_CAPACITY;
		for(int i = 0; i < array.length; i++) {
			check = check - array[i];
			if(check < 0) {
				throw new IndexOutOfBoundsException();
			}
		}
	}

	private void insert(T data) {
		array[size] = data;
		size++;
	}

	private T delete() {
		T result = array[size];
		size--;
		return result;
	}

	private T select() {
		return array[size - 1];
	}

	public boolean ensureCapacity() {
		checkAdd(array.length, array.length, 1);
		return ensureCapacity(array.length * 2 + 1);
	}

	@SuppressWarnings("unchecked")
	public boolean ensureCapacity(int capacity) {
		if(!permitResize) {
			return false;
		}
		if(capacity < size) {
			return false;
		}
		T[] newArray = (T[]) new Object[capacity];
		for(int i = 0; i < size; i++) {
			newArray[i] = array[i];
		}
		array = newArray;
		return true;
	}

	public T peek() {
		if(size == 0) {
			return null;
		}
		return select();
	}

	public T element() {
		if(size == 0) {
			throw new NoSuchElementException();
		}
		return select();
	}

	public T pop() {
		if(size == 0) {
			return null;
		}
		return delete();
	}

	public T remove() {
		if(size == 0) {
			throw new NoSuchElementException();
		}
		return delete();
	}

	public boolean push(T data) {
		if(size == array.length) {
			return false;
		}
		insert(data);
		return true;
	}

	public boolean add(T data) {
		if(size == array.length) {
			throw new IllegalStateException();
		}
		insert(data);
		return true;
	}

	public int minimumDepth(Object data) {
		if(data == null) {
			for(int i = size - 1; i >= 0; i--) {
				if(array[i] == null) {
					return size - i;
				}
			}
			return size;
		} else {
			for(int i = size - 1; i >= 0; i--) {
				if(data.equals(array[i])) {
					return size - i;
				}
			}
			return size;
		}
	}

	public int minimumHeight(Object data) {
		if(data == null) {
			for(int i = 0; i < size; i++) {
				if(array[i] == null) {
					return i + 1;
				}
			}
			return size;
		} else {
			for(int i = 0; i < size; i++) {
				if(data.equals(array[i])) {
					return i + 1;
				}
			}
			return size;
		}
	}

}
