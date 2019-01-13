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
		SequentList<Integer> bigDecimalList = new SequentList<>();
		bigDecimalList.addAll(new Integer[] { 1, 2, 3, 4, 5, 15, 2, 10, 9 });
		bigDecimalList.addAll(2, new Integer[] { 6, 7, 2, 8, 12 });
		System.out.println(bigDecimalList.toString());
		// bigDecimalList.insertSort();
		// bigDecimalList.quickSort();
		// bigDecimalList.selectSort();
		bigDecimalList.mergeSort();
		System.out.println(bigDecimalList.toString());
		Tree<Integer> tree = new Tree<>(10);
		tree.addFirstChild(12);
		tree.addLastChild(14);
		tree.addLastChild(16);
		tree.firstChild();
		tree.addFirstChild(18);
		tree.nextBrother();
		tree.addFirstChild(20);
		tree.nextBrother();
		tree.addFirstChild(22);
		tree.addFirstChild(24);
		tree.recursiveAfterIter((iter, depth) -> {
			System.out.println(indent(depth) + "data:" + iter);
		});
	}

}
