package algorithm;

public class Swap {

	public static void main(String[] args) {
		int[] list = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21 };
		System.out.println(string(list));
		int aStart = 0;
		int aEnd = 1;
		int bStart = aEnd + 1;
		int bEnd = list.length - 1;
		exchange(list, aStart, aEnd, bStart, bEnd);
		System.out.println(count);
		System.out.println(string(list));
	}

	public static String string(int[] list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			builder.append(list[i]);
			if (i != list.length - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

	public static void exchange(int[] list, int aStart, int aEnd, int bStart, int bEnd) {
		int condition = aEnd - aStart - (bEnd - bStart);
		if (condition == 0) {
			swap(list, aStart, bStart, aEnd - aStart + 1);
			return;
		} else if (condition > 0) {
			swap(list, aStart + condition, bStart, bEnd - bStart + 1);
			exchange(list, aStart, aStart + condition - 1, aStart + condition, bEnd - (bEnd - bStart + 1));
		} else {
			swap(list, aStart, bStart, aEnd - aStart + 1);
			exchange(list, aStart + aEnd - aStart + 1, bEnd + condition, bEnd + condition + 1, bEnd);
		}
	}

	public static int count = 0;

	public static void swap(int[] list, int aStart, int bStart, int length) {
		count = count + length;
		for (int i = 0; i < length; i++) {
			int temp = list[bStart + i];
			list[bStart + i] = list[aStart + i];
			list[aStart + i] = temp;
		}
	}

}
