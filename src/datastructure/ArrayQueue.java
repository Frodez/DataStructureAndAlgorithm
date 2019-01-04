package datastructure;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class ArrayQueue<T> implements Queue<T> {
	
	private static final int BASE_CAPACITY = 10;

	private int size = 0;
	
	private int start = 0;
	
	private int end = 0;
	
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
	
	public int capacity() {
		return array.length;
	}
	
	public boolean isFull() {
		return size == array.length;
	}
	
	/**
	 * 对新增做合法性判断
	 */
	private void checkAdd() {
		if(size == array.length) {
			throw new IndexOutOfBoundsException("queue is full!");
		}
	}
	
	/**
	 * 获取队列的下标
	 * @return
	 */
	private int getEnd() {
		if(start < end || (start == end && isEmpty())) {
			return end;
		}
		return end + array.length;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object data) {
		if(data != null) {
			for(int i = start; i < getEnd(); i++) {
				if(data.equals(array[i < array.length ? i : i - array.length])) {
					return true;
				}			
			}
		} else {
			for(int i = start; i < getEnd(); i++) {
				if(array[i < array.length ? i : i - array.length] == null) {
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
		for(int i = start; i < getEnd(); i++) {
			result[i] = array[i < array.length ? i : i - array.length];			
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
				int index = i + start;
				a[i] = (T) array[index < array.length ? index : index - array.length];
			} else {
				a[i] = null;
			}
		}
		return a;
	}

	@Override
	public boolean remove(Object data) {
		if(data == null) {
			
		}
		for(int i = start; i < getEnd(); i++) {
			//array[i < array.length ? i : i - array.length];			
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
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
