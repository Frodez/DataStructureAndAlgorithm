package datastructure;

public class Test {

	public static void main(String[] args) {
		SequentList<Integer> bigDecimalList = new SequentList<>();
		bigDecimalList.addAll(new Integer[] { 1, 2, 3, 4, 5 });
		bigDecimalList.addAll(4, new Integer[] { 6, 7 });
		System.out.println(bigDecimalList.toString());
		bigDecimalList.quickSort();
		System.out.println(bigDecimalList.toString());
	}

}
