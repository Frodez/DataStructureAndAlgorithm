package datastructure;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SequentList<T> implements Iterable<T>, List<T> {

	private static final int BASE_CAPACITY = 10;

	private int size = 0;

	private int ensurePercent = 75;

	private T[] array;

	@SuppressWarnings("unchecked")
	public SequentList() {
		array = (T[]) new Object[BASE_CAPACITY];
	}

	@SuppressWarnings("unchecked")
	public SequentList(int capacity) {
		array = (T[]) new Object[capacity];
	}
	
	@SuppressWarnings("unchecked")
	public SequentList(int capacity, int ensurePercent) {
		array = (T[]) new Object[capacity];
		this.ensurePercent = ensurePercent;
	}

	/**
	 * 获得当前尺寸
	 * @author Frodez
	 * @date 2018-12-31
	 */
	@Override
	public int size() {
		return size;
	}

	/**
	 * 判断是否为空
	 * @author Frodez
	 * @date 2018-12-31
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 判断索引值是否合法
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public boolean isLegal(int index) {
		return index >= 0 && index < size;
	}

	/**
	 * 判断索引值是否非法
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public boolean isNotLegal(int index) {
		return index < 0 || index >= size;
	}

	/**
	 * 判断索引值是否非法,非法抛出异常
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private void checkLegal(int index) {
		if(index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * 检查左闭右开区间合法性
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private void checkInterval(int start, int end) {
		if(isNotLegal(start)) {
			throw new IndexOutOfBoundsException();
		}
		if(isNotLegal(end - 1)) {
			throw new IndexOutOfBoundsException();
		}
		if(end < start) {
			throw new RuntimeException();
		}
	}

	private void insert(int position, T data) {
		ensureCapacity(size + 1);
		for(int i = size - 1; i >= position; i--) {
			array[i + 1] = array[i];
		}
		array[position] = data;
		size++;
	}

	private void batchInsert(int position, T[] data) {
		ensureCapacity(size + data.length);
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

	/**
	 * 在末尾添加元素
	 * @author Frodez
	 * @date 2018-12-31
	 */
	@Override
	public boolean add(T data) {
		insert(size - 1, data);
		return true;
	}

	/**
	 * 在指定位置添加元素,在此位置以及之后的所有元素将会向后移动一格.
	 * @author Frodez
	 * @date 2018-12-31
	 */
	@Override
	public void add(int position, T data) {
		checkLegal(position);
		insert(position, data);
	}

	public void addAll(T[] data) {
		batchInsert(size - 1, data);
	}

	public void addAll(int position, T[] data) {
		checkLegal(position);
		batchInsert(position, data);
	}

	@Override
	public T get(int position) {
		checkLegal(position);
		return array[position];
	}

	@Override
	public T remove(int position) {
		checkLegal(position);
		return delete(position);
	}

	@Override
	public boolean remove(Object data) {
		return delete(data) != null;
	}

	@Override
	public T set(int position, T data) {
		checkLegal(position);
		return update(position, data);
	}

	@Override
	public int indexOf(Object data) {
		return indexOfWithoutCheck(data, 0, size);
	}

	public int indexOf(Object data, int start, int end) {
		checkInterval(start, end);
		return indexOfWithoutCheck(data, start, end);
	}

	public int indexOfSafely(Object data, int start, int end) {
		start = start < 0 ? 0 : start;
		end = end > size ? size : end;
		if(start >= end) {
			throw new RuntimeException();
		}
		return indexOfWithoutCheck(data, start, end);
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
	public int lastIndexOf(Object data) {
		return lastIndexOfWithoutCheck(data, 0, size);
	}

	public int lastIndexOf(Object data, int start, int end) {
		checkInterval(start, end);
		return lastIndexOfWithoutCheck(data, start, end);
	}

	public int lastIndexOfSafely(Object data, int start, int end) {
		start = start < 0 ? 0 : start;
		end = end > size ? size : end;
		if(start >= end) {
			throw new RuntimeException();
		}
		return lastIndexOfWithoutCheck(data, start, end);
	}

	private int lastIndexOfWithoutCheck(Object data, int start, int end) {
		if(data != null) {
			for(int i = end - 1; i >= start; i--) {
				if(data.equals(array[i])) {
					return i;
				}
			}
		} else {
			for(int i = end - 1; i >= start; i--) {
				if(array[i] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	private boolean ensureCapacity(int size) {
		if(size * ensurePercent > this.array.length * 100) {
			expand();
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void expand() {
		T[] temp = (T[]) new Object[array.length * 2];
		for(int i = 0; i < array.length; i++) {
			temp[i] = array[i];
		}
	}

	@Override
	public boolean contains(Object data) {
		return indexOf(data) != -1;
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
		return a;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		Iterator<?> iterator = collection.iterator();
		while (iterator.hasNext()) {
			if(indexOf(iterator.next()) == -1) {
				return false;
			}
		}
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean addAll(Collection<? extends T> collection) {
		addAll((T[]) collection.toArray());
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean addAll(int index, Collection<? extends T> collection) {
		checkLegal(index);
		addAll(index, (T[]) collection.toArray());
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean removeAll(Collection<?> collection) {
		batchDelete(0, size, (T[]) collection.toArray());
		return true;
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
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void clear() {
		array = (T[]) new Object[] {};
		size = 0;
	}

	@Override
	public List<T> subList(int start, int end) {
		SequentList<T> newList = new SequentList<>(end - start);
		for(int i = start; i < end; i++) {
			newList.add(array[i]);
		}
		return newList;
	}

	public void reverse() {
		if(size != 0) {
			for(int i = 0; i < (size + 1) / 2; i++) {
				T temp = array[i];
				array[i] = array[size - 1 - i];
				array[size - 1 - i] = temp;
			}
		}
	}

	private class SequentListIterator implements ListIterator<T> {

		private int index = 0;

		public SequentListIterator() {
		}

		public SequentListIterator(int index) {
			this.index = index;
		}

		@Override
		public boolean hasNext() {
			return index >= size;
		}

		@Override
		public T next() {
			if(!hasNext()) {
				throw new IndexOutOfBoundsException();
			}
			return SequentList.this.array[index++];
		}

		@Override
		public boolean hasPrevious() {
			return index <= 0;
		}

		@Override
		public T previous() {
			if(!hasPrevious()) {
				throw new IndexOutOfBoundsException();
			}
			return SequentList.this.array[--index];
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void remove() {
			SequentList.this.checkLegal(index);
			SequentList.this.delete(index);
		}

		@Override
		public void set(T data) {
			SequentList.this.checkLegal(index);
			SequentList.this.update(index, data);
		}

		@Override
		public void add(T data) {
			SequentList.this.checkLegal(index);
			SequentList.this.insert(index, data);
		}

	}

	@Override
	public Iterator<T> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new SequentListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return new SequentListIterator(index);
	}

}
