package datastructure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Graph<V, E> {

	/**
	 * 遍历状态 0:未发现 1:已发现 2:已遍历
	 */
	private static final byte UNDISCOVERED = 0;

	/**
	 * 遍历状态 0:未发现 1:已发现 2:已遍历
	 */
	private static final byte DISCOVERED = 1;

	/**
	 * 遍历状态 0:未发现 1:已发现 2:已遍历
	 */
	private static final byte VISITED = 2;

	/**
	 * 顶点
	 * @author Frodez
	 * @date 2019-02-15
	 */
	public static class Vertex<V> {

		public V data;

		public int position;

		/**
		 * 遍历状态 0:未发现 1:已发现 2:已遍历
		 */
		public byte status = UNDISCOVERED;

		/**
		 * 发现时间
		 */
		public int discoveredTime = 0;

		/**
		 * 遍历时间
		 */
		public int finalTime = 0;

		/**
		 * 出度
		 */
		public int out = 0;

		/**
		 * 入度
		 */
		public int in = 0;

		public Vertex(V data, int position) {
			this.data = data;
			this.position = position;
		}

		@Override
		public String toString() {
			return "[data=" + data.toString() + ", status=" + status + ", out=" + out + ", in=" + in + ", dTime="
				+ discoveredTime + ", fTime=" + finalTime + "]";
		}

	}

	/**
	 * 边
	 * @author Frodez
	 * @date 2019-02-15
	 */
	public static class Edge<E> {

		public E data;

		public boolean linked;

		public Edge(E data, boolean linked) {
			this.data = data;
			this.linked = linked;
		}

		@Override
		public String toString() {
			return "[data=" + data.toString() + ", linked=" + linked + "]";
		}

	}

	/**
	 * 顶点
	 */
	private List<Vertex<V>> vertexs;

	/**
	 * 邻接矩阵
	 */
	private List<List<Edge<E>>> matrix;

	private int edges;

	public Graph() {
		//初始化顶点
		vertexs = new ArrayList<>();
		//初始化邻接矩阵
		matrix = new ArrayList<>();
		edges = 0;
	}

	public Graph(int size) {
		//初始化顶点
		vertexs = new ArrayList<>(size);
		//初始化邻接矩阵
		matrix = new ArrayList<>(size);
		edges = 0;
		for (int i = 0; i < size; i++) {
			Vertex<V> vertex = new Vertex<>(null, i);
			List<Edge<E>> row = new ArrayList<>(size);
			for (int j = 0; j < size; j++) {
				Edge<E> edge = new Edge<>(null, false);
				row.add(edge);
			}
			matrix.add(row);
			vertexs.add(vertex);
		}
	}

	public int size() {
		return vertexs.size();
	}

	public int edges() {
		return edges;
	}

	public V getData(int index) {
		checkVertex(index);
		return vertex(index).data;
	}

	public E getData(int start, int end) {
		checkMatrix(start, end);
		return matrix(start, end).data;
	}

	public void setData(int index, V data) {
		checkVertex(index);
		vertex(index).data = data;
	}

	public void setData(int start, int end, E data) {
		checkMatrix(start, end);
		matrix(start, end).data = data;
	}

	public boolean isLinked(int start, int end) {
		checkMatrix(start, end);
		return matrix(start, end).linked;
	}

	private Edge<E> matrix(int row, int column) {
		return matrix.get(row).get(column);
	}

	private Vertex<V> vertex(int index) {
		return vertexs.get(index);
	}

	/**
	 * 检查顶点位置
	 * @author Frodez
	 * @date 2019-02-15
	 */
	private void checkVertex(int index) {
		if (index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException("非法的顶点位置!");
		}
	}

	/**
	 * 检查邻接矩阵行列位置
	 * @author Frodez
	 * @date 2019-02-15
	 */
	private void checkMatrix(int row, int column) {
		if (row < 0 || row >= size()) {
			throw new IndexOutOfBoundsException("非法的邻接矩阵行!");
		}
		if (column < 0 || column >= size()) {
			throw new IndexOutOfBoundsException("非法的邻接矩阵列!");
		}
	}

	/**
	 * 添加新顶点
	 * @author Frodez
	 * @date 2019-02-15
	 */
	public void addVertex(V data) {
		Vertex<V> vertex = new Vertex<>(data, this.vertexs.size());
		Edge<E> edge = new Edge<>(null, false);
		//加入新顶点
		this.vertexs.add(vertex);
		//邻接矩阵增加一行和一列
		for (List<Edge<E>> row : matrix) {
			row.add(edge);
		}
		List<Edge<E>> row = new ArrayList<>();
		for (int i = 0; i < (matrix.isEmpty() ? 1 : size()); i++) {//如果存在行,那么刚才已经给行增加列了
			row.add(edge);
		}
		matrix.add(row);
	}

	/**
	 * 删除顶点
	 * @author Frodez
	 * @date 2019-02-15
	 */
	public V deleteVertex(int index) {
		checkVertex(index);
		//删除出边
		for (int i = 0; i < size(); i++) {
			if (matrix(index, i).linked) {
				vertex(i).in--;
			}
		}
		matrix.remove(index);
		//删除入边
		for (int i = 0; i < size(); i++) {
			if (matrix(i, index).linked) {
				vertex(i).out--;
			}
			matrix.get(i).remove(index);
		}
		return vertexs.remove(index).data;
	}

	/**
	 * 查找顶点
	 * @author Frodez
	 * @date 2019-02-15
	 */
	public int findVertex(V data) {
		if (data != null) {
			int i = size() - 1;
			for (; i >= 0; i--) {
				if (data.equals(vertex(i).data)) {
					break;
				}
			}
			return i;
		} else {
			int i = size() - 1;
			for (; i >= 0; i--) {
				if (vertex(i).data == null) {
					break;
				}
			}
			return i;
		}
	}

	/**
	 * 查找顶点接下来的邻接顶点(指顶点指向邻接顶点)
	 * @param index 顶点位置
	 * @param start 查找起点
	 * @author Frodez
	 * @date 2019-02-15
	 */
	public int nextNbr(int index, int start) {
		checkMatrix(index, start);
		int i = start;
		for (; i >= 0; i--) {
			if (matrix(index, i).linked) {
				break;
			}
		}
		return i;
	}

	public int firstNbr(int index) {
		return nextNbr(index, size() - 1);
	}

	public boolean addEdge(int start, int end, E data) {
		checkMatrix(start, end);
		Edge<E> edge = matrix(start, end);
		if (edge.linked) {
			return false;
		}
		edge.linked = true;
		edge.data = data;
		vertex(start).out++;
		vertex(end).in++;
		edges++;
		return true;
	}

	public E deleteEdge(int start, int end) {
		checkMatrix(start, end);
		Edge<E> edge = matrix(start, end);
		if (!edge.linked) {
			return null;
		}
		edge.linked = false;
		vertex(start).out--;
		vertex(end).in--;
		edges--;
		return edge.data;
	}

	public Tree<V> BFS(final int root) {
		checkVertex(root);
		Tree<V> tree = new Tree<>(vertex(root).data);
		Queue<Vertex<V>> queue = new ArrayDeque<>();
		queue.add(vertex(root));
		Vertex<V> vertex = null;
		int time = 1;
		while (!queue.isEmpty()) {
			vertex = queue.poll();
			vertex.status = DISCOVERED;
			vertex.discoveredTime = time;
			time++;
			if (vertex.position != root) {
				if (tree.hasNext()) {
					tree.toNextBrother();
				} else {
					tree.toFirstChild();
				}
			}
			int i = firstNbr(vertex.position);
			while (i > -1) {
				Vertex<V> nbr = vertex(i);
				if (nbr.status == UNDISCOVERED) {
					nbr.status = DISCOVERED;
					nbr.discoveredTime = time;
					time++;
					queue.add(nbr);
					tree.addLastChild(nbr.data);
				}
				i = nextNbr(vertex.position, i - 1);
			}
			vertex.status = VISITED;
			vertex.finalTime = time;
		}
		return tree;
	}

	public Tree<V> DFSV1(int root) {
		checkVertex(root);
		Tree<V> tree = new Tree<>(vertex(root).data);
		DFSV1(root, 1, tree);
		return tree;
	}

	private int DFSV1(final int index, int time, Tree<V> tree) {
		vertex(index).status = DISCOVERED;
		vertex(index).discoveredTime = time;
		time++;
		if (!tree.isRoot() || tree.hasChild()) {
			tree.toFirstChild();
		}
		int i = firstNbr(index);
		while (i > -1) {
			if (vertex(i).status == UNDISCOVERED) {
				tree.addFirstChild(vertex(i).data);
				time = DFSV1(i, time, tree);
				tree.toParent();
			}
			i = nextNbr(index, i - 1);
		}
		vertex(index).status = VISITED;
		vertex(index).finalTime = time;
		time++;
		return time;
	}

	public Tree<V> DFSV2(int root) {
		checkVertex(root);
		Tree<V> tree = new Tree<>(vertex(root).data);
		Stack<Vertex<V>> stack = new Stack<>();
		stack.push(vertex(root));
		int time = 1;
		vertex(root).status = DISCOVERED;
		vertex(root).discoveredTime = time;
		time++;
		Vertex<V> vertex = null;
		while (!stack.isEmpty()) {
			vertex = stack.peek();
			if (needVisit(vertex.position)) {
				vertex = stack.pop();
				vertex.status = VISITED;
				vertex.finalTime = time;
				time++;
			} else {
				int i = firstNbr(vertex.position);
				if (!tree.isRoot() || tree.hasChild()) {
					tree.toFirstChild();
				}
				while (i > -1) {
					if (vertex(i).status == UNDISCOVERED) {
						vertex(i).status = DISCOVERED;
						vertex(i).discoveredTime = time;
						time++;
						tree.addFirstChild(vertex(i).data);
						stack.push(vertex(i));
					}
					i = nextNbr(vertex.position, i - 1);
				}
			}
		}
		return tree;
	}

	private boolean needVisit(int position) {
		int i = firstNbr(position);
		if (i == -1) {
			return true;
		}
		while (i > -1) {
			if (vertex(i).status == UNDISCOVERED) {
				return false;
			}
			i = nextNbr(position, i - 1);
		}
		return true;
	}

}
