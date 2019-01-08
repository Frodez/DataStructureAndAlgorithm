package datastructure;

import java.util.function.Function;

public class Tree<T> {

	private static class Node<T> {

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

		public Node(T data, Node<T> parent, Node<T> prev, Node<T> next, Node<T> firstChild,
			Node<T> lastChild) {
			this.data = data;
			this.parent = parent;
			this.prev = prev;
			this.next = next;
			this.firstChild = firstChild;
			this.lastChild = lastChild;
		}

	}

	private Node<T> root;

	private Node<T> now;

	/**
	 * 初始化树
	 * @param root
	 */
	public Tree(T root) {
		this.root = new Node<>(root, null, null, null, null, null);
		this.now = this.root;
	}

	/**
	 * 获取当前节点的值
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public T getData() {
		return now.data;
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
	}

	/**
	 * 递归式先序遍历
	 * @author Frodez
	 * @date 2019-01-08
	 */
	public void recursiveAheadIter(Function<T, ?> function) {
		recursiveAheadIter(root, function);
	}

	/**
	 * 递归式先序遍历
	 * @author Frodez
	 * @date 2019-01-08
	 */
	private void recursiveAheadIter(Node<T> iter, Function<T, ?> function) {
		function.apply(now.data);
		Node<T> next = now.firstChild;
		while (next != null) {
			recursiveAheadIter(next, function);
			next = next.next;
		}
	}

}
