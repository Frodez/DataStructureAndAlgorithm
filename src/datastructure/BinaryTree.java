package datastructure;

public class BinaryTree<T> {

	public static class Node<T> {

		public T data;

		public Node<T> parent;

		public Node<T> left;

		public Node<T> right;

		public int depth = 0;

		public Node(T data, Node<T> parent, Node<T> left, Node<T> right) {
			this.data = data;
			this.parent = parent;
			this.left = left;
			this.right = right;
			if (parent != null) {
				this.depth = parent.depth + 1;
			}
		}

	}

	private Node<T> root;

	private Node<T> now;

	private int size = 0;

	/**
	 * 初始化树
	 * @param root
	 */
	public BinaryTree(T root) {
		this.root = new Node<>(root, null, null, null);
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

	public int depth() {
		return now.depth;
	}

	public boolean hasLeft() {
		return now.left != null;
	}

	public boolean hasRight() {
		return now.right != null;
	}

	public void root() {
		now = root;
	}

	public boolean left() {
		if (now.left != null) {
			now = now.left;
			return true;
		} else {
			return false;
		}
	}

	public boolean right() {
		if (now.right != null) {
			now = now.right;
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

	public boolean addLeft(T data) {
		if (now.left == null) {
			now.left = new Node<>(data, now, null, now.right);
			if (now.right != null) {
				now.right.left = now.left;
			}
			return true;
		} else {
			return false;
		}
	}

	public boolean addRight(T data) {
		if (now.right == null) {
			now.right = new Node<>(data, now, now.left, null);
			if (now.left != null) {
				now.left.right = now.right;
			}
			return true;
		} else {
			return false;
		}
	}

}
