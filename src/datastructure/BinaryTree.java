package datastructure;

public class BinaryTree<T> {

	private static class Node<T> {

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

}
