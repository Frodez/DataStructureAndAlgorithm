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
		//初始化操作:将当前节点指定为root节点
		Node<T> node = root;
		while (true) {
			//首先执行操作:寻找当前节点及其所有的左孩子节点并且遍历该节点,直到无法找到.
			//每找到一个节点,即将其入栈,并指向其左孩子(不论是否存在).
			conductAllLefts(stack, node, consumer);
			//如果该操作无法使得栈非空,则说明遍历完毕
			if (stack.isEmpty()) {
				break;
			}
			//将栈顶弹出,并将当前节点指向弹出节点的右孩子节点(不论是否存在)
			node = stack.pop().right;
		}
	}

	/**
	 * 寻找当前节点及其所有的左孩子节点,每找到一个节点,即将其入栈,<br>
	 * 如果存在消费者,则使用消费者遍历该节点.<br>
	 * 并指向其左孩子(不论是否存在),直到当前节点为空.<br>
	 * 返回时当前节点一定为空.
	 * @author Frodez
	 * @date 2019-05-28
	 */
	private void conductAllLefts(Stack<Node<T>> stack, Node<T> now, BiConsumer<T, Integer> consumer) {
		while (now != null) {
			stack.push(now);
			if (consumer != null) {
				consumer.accept(now.data, now.depth);
			}
			now = now.left;
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
		//初始化操作:将当前节点指定为root节点
		Node<T> node = root;
		while (true) {
			//首先执行操作:寻找当前节点及其所有的左孩子节点,直到无法找到.(这里不会遍历该节点)
			//每找到一个节点,即将其入栈,并指向其左孩子(不论是否存在).
			conductAllLefts(stack, node, null);
			//如果该操作无法使得栈非空,则说明遍历完毕
			if (stack.isEmpty()) {
				break;
			}
			//将栈顶弹出
			node = stack.pop();
			//遍历弹出节点
			consumer.accept(node.data, node.depth);
			//将当前节点指向弹出节点的右孩子节点(不论是否存在)
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
		//初始化操作1:将根节点入栈
		stack.push(root);
		//初始化操作2:设定两个节点,一个指当前节点,一个指前一个被遍历过的节点.均初始化为空.
		Node<T> node = null;
		Node<T> prev = null;
		//当栈非空时执行循环
		while (!stack.isEmpty()) {
			//将当前节点指向栈顶元素,但不出栈
			node = stack.peek();
			//如果当前节点无孩子,或者是合法的下一个节点(指当前节点的孩子为被遍历过的节点,且被遍历过的节点非空),
			//则遍历该节点,该节点出栈并将遍历过节点置为该节点,重新开始循环.
			if (noChild(node) || isNext(node, prev)) {
				consumer.accept(node.data, node.depth);
				stack.pop();
				prev = node;
				continue;
			}
			//然后按从右至左的顺序,将其所有非空孩子入栈,之后进入下一次循环.
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
		//初始化操作1:将root放入队列
		queue.add(root);
		//初始化操作2:设定当前节点(为空)
		Node<T> node = null;
		//当队列非空时执行循环
		while (!queue.isEmpty()) {
			//从队列中弹出元素,并将当前节点指向该元素
			node = queue.poll();
			//遍历该节点
			consumer.accept(node.data, node.depth);
			//从左到右的顺序将当前节点所有的非空孩子节点放入队列,然后进入下一循环.
			if (node.left != null) {
				queue.add(node.left);
			}
			if (node.right != null) {
				queue.add(node.right);
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		middleIter((data, depth) -> {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < depth; i++) {
				builder.append("  ");
			}
			buffer.append(builder.toString()).append(data.toString()).append("\n");
		});
		return buffer.toString();
	}

}
