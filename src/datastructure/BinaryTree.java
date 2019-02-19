package datastructure;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.function.BiConsumer;

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

		@Override
		public String toString() {
			return data.toString();
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

	public boolean toLeft() {
		if (now.left != null) {
			now = now.left;
			return true;
		} else {
			return false;
		}
	}

	public boolean toRight() {
		if (now.right != null) {
			now = now.right;
			return true;
		} else {
			return false;
		}
	}

	public boolean toParent() {
		if (now.parent != null) {
			now = now.parent;
			return true;
		} else {
			return false;
		}
	}

	public boolean addLeft(T data) {
		if (now.left == null) {
			now.left = new Node<>(data, now, null, null);
			return true;
		} else {
			return false;
		}
	}

	public boolean addRight(T data) {
		if (now.right == null) {
			now.right = new Node<>(data, now, null, null);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 前序遍历
	 * @author Frodez
	 * @date 2019-02-14
	 */
	public void preIter(BiConsumer<T, Integer> consumer) {
		Stack<Node<T>> stack = new Stack<>();
		Node<T> node = root;
		while (true) {
			while (node != null) {
				consumer.accept(node.data, node.depth);
				stack.push(node);
				node = node.left;
			}
			if (stack.isEmpty()) {
				break;
			}
			node = stack.pop().right;
		}
	}

	//前序遍历和中序遍历非常相似:首先,从某个节点起,一直沿左分支向下,直到没有左孩子节点为止,将经过的节点逐个入栈.
	//然后判断栈是否为空,为空则退出,否则指向为止时的节点的右孩子节点.
	//前序遍历的遍历位于沿左分支向下时,中序遍历的遍历位于判断栈不为空之后.
	/**
	 * 中序遍历
	 * @author Frodez
	 * @date 2019-02-14
	 */
	public void middleIter(BiConsumer<T, Integer> consumer) {
		Stack<Node<T>> stack = new Stack<>();
		Node<T> node = root;
		while (true) {
			while (node != null) {
				stack.push(node);
				node = node.left;
			}
			if (stack.isEmpty()) {
				break;
			}
			node = stack.pop();
			consumer.accept(node.data, node.depth);
			node = node.right;
		}
	}

	//后序遍历和前序以及中序不同.后序需要先将根节点入栈,然后每次判断栈是否为空,为空则结束.不为空时,就先看栈顶元素是否符合遍历条件:
	//要么该节点没有子节点,要么该节点的左右子节点中的一个是刚刚遍历过的节点.
	//如果满足遍历条件,则遍历栈顶元素,将栈顶元素弹出,将遍历过的节点置为该元素.如果不满足,则从右至左将该节点的直接子节点入栈.
	/**
	 * 后序遍历
	 * @author Frodez
	 * @date 2019-02-14
	 */
	public void afterIter(BiConsumer<T, Integer> consumer) {
		Stack<Node<T>> stack = new Stack<>();
		Node<T> node = root;
		Node<T> prev = null;
		stack.push(node);
		while (!stack.isEmpty()) {
			node = stack.peek();
			if (noChild(node) || isNext(node, prev)) {
				consumer.accept(node.data, node.depth);
				stack.pop();
				prev = node;
				continue;
			}
			if (node.right != null) {
				stack.push(node.right);
			}
			if (node.left != null) {
				stack.push(node.left);
			}
		}
	}

	private boolean noChild(Node<T> node) {
		return node.left == null && node.right == null;
	}

	private boolean isNext(Node<T> node, Node<T> prev) {
		return (node.left == prev || node.right == prev) && prev != null;
	}

	//层次遍历需要一直以从上到下从左到右的顺序来遍历,因此需要使用队列而不是栈.
	//先将根节点入队列,然后每次判断队列是否为空,为空则退出,不为空则继续.
	//然后弹出队尾元素,遍历.接下来从左到右将该元素子节点入队列.
	/**
	 * 层次遍历
	 * @author Frodez
	 * @date 2019-02-14
	 */
	public void layerIter(BiConsumer<T, Integer> consumer) {
		Queue<Node<T>> queue = new ArrayDeque<>();
		Node<T> node = root;
		queue.add(root);
		while (!queue.isEmpty()) {
			node = queue.poll();
			consumer.accept(node.data, node.depth);
			if (node.left != null) {
				queue.add(node.left);
			}
			if (node.right != null) {
				queue.add(node.right);
			}
		}
	}

}
