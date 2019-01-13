package datastructure;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.function.BiConsumer;

public class Tree<T extends Comparable<T>> {

	public static class Node<T> {

		/**
		 * 数据
		 */
		public T data;

		/**
		 * 父节点
		 */
		public Node<T> parent;

		/**
		 * 前一个兄弟节点
		 */
		public Node<T> prev;

		/**
		 * 后一个兄弟节点
		 */
		public Node<T> next;

		/**
		 * 第一个子节点
		 */
		public Node<T> firstChild;

		/**
		 * 最后一个子节点
		 */
		public Node<T> lastChild;

		/**
		 * 深度
		 */
		public int depth = 0;

		public Node(T data, Node<T> parent, Node<T> prev, Node<T> next, Node<T> firstChild, Node<T> lastChild) {
			this.data = data;
			this.parent = parent;
			this.prev = prev;
			this.next = next;
			this.firstChild = firstChild;
			this.lastChild = lastChild;
			this.depth = parent == null ? 0 : parent.depth + 1;
		}

	}

	private Node<T> root;

	private Node<T> now;

	private int size = 0;

	/**
	 * 初始化树
	 * @param root
	 */
	public Tree(T root) {
		this.root = new Node<>(root, null, null, null, null, null);
		this.now = this.root;
		size = 1;
	}

	public int size() {
		return size;
	}

	/**
	 * 获取当前节点的值
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public T now() {
		return now.data;
	}

	public void root() {
		now = root;
	}

	public boolean firstChild() {
		if (now.firstChild != null) {
			now = now.firstChild;
			return true;
		} else {
			return false;
		}
	}

	public boolean lastChild() {
		if (now.lastChild != null) {
			now = now.lastChild;
			return true;
		} else {
			return false;
		}
	}

	public boolean prevBrother() {
		if (now.prev != null) {
			now = now.prev;
			return true;
		} else {
			return false;
		}
	}

	public boolean nextBrother() {
		if (now.next != null) {
			now = now.next;
			return true;
		} else {
			return false;
		}
	}

	public boolean parent() {
		if (now.parent != null) {
			now = now.parent;
			return true;
		} else {
			return false;
		}
	}

	public int depth() {
		return now.depth;
	}

	/**
	 * 新增第一个子节点
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public void addFirstChild(T data) {
		Node<T> oldFirstChild = now.firstChild;
		// 如果原来存在第一个子节点,则必定存在叶子节点和最后一个子节点
		if (oldFirstChild != null) {
			// 将第一个子节点赋值,最后一个子节点不变
			now.firstChild = new Node<>(data, now, null, oldFirstChild, null, null);
			// 第一个子节点的上一个兄弟节点赋值
			oldFirstChild.prev = now.firstChild;
		} else {
			now.firstChild = new Node<>(data, now, null, null, null, null);
			now.lastChild = now.firstChild;
		}
		size++;
	}

	/**
	 * 新增最后一个子节点
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public void addLastChild(T data) {
		Node<T> oldLastChild = now.lastChild;
		if (oldLastChild != null) {
			now.lastChild = new Node<>(data, now, oldLastChild, null, null, null);
			oldLastChild.next = now.lastChild;
		} else {
			now.lastChild = new Node<>(data, now, null, null, null, null);
			now.firstChild = now.lastChild;
		}
		size++;
	}

	/**
	 * 新增下一个兄弟节点
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public void addNextBrother(T data) {
		if (now == root) {
			throw new RuntimeException();
		}
		Node<T> oldNext = now.next;
		now.next = new Node<>(data, now.parent, now, oldNext, null, null);
		if (oldNext != null) {
			oldNext.prev = now.next;
		}
		size++;
	}

	/**
	 * 新增上一个兄弟节点
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public void addPrevBrother(T data) {
		if (now == root) {
			throw new RuntimeException();
		}
		Node<T> oldPrev = now.prev;
		now.prev = new Node<>(data, now.parent, oldPrev, now, null, null);
		if (oldPrev != null) {
			oldPrev.next = now.prev;
		}
		size++;
	}

	/**
	 * 递归式先序遍历
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public void recursiveAheadIter(BiConsumer<T, Integer> consumer) {
		recursiveAheadIter(root, consumer);
	}

	/**
	 * 递归式先序遍历
	 * @author Frodez
	 * @date 2019-01-08
	 */
	private void recursiveAheadIter(Node<T> iter, BiConsumer<T, Integer> consumer) {
		consumer.accept(iter.data, iter.depth);
		Node<T> next = iter.firstChild;
		while (next != null) {
			recursiveAheadIter(next, consumer);
			next = next.next;
		}
	}

	public void aheadIter(BiConsumer<T, Integer> consumer) {
		Node<T> node = root;
		Queue<Node<T>> queue = new ArrayDeque<>(size);
		Stack<Node<T>> stack = new Stack<>();
		while (true) {
			stack.push(node);
			queue.add(node);
		}
	}

	/**
	 * 递归式后序遍历
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public void recursiveAfterIter(BiConsumer<T, Integer> consumer) {
		recursiveAfterIter(root, consumer);
	}

	/**
	 * 递归式后序遍历
	 * @author Frodez
	 * @date 2019-01-08
	 */
	private void recursiveAfterIter(Node<T> iter, BiConsumer<T, Integer> consumer) {
		Node<T> next = iter.firstChild;
		while (next != null) {
			recursiveAfterIter(next, consumer);
			next = next.next;
		}
		consumer.accept(iter.data, iter.depth);
	}

}
