package datastructure;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;

/**
 * 双向链表实现
 * @author Frodez
 * @date 2018-12-30
 */
public class LinkList<T extends Comparable<T>>
	implements Serializable, Iterable<T>, List<T>, Sortable<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * 链表节点
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private static class Node<T> {

		/**
		 * 数据
		 */
		public T data;

		/**
		 * 前节点
		 */
		public Node<T> prev;

		/**
		 * 后节点
		 */
		public Node<T> next;

		public Node(T data, Node<T> prev, Node<T> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
		}

	}

	/**
	 * 链表尺寸
	 */
	private int size = 0;

	/**
	 * 开始节点
	 */
	private Node<T> startNode;

	/**
	 * 结束节点
	 */
	private Node<T> endNode;

	public LinkList() {
		startNode = new Node<>(null, null, null);
		endNode = new Node<>(null, startNode, null);
		startNode.next = endNode;
	}

	public LinkList(T[] array) {
		startNode = new Node<>(null, null, null);
		endNode = new Node<>(null, null, null);
		batchInsert(array, startNode, endNode);
	}

	/**
	 * 获取链表尺寸
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public int size() {
		return size;
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

	/**
	 * 判断链表尺寸是否为空
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * 判断链表尺寸是否为空
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public static boolean isEmpty(LinkList<?> list) {
		return list == null || list.size == 0;
	}

	/**
	 * 获取索引值为position的节点,position >= 0 && position < this.size<br>
	 * 当链表为空时,返回null<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private Node<T> select(int position) {
		if (size == 0) {
			return null;
		}
		if (position <= size / 2) {
			Node<T> node = startNode.next;
			for (int i = 0; i < position; i++) {
				node = node.next;
			}
			return node;
		} else {
			Node<T> node = endNode;
			for (int i = position; i < size; i++) {
				node = node.prev;
			}
			return node;
		}
	}

	/**
	 * 插入节点
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private Node<T> insert(T data, Node<T> prev, Node<T> next) {
		Node<T> node = new Node<>(data, prev, next);
		prev.next = node;
		next.prev = node;
		size++;
		return node;
	}

	/**
	 * 批量插入节点
	 * @author Frodez
	 * @date 2018-12-31
	 */
	private void batchInsert(T[] data, Node<T> prev, Node<T> next) {
		for (int i = 0; i < data.length; i++) {
			Node<T> node = new Node<>(data[i], prev, null);
			prev.next = node;
			prev = node;
		}
		prev.next = next;
		next.prev = prev;
		size = size + data.length;
	}

	/**
	 * 更新节点的值
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private T update(Node<T> node, T data) {
		T oldData = node.data;
		node.data = data;
		return oldData;
	}

	/**
	 * 删除节点
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private T delete(Node<T> node) {
		T data = node.data;
		node.prev.next = node.next;
		node.next.prev = node.prev;
		size--;
		return data;
	}

	/**
	 * 在链表最后添加值
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public boolean add(T data) {
		insert(data, endNode.prev, endNode);
		return true;
	}

	/**
	 * 在链表最后批量添加值
	 * @author Frodez
	 * @date 2018-12-31
	 */
	public void addAll(T[] data) {
		batchInsert(data, endNode.prev, endNode);
	}

	/**
	 * 在链表索引为position的节点前面批量添加值
	 * @author Frodez
	 * @date 2018-12-31
	 */
	public void addAll(int position, T[] data) {
		Node<T> node = null;
		if (position == size) {
			node = endNode;
		} else {
			checkLegal(position);
			node = select(position);
		}
		batchInsert(data, node.prev, node);
	}

	/**
	 * 在链表索引为position的节点前面添加值
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public void add(int position, T data) {
		Node<T> node = null;
		if (position == size) {
			node = endNode;
		} else {
			checkLegal(position);
			node = select(position);
		}
		insert(data, node.prev, node);
	}

	/**
	 * 获取索引为position的节点的值
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public T get(int position) {
		checkLegal(position);
		return select(position).data;
	}

	/**
	 * 删除索引为position的节点
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public T remove(int position) {
		checkLegal(position);
		return delete(select(position));
	}

	/**
	 * 设置索引为position的节点的值
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public T set(int position, T data) {
		checkLegal(position);
		return update(select(position), data);
	}

	/**
	 * 获取某个值在链表中从前往后的第一个位置,如果未出现返回-1
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public int indexOf(Object data) {
		return indexOfWithoutCheck(data, 0, size);
	}

	/**
	 * 获取某个值在链表区间[start, end)中从前往后的第一个位置,如果未出现返回-1.<br>
	 * start不能小于0且不能大于等于链表尺寸.<br>
	 * end不能小于1且不能大于链表尺寸.<br>
	 * end不能小于start.<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public int indexOf(Object data, int start, int end) {
		checkInterval(start, end);
		return indexOfWithoutCheck(data, start, end);
	}

	/**
	 * 安全地获取某个值在链表区间[start, end)中从前往后的第一个位置,如果未出现返回-1.<br>
	 * start小于0时,会被置为0.<br>
	 * end大于链表尺寸时,会被置为链表尺寸.<br>
	 * end不能小于start.<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public int indexOfSafely(Object data, int start, int end) {
		start = start < 0 ? 0 : start;
		end = end > size ? size : end;
		if (start >= end) {
			throw new RuntimeException();
		}
		return indexOfWithoutCheck(data, start, end);
	}

	/**
	 * 获取某个值在链表区间[start, end)中从前往后的第一个位置,如果未出现返回-1.<br>
	 * start不能小于0且不能大于等于链表尺寸.<br>
	 * end不能小于1且不能大于链表尺寸.<br>
	 * end不能小于start.<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private int indexOfWithoutCheck(Object data, int start, int end) {
		Node<T> node = select(start);
		if (data != null) {
			for (int i = start; i < end; i++) {
				if (data.equals(node.data)) {
					return i;
				}
				node = node.next;
			}
			return -1;
		} else {
			for (int i = start; i < end; i++) {
				if (node.data == null) {
					return i;
				}
				node = node.next;
			}
			return -1;
		}
	}

	/**
	 * 获取某个值在链表中从后往前的第一个位置,如果未出现返回-1
	 * @author Frodez
	 * @date 2018-12-30
	 */
	@Override
	public int lastIndexOf(Object data) {
		return lastIndexOfWithoutCheck(data, 0, size);
	}

	/**
	 * 获取某个值在链表区间[start, end)中从后往前的第一个位置,如果未出现返回-1.<br>
	 * start不能小于0且不能大于等于链表尺寸.<br>
	 * end不能小于1且不能大于链表尺寸.<br>
	 * end不能小于start.<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public int lastIndexOf(Object data, int start, int end) {
		checkInterval(start, end);
		return lastIndexOfWithoutCheck(data, start, end);
	}

	/**
	 * 安全地获取某个值在链表区间[start, end)中从后往前的第一个位置,如果未出现返回-1.<br>
	 * start小于0时,会被置为0.<br>
	 * end大于链表尺寸时,会被置为链表尺寸.<br>
	 * end不能小于start.<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public int lastIndexOfSafely(Object data, int start, int end) {
		start = start < 0 ? 0 : start;
		end = end > size ? size : end;
		if (start >= end) {
			throw new RuntimeException();
		}
		return lastIndexOfWithoutCheck(data, start, end);
	}

	/**
	 * 获取某个值在链表区间[start, end)中从后往前的第一个位置,如果未出现返回-1.<br>
	 * start不能小于0且不能大于等于链表尺寸.<br>
	 * end不能小于1且不能大于链表尺寸.<br>
	 * end不能小于start.<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	private int lastIndexOfWithoutCheck(Object data, int start, int end) {
		Node<T> node = select(end - 1);
		if (data != null) {
			for (int i = start; i < end; i++) {
				if (data.equals(node.data)) {
					return i;
				}
				node = node.prev;
			}
			return -1;
		} else {
			for (int i = start; i < end; i++) {
				if (node.data == null) {
					return i;
				}
				node = node.prev;
			}
			return -1;
		}
	}

	/**
	 * 将链表裁剪成[start, end)区间的子链表.<br>
	 * start不能小于0且不能大于等于链表尺寸.<br>
	 * end不能小于1且不能大于链表尺寸.<br>
	 * end不能小于start.<br>
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public LinkList<T> cutList(int start, int end) {
		checkInterval(start, end);
		LinkList<T> list = new LinkList<>();
		Node<T> startNode = select(start);
		Node<T> endNode = select(end - 1);
		// jdk会自己收集被裁剪的元素
		startNode.prev.next = null;
		endNode.next.prev = null;
		list.startNode.next = startNode;
		list.endNode.prev = endNode;
		list.size = size;
		return list;
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
		Node<T> node = startNode;
		for (int i = 0; i < size; i++) {
			node = node.next;
			if (node.data == null) {
				sb.append(defaultString);
			} else {
				sb.append(node.data.toString());
			}
			if (i != size - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * toString方法,可自定义每个值的转换方式,null值会转换为"null"
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public String toString(Function<T, String> function) {
		return toString(function, "null");
	}

	/**
	 * toString方法,可自定义每个值的转换方式,null值会按默认字符串转换
	 * @author Frodez
	 * @date 2018-12-30
	 */
	public String toString(Function<T, String> function, String defaultString) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Node<T> node = startNode;
		for (int i = 0; i < size; i++) {
			node = node.next;
			if (node.data == null) {
				sb.append(defaultString);
			} else {
				sb.append(function.apply(node.data));
			}
			if (i != size - 1) {
				sb.append(", ");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	public void reserse() {
		if (size == 0 || size == 1) {
			return;
		}
		Node<T> target = startNode;
		Node<T> node = endNode.prev;
		for (int i = 0; i < size - 1; i++) {
			// 将node转移到target后,将node.prev转移到this.endNode前,然后将node置为node.prev,将target置为target.next
			Node<T> temp = node.prev;
			node.prev = target;
			target.next = node;
			temp.next = endNode;
			endNode.prev = temp;
			target = target.next;
			node = temp;
		}
		// 将最后的两个元素连接起来
		target.next = node;
		node.prev = target;
	}

	@Override
	public boolean contains(Object data) {
		return indexOf(data) != -1;
	}

	@Override
	public Object[] toArray() {
		if (size == 0) {
			return new Object[] {};
		}
		Object[] result = new Object[size];
		Node<T> node = startNode;
		for (int i = 0; i < size; i++) {
			node = node.next;
			result[i] = node.data;
		}
		return result;
	}

	@Override
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T> T[] toArray(T[] array) {
		if (array == null) {
			throw new NullPointerException();
		}
		Node<T> node = (Node<T>) startNode;
		for (int i = 0; i < array.length; i++) {
			node = node.next;
			if (i < size) {
				array[i] = node.data;
			} else {
				array[i] = null;
			}
		}
		return array;
	}

	@Override
	public boolean remove(Object data) {
		if (data == null) {
			throw new NullPointerException();
		}
		Node<T> node = endNode;
		for (int i = 0; i < size; i++) {
			node = node.prev;
			if (data.equals(node.data)) {
				delete(node);
				return true;
			}
		}
		return false;
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
		T[] array = (T[]) collection.toArray();
		Node<T> node = endNode;
		batchInsert(array, node.prev, node);
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean addAll(int index, Collection<? extends T> collection) {
		checkLegal(index);
		T[] array = (T[]) collection.toArray();
		Node<T> node = select(index);
		batchInsert(array, node.prev, node);
		return true;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			return true;
		}
		Node<T> node = startNode;
		for (int i = 0; i < size; i++) {
			node = node.next;
			if (collection.contains(node.data)) {
				delete(node);
			}
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		if (collection == null || collection.isEmpty()) {
			clear();
			return true;
		}
		Node<T> node = startNode;
		for (int i = 0; i < size; i++) {
			node = node.next;
			if (!collection.contains(node.data)) {
				delete(node);
			}
		}
		return true;
	}

	@Override
	public void clear() {
		startNode.next = endNode;
		endNode.prev = startNode;
		size = 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> subList(int start, int end) {
		checkInterval(start, end);
		Node<T> from = select(start);
		T[] array = (T[]) new Object[end - start];
		for (int i = start; i < end; i++) {
			array[i] = from.data;
		}
		return new LinkList<>(array);
	}

	private class LinkListIterator implements ListIterator<T> {

		private Node<T> now = LinkList.this.startNode.next;

		private int index;

		public LinkListIterator() {
			now = LinkList.this.startNode.next;
			this.index = 0;
		}

		public LinkListIterator(int index) {
			now = LinkList.this.select(index);
			this.index = index;
		}

		@Override
		public boolean hasNext() {
			return now != LinkList.this.endNode;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new IndexOutOfBoundsException();
			}
			T data = now.data;
			now = now.next;
			index++;
			return data;
		}

		@Override
		public boolean hasPrevious() {
			return now != LinkList.this.startNode;
		}

		@Override
		public T previous() {
			if (!hasPrevious()) {
				throw new IndexOutOfBoundsException();
			}
			T data = now.data;
			now = now.prev;
			index--;
			return data;
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
			LinkList.this.checkLegal(index);
			Node<T> temp = now.next;
			LinkList.this.delete(now);
			now = temp;
		}

		@Override
		public void set(T data) {
			now.data = data;
		}

		@Override
		public void add(T data) {
			LinkList.this.insert(data, now, now.next);
			now = now.next;
			index++;
		}

	}

	@Override
	public java.util.Iterator<T> iterator() {
		return listIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new LinkListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		checkLegal(index);
		return new LinkListIterator(index);
	}

	@Override
	public void bubbleSort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertSort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectSort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mergeSort() {
		// TODO Auto-generated method stub

	}

	@Override
	public void quickSort() {
		// TODO Auto-generated method stub

	}

}
