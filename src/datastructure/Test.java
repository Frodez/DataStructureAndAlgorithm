package datastructure;

public class Test {

	public static final String INDENT = "  ";

	public static String indent(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < num; i++) {
			sb.append(INDENT);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		AVLTree<Integer> tree = new AVLTree<>();
		tree.insert(50);
		tree.insert(60);
		tree.insert(90);
		tree.insert(40);
		tree.insert(10);
		tree.insert(30);
		tree.insert(80);
		tree.insert(70);
		tree.insert(20);
		tree.insert(100);
		tree.insert(45);
		System.out.println(tree);
		//		BinaryTree<Integer> tree = new BinaryTree<>(10);
		//		tree.addLeft(12);
		//		tree.addRight(15);
		//		tree.toLeft();
		//		tree.addLeft(9);
		//		tree.addRight(11);
		//		tree.root();
		//		tree.toRight();
		//		tree.addLeft(13);
		//		tree.addRight(18);
		//		System.out.println(tree.toString());
		//		tree.afterIter((data, depth) -> {
		//			System.out.println(indent(depth) + data);
		//		});
		//		Graph<Integer, Integer> graph = new Graph<>(10);
		//		for (int i = 0; i < graph.size(); i++) {
		//			graph.setData(i, i);
		//		}
		//		graph.addEdge(0, 1, 1);
		//		graph.addEdge(1, 2, 2);
		//		graph.addEdge(1, 4, 3);
		//		graph.addEdge(1, 8, 4);
		//		graph.addEdge(2, 3, 5);
		//		graph.addEdge(2, 6, 6);
		//		graph.addEdge(2, 9, 7);
		//		//		var BFSTree = graph.BFS(0);
		//		//		BFSTree.aheadIter((data, depth) -> {
		//		//			System.out.println(indent(depth) + data);
		//		//		});
		//		var DFSTree = graph.DFSV2(0);
		//		DFSTree.afterIter((data, depth) -> {
		//			System.out.println(indent(depth) + data);
		//		});
		//		SequentList<Integer> bigDecimalList = new SequentList<>();
		//		bigDecimalList.addAll(new Integer[] { 1, 2, 3, 4, 5, 15, 2, 10, 9 });
		//		bigDecimalList.addAll(2, new Integer[] { 6, 7, 2, 8, 12 });
		//		System.out.println(bigDecimalList.toString());
		//		bigDecimalList.insertSort();
		//		bigDecimalList.quickSort();
		//		bigDecimalList.selectSort();
		//		bigDecimalList.mergeSort();
		//		System.out.println(bigDecimalList.toString());
		//		Tree<Integer> tree = new Tree<>(10);
		//		tree.addFirstChild(12);
		//		tree.addLastChild(14);
		//		tree.addLastChild(16);
		//		tree.firstChild();
		//		tree.addFirstChild(17);
		//		tree.addFirstChild(18);
		//		tree.lastChild();
		//		tree.addFirstChild(101);
		//		tree.addFirstChild(100);
		//		tree.root();
		//		tree.firstChild();
		//		tree.nextBrother();
		//		tree.addFirstChild(19);
		//		tree.addFirstChild(20);
		//		tree.nextBrother();
		//		tree.addFirstChild(22);
		//		tree.addFirstChild(23);
		//		tree.addFirstChild(24);
		//		tree.firstChild();
		//		tree.nextBrother();
		//		tree.addFirstChild(30);
		//		tree.recursiveAheadIter((iter, depth) -> {
		//			System.out.println(indent(depth) + "data:" + iter);
		//		});
		//		System.out.println("-----------------------------------");
		//		tree.aheadIter((iter, depth) -> {
		//			System.out.println(indent(depth) + "data:" + iter);
		//		});
		//		System.out.println("-----------------------------------");
		//		tree.recursiveAfterIter((iter, depth) -> {
		//			System.out.println(indent(depth) + "data:" + iter);
		//		});
		//		System.out.println("-----------------------------------");
		//		tree.afterIter((iter, depth) -> {
		//			System.out.println(indent(depth) + "data:" + iter);
		//		});
	}

}
