package datastructure;

import java.beans.DefaultPersistenceDelegate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class ArrayQueue<T> implements Queue<T> {
	
	private static final int BASE_CAPACITY = 10;

	private int size = 0;
	
	private T[] array;
	
	@SuppressWarnings("unchecked")
	public ArrayQueue() {
		array = (T[]) new Object[BASE_CAPACITY];
	}
	
	@SuppressWarnings("unchecked")
	public ArrayQueue(int capacity) {
		array = (T[]) new Object[capacity];
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	private void insert(int position, T data) {
		for(int i = size - 1; i >= position; i--) {
			array[i + 1] = array[i];
		}
		array[position] = data;
		size++;
	}
	
	private void batchInsert(int position, T[] data) {
		for(int i = size - 1; i >= position; i--) {
			array[i + data.length] = array[i];
		}
		for(int i = position; i < size; i++) {
			array[i] = data[i - position];
		}
		size = size + data.length;
	}

	private T delete(int position) {
		T data = array[position];
		for(int i = position; i < size - 1; i++) {
			array[i] = array[i + 1];
		}
		size--;
		return data;
	}
	
	private T delete(Object data) {
		return delete(data, 0, size);
	}
	
	private T delete(Object data, int start, int end) {
		if(data != null) {
			for(int i = start; i < end; i++) {
				if(data.equals(array[i])) {
					return delete(i);
				}
			}			
		} else {
			for(int i = start; i < end; i++) {
				if(array[i] == null) {
					return delete(i);
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private void batchDelete(int start, int end, T[] data) {
		boolean[] status = new boolean[size];
		for(int i = 0; i < data.length; i++) {
			int index = indexOfWithoutCheck(data[i], start, end);
			if(index != -1) {
				status[index] = true;
			}
		}
		T[] newArray = (T[]) new Object[array.length];
		int count = 0;
		for(int i = 0; i < size; i++) {
			if(status[i] == false) {
				newArray[count] = array[i];
				count++;
			}
		}
		size = count;
		array = newArray;
	}

	private T update(int position, T data) {
		T result = array[position];
		array[position] = data;
		return result;
	}
	
	private int indexOfWithoutCheck(Object data, int start, int end) {
		if(data != null) {
			for(int i = start; i < end; i++) {
				if(data.equals(array[i])) {
					return i;
				}
			}			
		} else {
			for(int i = start; i < end; i++) {
				if(array[i] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	@Override
	public boolean contains(Object data) {
		if(data != null) {
			for(int i = 0; i < size; i++) {
				if(data.equals(array[i])) {
					return true;
				}
			}
		} else {
			for(int i = 0; i < size; i++) {
				if(array[i] == null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		for(int i = 0; i < size; i++) {
			result[i] = array[i];
		}
		return result;
	}

	@Override
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T[] toArray(T[] a) {
		if(a == null || a.length == 0) {
			return (T[]) new Object[] {};
		}
		for(int i = 0; i < a.length; i++) {
			if(i < size) {
				a[i] = (T) array[i];
			} else {
				a[i] = null;
			}
		}
		return null;
	}

	@Override
	public boolean remove(Object data) {
		return delete(data) != null;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		Iterator<?> iterator = collection.iterator();
		while (iterator.hasNext()) {
			if(indexOfWithoutCheck(iterator.next(), 0, size) == -1) {
				return false;
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean addAll(Collection<? extends T> collection) {
		if(collection.size() > array.length - size) {
			throw new IllegalStateException();
		}
		batchInsert(size, (T[]) collection.toArray());
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> collection) {
		batchDelete(0, size, (T[]) collection.toArray());
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean retainAll(Collection<?> collection) {
		T[] newArray = (T[]) new Object[array.length];
		int count = 0;
		for(int i = 0; i < size; i++) {
			if(collection.contains(array[i])) {
				newArray[count] = array[i];
				count++;
			}
		}
		array = newArray;
		size = count;
		return false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void clear() {
		array = (T[]) new Object[] {};
		size = 0;
	}

	@Override
	public boolean add(T e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean offer(T e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public T remove() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T poll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T element() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T peek() {
		// TODO Auto-generated method stub
		return null;
	}

}
