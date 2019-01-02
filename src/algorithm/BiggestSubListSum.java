package algorithm;

/**
 * 最大连续子序列和问题
 * @author Adminis
 *
 */
public class BiggestSubListSum {
	
	/**
	 * 计算最大连续子序列和
	 * 问题介绍:设有一组数,其中每个数都是整数.我们需要得到这组数中任意一个连续序列的值中的最大值.
	 * @param array
	 * @return
	 */
	public static int calculate(int[] array) {
		int result = 0;
		int partlyCount = 0;
		for(int i : array) {
			partlyCount = partlyCount + i;
			if(partlyCount < 0) {
				partlyCount = 0;
			} else {
				if(result < partlyCount) {
					result = partlyCount;
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		int[] testData = new int[] {-3, 5, 8, -10, 2, 9, 12, -28, 10, 11};
		System.out.println(calculate(testData));
	}

}
