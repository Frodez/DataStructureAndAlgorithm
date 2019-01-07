package datastructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SequentList<T extends Comparable<T>>
	implements Serializable, Iterable<T>, List<T>, Sortable<T> {

	private static final long serialVersionUID = 1L;

	private static final int BASE_CAPACITY = 15;

	private static final int MAX_CAPACITY = 0x7fffffff;

	private int size = 0;

	private int ensurePercent = 75;

	private T[] array;

	@SuppressWarnings("unchecked")
	public SequentList() {
		array = (T[]) new Comparable[BASE_CAPACITY];
	}

	@SuppressWarnings("unchecked")
	public SequentList(int capacity) {
		array = (T[]) new Comparable[capacity];
	}

	@SuppressWarnings("unchecked")
	public SequentList(int capacity, int ensurePercent) {
		array = (T[]) new Comparable[capacity];
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

	private void checkAdd(int... array) {
		int check = MAX_CAPACITY;
		for (int i = 0; i < array.length; i++) {
			check = check - array[i];
			if (check < 0) {
				throw new IndexOutOfBoundsException();
			}
		}
	}

	/**
	 * 判断索引值是否非法,非法抛出异常
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private void checkLegal(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
	}

	/**
	 * 检查左闭右开区间合法性
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private void checkInterval(int start, int end) {
		if (isNotLegal(start)) {
			throw new IndexOutOfBoundsException();
		}
		if (isNotLegal(end - 1)) {
			throw new IndexOutOfBoundsException();
		}
		if (end < start) {
			throw new RuntimeException();
		}
	}

	private void insert(int position, T data) {
		ensureCapacity(size + 1);
		for (int i = size - 1; i >= position; i--) {
			array[i + 1] = array[i];
		}
		array[position] = data;
		size++;
	}

	private void batchInsert(int position, T[] data) {
		ensureCapacity(size + data.length);
		for (int i = size - 1; i >= position; i--) {
			array[i + data.length] = array[i];
		}
		for (int i = position; i < position + data.length; i++) {
			array[i] = data[i - position];
		}
		size = size + data.length;
	}

	private T delete(int position) {
		T data = array[position];
		for (int i = position; i < size - 1; i++) {
			array[i] = array[i + 1];
		}
		size--;
		return data;
	}

	private T delete(Object data) {
		return delete(data, 0, size);
	}

	private T delete(Object data, int start, int end) {
		if (data != null) {
			for (int i = start; i < end; i++) {
				if (data.equals(array[i])) {
					return delete(i);
				}
			}
		} else {
			for (int i = start; i < end; i++) {
				if (array[i] == null) {
					return delete(i);
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private void batchDelete(int start, int end, T[] data) {
		boolean[] status = new boolean[size];
		for (int i = 0; i < data.length; i++) {
			int index = indexOfWithoutCheck(data[i], start, end);
			if (index != -1) {
				status[index] = true;
			}
		}
		T[] newArray = (T[]) new Object[array.length];
		int count = 0;
		for (int i = 0; i < size; i++) {
			if (status[i] == false) {
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
		batchInsert(size, data);
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
		if (start >= end) {
			throw new RuntimeException();
		}
		return indexOfWithoutCheck(data, start, end);
	}

	private int indexOfWithoutCheck(Object data, int start, int end) {
		if (data != null) {
			for (int i = start; i < end; i++) {
				if (data.equals(array[i])) {
					return i;
				}
			}
		} else {
			for (int i = start; i < end; i++) {
				if (array[i] == null) {
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
		if (start >= end) {
			throw new RuntimeException();
		}
		return lastIndexOfWithoutCheck(data, start, end);
	}

	private int lastIndexOfWithoutCheck(Object data, int start, int end) {
		if (data != null) {
			for (int i = end - 1; i >= start; i--) {
				if (data.equals(array[i])) {
					return i;
				}
			}
		} else {
			for (int i = end - 1; i >= start; i--) {
				if (array[i] == null) {
					return i;
				}
			}
		}
		return -1;
	}

	private boolean ensureCapacity(int size) {
		if (size * ensurePercent > this.array.length * 100) {
			expand();
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void expand() {
		checkAdd(array.length, array.length, 1);
		T[] temp = (T[]) new Comparable[array.length * 2 + 1];
		for (int i = 0; i < array.length; i++) {
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
		for (int i = 0; i < size; i++) {
			result[i] = array[i];
		}
		return result;
	}

	@Override
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T[] toArray(T[] a) {
		if (a == null || a.length == 0) {
			return (T[]) new Object[] {};
		}
		for (int i = 0; i < a.length; i++) {
			if (i < size) {
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
			if (indexOf(iterator.next()) == -1) {
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
		for (int i = 0; i < size; i++) {
			if (collection.contains(array[i])) {
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
		for (int i = start; i < end; i++) {
			newList.add(array[i]);
		}
		return newList;
	}

	public void reverse() {
		if (size != 0) {
			for (int i = 0; i < (size + 1) / 2; i++) {
				T temp = array[i];
				array[i] = array[size - 1 - i];
				array[size - 1 - i] = temp;
			}
		}
	}

	private class SequentListIterator implements ListIterator<T> {

		private int index;

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
			if (!hasNext()) {
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
			if (!hasPrevious()) {
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

	/**
	 * 冒泡排序
	 * @author Frodez
	 * @date 2019-01-07
	 */
	@Override
	public void bubbleSort() {
		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				if (array[i].compareTo(array[j]) > 0) {
					T temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
	}

	/**
	 * 插入排序
	 * @author Frodez
	 * @date 2019-01-07
	 */
	@Override
	public void insertSort() {
		// 已排序序列的后面一个的位置
		int sortedIndex = 0;
		// 遍历全序列
		for (int i = 0; i < size; i++) {
			// i和sortedIndex永远相等
			int j = 0;
			// 遍历已排序序列(此序列从小到大排列)
			for (; j < sortedIndex; j++) {
				// 如果遇到第一个比自己大的元素
				if (array[j].compareTo(array[i]) > 0) {
					T temp = array[sortedIndex];
					// 将此元素以及之后已排序的元素全部向后移动一格
					for (int k = sortedIndex; k > j; k--) {
						array[k] = array[k - 1];
					}
					// 将自己放入此元素原位置
					array[j] = temp;
					// 已排序序列向后延伸一位
					sortedIndex++;
					break;
				}
			}
			if (j == sortedIndex) {
				// 说明已排序序列中所有元素均小于自己
				T temp = array[sortedIndex];
				array[sortedIndex] = array[i];
				array[i] = temp;
				// 已排序序列向后延伸一位
				sortedIndex++;
			}
		}
	}

	/**
	 * 选择排序
	 * @author Frodez
	 * @date 2019-01-07
	 */
	@Override
	public void selectSort() {
		// 已排序序列的后面一个的位置
		int sortedIndex = 0;
		for (int i = 0; i < size; i++) {
			T min = array[i];
			int minIndex = i;
			for (int j = sortedIndex; j < size; j++) {
				if (array[j].compareTo(min) < 0) {
					// 找到最小位置和值,默认为外层遍历当前位置和当前值
					min = array[j];
					minIndex = j;
				}
			}
			// 将其与已排序序列后面一个交换
			array[minIndex] = array[sortedIndex];
			array[sortedIndex] = min;
			// 排序序列长度加1
			sortedIndex++;
		}
	}

	/**
	 * 归并排序
	 * @author Frodez
	 * @date 2019-01-07
	 */
	@Override
	public void mergeSort() {
		mergeSort(array, 0, size);
	}

	@SuppressWarnings("unchecked")
	private void mergeSort(T[] array, int start, int end) {
		// 如果抵达递归终点
		if (end - start <= 2) {
			if (end == start + 2) {
				if (array[start].compareTo(array[start + 1]) > 0) {
					T temp = array[start];
					array[start] = array[start + 1];
					array[start + 1] = temp;
				}
			}
			return;
		}
		// 将序列两等分递归,分别排序
		int middle = (start + end) / 2;
		mergeSort(array, start, middle);
		mergeSort(array, middle, end);
		// 分别排序完后,进行归并
		int beforeIndex = 0;
		int afterIndex = 0;
		int beforeLength = middle - start;
		int afterLength = end - middle;
		T[] beforeArray = (T[]) new Comparable[beforeLength];
		T[] afterArray = (T[]) new Comparable[afterLength];
		for (int i = start; i < middle; i++) {
			beforeArray[i - start] = array[i];
		}
		for (int i = middle; i < end; i++) {
			afterArray[i - middle] = array[i];
		}
		for (int i = start; i < end; i++) {
			if (beforeIndex != beforeLength && afterIndex != afterLength) {
				if (beforeArray[beforeIndex].compareTo(afterArray[afterIndex]) <= 0) {
					array[i] = beforeArray[beforeIndex];
					beforeIndex++;
					continue;
				} else {
					array[i] = afterArray[afterIndex];
					afterIndex++;
					continue;
				}
			}
			if (beforeIndex != beforeLength && afterIndex == afterLength) {
				array[i] = beforeArray[beforeIndex];
				beforeIndex++;
				continue;
			}
			if (beforeIndex == beforeLength && afterIndex != afterLength) {
				array[i] = afterArray[afterIndex];
				afterIndex++;
				continue;
			}
		}
	}

	/**
	 * 快速排序
	 * @author Frodez
	 * @date 2019-01-07
	 */
	@Override
	public void quickSort() {
		quickSort(array, 0, size);
	}

	private void quickSort(T[] array, int start, int end) {
		// 已排序部分中,最后一个小元素的index,默认为start
		int smallerIndex = start;
		// 已排序部分中,最后一个相等元素的index,默认为start
		int equationIndex = start;
		// 已排序部分中,最前一个大元素的index,默认为end
		int largerIndex = end;
		// 以第一个值作为example
		T example = array[start];
		// 对[start + 1, end)部分进行遍历
		for (int i = start + 1; i < end;) {
			T temp = array[i];
			if (temp.compareTo(example) < 0) {
				// 小于example时,将其与已排序部分中小元素之后第一个元素交换
				// smallerIndex和equationIndex均+1.遍历下个元素.
				array[i] = array[smallerIndex + 1];
				array[smallerIndex + 1] = temp;
				smallerIndex++;
				equationIndex++;
				i++;
			} else if (temp.compareTo(example) == 0) {
				// 等于example时,equation+1,遍历下个元素.
				equationIndex++;
				i++;
			} else {
				// 大于example时,将其与已排序部分中大元素之前第一个元素交换
				// largerIndex+1,但仍遍历该位置元素,直到不大于example为止.
				array[i] = array[largerIndex - 1];
				array[largerIndex - 1] = temp;
				largerIndex--;
			}
			if (equationIndex == largerIndex - 1) {
				// 如果largerIndex正好比equationIndex大1,说明已经碰到一起了,本次遍历完毕.
				break;
			}
		}
		// 将start处元素与已排序部分中小元素最后一个交换位置,smallerIndex减1.
		array[start] = array[smallerIndex];
		array[smallerIndex] = example;
		smallerIndex--;
		// 如果本次排序的所有值均相等,说明这一部分的元素已经完成了排序.
		if (equationIndex == end) {
			return;
		}
		quickSort(array, start, smallerIndex + 1);
		quickSort(array, largerIndex, end);
	}

	/**
	 * toString方法,null值会转换为"null"
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public String toString() {
		return toString("null");
	}

	/**
	 * toString方法,null值会按默认字符串转换
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public String toString(String defaultString) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < size; i++) {
			T data = array[i];
			if (data == null) {
				sb.append(defaultString);
			} else {
				sb.append(data.toString());
			}
			if (i != size - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

}
