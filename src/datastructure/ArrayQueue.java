package datastructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

public class ArrayQueue<T extends Comparable<T>> implements Serializable, Queue<T> {

	private static final long serialVersionUID = 1L;

	private static final int BASE_CAPACITY = 15;

	private static final int MAX_CAPACITY = 0x7fffffff;

	private int size = 0;

	private int start = 0;

	private int end = 0;

	private T[] array;

	private boolean permitResize = false;

	@SuppressWarnings("unchecked")
	public ArrayQueue() {
		array = (T[]) new Object[BASE_CAPACITY];
	}

	@SuppressWarnings("unchecked")
	public ArrayQueue(int capacity) {
		array = (T[]) new Object[capacity];
	}

	@SuppressWarnings("unchecked")
	public ArrayQueue(int capacity, boolean permitExpend) {
		array = (T[]) new Object[capacity];
		this.permitResize = permitExpend;
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

	private void checkAdd(int... array) {
		int check = MAX_CAPACITY;
		for(int i = 0; i < array.length; i++) {
			check = check - array[i];
			if(check < 0) {
				throw new IndexOutOfBoundsException();
			}
		}
	}

	/**
	 * 获取队列的下标
	 * @return
	 */
	private int getEnd() {
		if(start < end || start == end && size == 0) {
			return end;
		}
		return end + array.length;
	}

	private void insert(T data) {
		array[end] = data;
		end = (end + 1) % array.length;
	}

	private T delete() {
		T result = array[start];
		start = (start + 1) % array.length;
		return result;
	}

	private T select() {
		if(end == 0) {
			return array[array.length - 1];
		} else {
			return array[end - 1];
		}
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
		for(int i = start; i < getEnd(); i++) {
			newArray[i % newArray.length] = array[i % array.length];
		}
		array = newArray;
		return true;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object data) {
		if(data != null) {
			for(int i = start; i < getEnd(); i++) {
				if(data.equals(array[i % array.length])) {
					return true;
				}
			}
		} else {
			for(int i = start; i < getEnd(); i++) {
				if(array[i % array.length] == null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[size];
		for(int i = start; i < getEnd(); i++) {
			result[i] = array[i % array.length];
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
				a[i] = (T) array[i + start % array.length];
			} else {
				a[i] = null;
			}
		}
		return a;
	}

	/**
	 * 不要这个
	 * @author Frodez
	 * @date 2019-01-05
	 */
	@Override
	public boolean remove(Object data) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		Iterator<?> iterator = collection.iterator();
		int end = getEnd();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			int i = start;
			if(next == null) {
				for(; i < end; i++) {
					if(array[i % array.length] == null) {
						break;
					}
				}
			} else {
				for(; i < end; i++) {
					if(next.equals(array[i % array.length])) {
						break;
					}
				}
			}
			if(i != end) {
				return false;
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean addAll(Collection<? extends T> collection) {
		if(permitResize) {
			checkAdd(size, collection.size());
			int newSize = size + collection.size();
			ensureCapacity(newSize < array.length ? array.length : newSize);
		} else {
			if(size + collection.size() > array.length) {
				throw new IndexOutOfBoundsException();
			}
		}
		Iterator<?> iterator = collection.iterator();
		while (iterator.hasNext()) {
			insert((T) iterator.next());
		}
		return true;
	}

	/**
	 * 不要这个
	 * @author Frodez
	 * @date 2019-01-05
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	/**
	 * 不要这个
	 * @author Frodez
	 * @date 2019-01-05
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {
		start = 0;
		end = 0;
		size = 0;
	}

	@Override
	public boolean add(T data) {
		if(size == array.length) {
			throw new IllegalStateException();
		}
		insert(data);
		return true;
	}

	@Override
	public boolean offer(T data) {
		if(size == array.length) {
			return false;
		}
		insert(data);
		return true;
	}

	@Override
	public T remove() {
		if(size == 0) {
			throw new NoSuchElementException();
		}
		return delete();
	}

	@Override
	public T poll() {
		if(size == 0) {
			return null;
		}
		return delete();
	}

	@Override
	public T element() {
		if(size == 0) {
			throw new NoSuchElementException();
		}
		return select();
	}

	@Override
	public T peek() {
		if(size == 0) {
			return null;
		}
		return select();
	}

	@Override
	public Iterator<T> iterator() {
		return new QueueIterator();
	}

	public class QueueIterator implements Iterator<T> {

		private int index;

		public QueueIterator() {
			index = ArrayQueue.this.start;
		}

		@Override
		public boolean hasNext() {
			return index != ArrayQueue.this.end;
		}

		@Override
		public T next() {
			T result = array[index];
			index = (index + 1) % array.length;
			return result;
		}

	}

}
