package datastructure;

public class Test {

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
	}

}
