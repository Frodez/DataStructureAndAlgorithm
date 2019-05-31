package datastructure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class AVLTree<T extends Comparable<T>> {

	static class Node<T> {

		public T data;

		public Node<T> left;

		public Node<T> right;

		public int height = 1;

		public Node(T data) {
			this.data = data;
			this.left = null;
			this.right = null;
		}

		public Node(T data, Node<T> left, Node<T> right) {
			this.data = data;
			this.left = left;
			this.right = right;
		}

		@Override
		public String toString() {
			return this.data.toString();
		}

	}

	private Node<T> root = null;

	private int size = 0;

	public AVLTree() {
	}

	public int size() {
		return size;
	}

	public int height() {
		return root == null ? 0 : root.height;
	}

	private int max(int first, int second) {
		return first > second ? first : second;
	}

	private int height(Node<T> node) {
		if (node == null) {
			return 0;
		}
		return max(height(node.left), height(node.right)) + 1;
	}

	private int compareHeight(Node<T> node) {
		return height(node.left) - height(node.right);
	}

	private Node<T> findParent(Node<T> node) {
		Queue<Node<T>> queue = new ArrayDeque<>();
		Node<T> now = null;
		queue.add(root);
		while (!queue.isEmpty()) {
			now = queue.poll();
			if (now.left == node || now.right == node) {
				return now;
			}
			if (now.left != null) {
				queue.add(now.left);
			}
			if (now.right != null) {
				queue.add(now.right);
			}
		}
		return null;
	}

	/**
	 * 有存在节点返回null;没有任何节点返回emptyStack;有节点返回该节点以及其所有父节点组成的栈,栈顶为该节点.
	 * @author Frodez
	 * @date 2019-06-01
	 */
	private List<Node<T>> findNodes(T data) {
		List<Node<T>> list = new ArrayList<>();
		Node<T> now = root;
		while (now != null) {
			int result = data.compareTo(now.data);
			if (result == 0) {
				return null;
			} else {
				list.add(now);
				now = result > 0 ? now.right : now.left;
			}
		}
		return list;
	}

	/**
	 * 更新父节点及其所有父亲的高度
	 * @author Frodez
	 * @date 2019-06-01
	 */
	private void refreshHeight(List<Node<T>> list) {
		list.get(list.size() - 1).height = 2;
		for (int i = list.size() - 2; i >= 0; i--) {
			Node<T> now = list.get(i);
			int leftHeight = now.left == null ? 1 : now.left.height;
			int rightHeight = now.right == null ? 1 : now.right.height;
			now.height = max(leftHeight, rightHeight) + 1;
		}
	}

	/**
	 * 左旋,以parent的右孩子为轴心,将parent左旋,并将轴心的左孩子变为parent右孩子.
	 * @author Frodez
	 * @date 2019-06-01
	 */
	private void leftRotate(Node<T> parent) {
		if (parent == root) {
			Node<T> center = parent.right;
			parent.right = center.left;
			center.left = parent;
			root = center;
		} else {
			Node<T> grandParent = findParent(parent);
			boolean position = grandParent.left == parent;
			Node<T> center = parent.right;
			parent.right = center.left;
			center.left = parent;
			if (position) {
				grandParent.left = center;
			} else {
				grandParent.right = center;
			}
		}
	}

	/**
	 * 右旋,以parent的左孩子为轴心,将parent右旋,并将轴心的右孩子变为parent左孩子.
	 * @author Frodez
	 * @date 2019-06-01
	 */
	private void rightRotate(Node<T> parent) {
		if (parent == root) {
			Node<T> center = parent.left;
			parent.left = center.right;
			center.right = parent;
			root = center;
		} else {
			Node<T> grandParent = findParent(parent);
			boolean position = grandParent.left == parent;
			Node<T> center = parent.left;
			parent.left = center.right;
			center.right = parent;
			if (position) {
				grandParent.left = center;
			} else {
				grandParent.right = center;
			}
		}
	}

	public boolean insert(T data) {
		if (data == null) {
			throw new IllegalArgumentException("data can't be null!");
		}
		//节点已存在时返回null,否则找出合适的父节点以及其所有的父亲.
		List<Node<T>> parents = findNodes(data);
		//如果节点已存在则返回false
		if (parents == null) {
			return false;
		}
		//如果没有父节点,说明是空树
		if (parents.isEmpty()) {
			root = new Node<>(data);
			return true;
		}
		//找出父节点
		Node<T> parent = parents.get(parents.size() - 1);
		//判断与父节点关系,并插入到父节点的孩子节点上
		int result = data.compareTo(parent.data);
		if (result > 0) {
			parent.right = new Node<>(data);
		} else {
			parent.left = new Node<>(data);
		}
		if (parents.size() == 2) {
			//如果数量为2,说明父节点的父亲一定是root
			//判断root的左右高度是否正确
			int compareHeight = compareHeight(root);
			if (compareHeight > 1) {
				//如果不正确且左高度大于右高度,单右旋
				rightRotate(root);
			}
			if (compareHeight < -1) {
				//如果不正确且左高度小于右高度,单左旋
				leftRotate(root);
			}
		} else if (parents.size() > 2) {
			//如果父节点的父亲不是root,则先判断父节点的父节点的父节点的左右高度是否正确,
			//不正确时再判断父节点的父节点的左右高度是否正确。
			Node<T> thirdParent = parents.get(parents.size() - 3);
			int thirdCompareHeight = compareHeight(thirdParent);
			//如果父节点的父节点的父节点的左右高度正确,说明全树高度正确
			if (thirdCompareHeight > 1 || thirdCompareHeight < -1) {
				//如果不正确,根据父节点,父节点的父节点,父节点的父节点的父节点之间的关系判断
				Node<T> secondParent = parents.get(parents.size() - 2);
				if (secondParent == thirdParent.left && parent == secondParent.left) {
					//左左,父节点的父节点的父节点单右旋
					rightRotate(thirdParent);
				} else if (secondParent == thirdParent.right && parent == secondParent.right) {
					//右右,父节点的父节点的父节点单左旋
					leftRotate(thirdParent);
				} else if (secondParent == thirdParent.left && parent == secondParent.right) {
					//左右,先父节点的父节点左旋再父节点的父节点的父节点右旋
					leftRotate(secondParent);
					rightRotate(thirdParent);
				} else if (secondParent == thirdParent.left && parent == secondParent.right) {
					//左右,先父节点的父节点左旋再父节点的父节点的父节点右旋
					rightRotate(secondParent);
					leftRotate(thirdParent);
				}
			}
		}
		//无论一开始是否正确,最后都更新高度并返回true
		refreshHeight(parents);
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		Stack<Node<T>> stack = new Stack<>();
		Node<T> now = root;
		while (true) {
			while (now != null) {
				stack.push(now);
				now = now.left;
			}
			if (stack.isEmpty()) {
				break;
			}
			now = stack.pop();
			builder.append(now.data.toString()).append(" ");
			now = now.right;
		}
		return builder.toString();
	}

}
