package datastructure;

import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.function.BiConsumer;

public class Tree<T> {

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

	public int depth() {
		return now.depth;
	}

	public void toRoot() {
		now = root;
	}

	public boolean hasChild() {
		return now.firstChild != null;
	}

	public boolean hasPrev() {
		return now.prev != null;
	}

	public boolean hasNext() {
		return now.next != null;
	}

	public boolean isRoot() {
		return now == root;
	}

	public void toFirstChild() {
		if (now.firstChild == null) {
			throw new NoSuchElementException();
		}
		now = now.firstChild;
	}

	public void toLastChild() {
		if (now.lastChild == null) {
			throw new NoSuchElementException();
		}
		now = now.lastChild;
	}

	public void toPrevBrother() {
		if (now.prev == null) {
			throw new NoSuchElementException();
		}
		now = now.prev;
	}

	public void toNextBrother() {
		if (now.next == null) {
			throw new NoSuchElementException();
		}
		now = now.next;
	}

	public void toParent() {
		if (now.parent == null) {
			throw new NoSuchElementException();
		}
		now = now.parent;
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

	/**
	 * 非递归式先序遍历
	 * @author Frodez
	 * @date 2019-01-14
	 */
	public void aheadIter(BiConsumer<T, Integer> consumer) {
		Node<T> node = root;
		Stack<Node<T>> stack = new Stack<>();
		while (true) {
			//先对自己进行操作,并将自己压入栈中.(此为父->左->右中的父)
			//如果有子节点,则指向最左边的子节点.
			//如果没有子节点,则将栈顶元素退出.(没有子节点时,由于一直从左边起遍历,因此自己一定是父节点的最左子节点)
			//直到退出的栈顶元素拥有下一个兄弟节点为止.
			//此时指向下一个兄弟节点.(下一个兄弟节点即意味着从左往右)
			//如果退出的栈顶元素为根节点,则终止循环.
			consumer.accept(node.data, node.depth);
			stack.push(node);
			if (node.firstChild != null) {
				node = node.firstChild;
			} else {
				while (true) {
					Node<T> temp = stack.pop();
					if (temp == root) {
						return;
					}
					if (temp.next != null) {
						node = temp.next;
						break;
					}
				}
			}
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

	/**
	 * 非递归式后序遍历
	 * @author Frodez
	 * @date 2019-01-14
	 */
	public void afterIter(BiConsumer<T, Integer> consumer) {
		Node<T> node = root;
		Stack<Node<T>> stack = new Stack<>();
		while (true) {
			//如果当前节点存在子节点,则将当前节点压入栈中,并指向第一个子节点.
			//如果当前节点不存在子节点,则操作本元素,且判断是否存在下一个兄弟节点.
			//(没有子节点时,由于一直从左边起遍历,因此自己一定是父节点的最左子节点)
			//如果存在下一个兄弟节点,则指向下一个兄弟节点.(从左到右的顺序)
			//如果不存在下一个兄弟节点,则将栈顶元素弹出,并操作.(最后操作父节点)
			//然后判断其是否存在下一个兄弟节点,直到存在下一个兄弟节点为止.
			//此时指向下一个兄弟节点.
			//如果弹出元素为根节点,则退出循环.
			if (node.firstChild != null) {
				stack.push(node);
				node = node.firstChild;
			} else {
				consumer.accept(node.data, node.depth);
				if (node.next == null) {
					while (true) {
						Node<T> temp = stack.pop();
						consumer.accept(temp.data, temp.depth);
						if (temp == root) {
							return;
						}
						if (temp.next != null) {
							node = temp.next;
							break;
						}
					}
				} else {
					node = node.next;
				}
			}
		}
	}

}
