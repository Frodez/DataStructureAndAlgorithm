package datastructure;

public class Test {

	public static void main(String[] args) {
		LinkList<Integer> bigDecimalList = new LinkList<>(new Integer[] {1, 2, 3, 4, 5});
		bigDecimalList.addAll(4, new Integer[] {6, 7});
		System.out.println(bigDecimalList.indexOf(2));
		System.out.println(bigDecimalList.lastIndexOf(2));
		System.out.println(bigDecimalList.toString());
		bigDecimalList.reserse();
		System.out.println(bigDecimalList.indexOf(2));
		System.out.println(bigDecimalList.lastIndexOf(2));
		System.out.println(bigDecimalList.toString());
	}

}
