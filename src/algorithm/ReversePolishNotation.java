package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 中缀表达式转后缀表达式
 * @author Adminis
 */
public class ReversePolishNotation {

	public enum SignalEnum {
		EOF((byte) 0, "EOF"), RIGHT_BRACKETS((byte) 1, ")"), LEFT_SHIFT((byte) 2, "<<"), RIGHT_SHIFT((byte) 2, ">>"),
		ADD((byte) 3, "+"), SUB((byte) 3, "-"), MUL((byte) 4, "*"), DIV((byte) 4, "/"), LEFT_BRACKETS((byte) 5, "(");

		private byte priority;

		private String signal;

		private SignalEnum(byte priority, String signal) {
			this.priority = priority;
			this.signal = signal;
		}

		public byte getPriority() {
			return this.priority;
		}

		public String getSignal() {
			return this.signal;
		}

		public static SignalEnum of(String signal) {
			for (SignalEnum code : values()) {
				if (code.getSignal().equals(signal)) {
					return code;
				}
			}
			return null;
		}
	}

	private static String BLANK = " ";

	/**
	 * 比较两个操作符的优先级,为true则first的优先级低于second的优先级
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean compare(String first, String second) {
		SignalEnum firstEnum = SignalEnum.of(first);
		SignalEnum secondEnum = SignalEnum.of(second);
		return firstEnum.priority < secondEnum.priority;
	}

	/**
	 * 将缓存栈中的内容放回原栈.
	 * @param tempStack
	 * @param targetStack
	 */
	private static void recoverStack(Stack<String> tempStack, Stack<String> targetStack) {
		while (true) {
			if (tempStack.isEmpty()) {
				break;
			}
			String iter = tempStack.pop();
			targetStack.push(iter);
		}
	}

	/**
	 * 判断是否为数字
	 * @param ch
	 * @return
	 */
	private static boolean isNumber(char ch) {
		return ch >= 48 && ch <= 57;
	}

	/**
	 * 将表达式切割为有效的字符串列表
	 * @param midstExpression
	 * @return
	 */
	private static List<String> sliceToList(String midstExpression) {
		List<String> result = new ArrayList<>();
		if (midstExpression == null) {
			return result;
		}
		int length = midstExpression.length();
		int index = 0;
		while (index < length) {
			char ch = midstExpression.charAt(index);
			String now = String.valueOf(midstExpression.charAt(index));
			if (now.equals(BLANK)) {
				index++;
				continue;
			}
			if (SignalEnum.of(now) != null) {
				result.add(now);
				index++;
				continue;
			}
			if (isNumber(ch)) {
				int numberIndex = index;
				while (true) {
					if (numberIndex >= length || !isNumber(midstExpression.charAt(numberIndex))) {
						break;
					}
					numberIndex++;
				}
				String number = midstExpression.substring(index, numberIndex);
				result.add(number);
				index = numberIndex;
				continue;
			}
			result.add(now);
			index++;
			continue;
		}
		result.add(SignalEnum.EOF.signal);
		return result;
	}

	/**
	 * 中缀表达式转后缀表达式
	 * @param midstExpression
	 * @return
	 */
	public static String convert(String midstExpression) {
		List<String> list = sliceToList(midstExpression);
		//表达式栈
		Stack<String> expressionStack = new Stack<>();
		//操作符栈
		Stack<String> operatorStack = new Stack<>();
		for (String now : list) {
			//如果不是操作符,则放入表达式栈
			if (SignalEnum.of(now) == null) {
				expressionStack.push(now);
				continue;
			}
			//对于操作符,遍历操作符栈,直到遇到比自己优先级低的操作符为止.之间遇到的操作符会有不同的操作.
			Stack<String> tempStack = new Stack<>();
			while (true) {
				if (operatorStack.isEmpty()) {
					recoverStack(tempStack, operatorStack);
					operatorStack.push(now);
					break;
				}
				String top = operatorStack.pop();
				if (compare(top, now)) {
					//如果操作符栈顶的操作符优先级低于当前操作符,则中止操作
					if (top.equals(SignalEnum.RIGHT_BRACKETS.signal)) {
						throw new RuntimeException("非法的右括号!");
					}
					tempStack.push(top);
					//直接中止
					recoverStack(tempStack, operatorStack);
					//将当前操作符也放入操作符栈
					operatorStack.push(now);
					break;
				}
				//左括号的操作
				if (top.equals(SignalEnum.LEFT_BRACKETS.signal)) {
					if (!now.equals(SignalEnum.RIGHT_BRACKETS.signal)) {
						//如果当前操作符不是右括号
						//则将其放入暂存栈中
						tempStack.push(top);
						//直接中止
						recoverStack(tempStack, operatorStack);
						//将当前操作符也放入操作符栈
						operatorStack.push(now);
					} else {
						//直接中止
						recoverStack(tempStack, operatorStack);
					}
					break;
				}
				//其他操作符执行各自的操作
				String second = expressionStack.pop();
				String first = expressionStack.pop();
				String expression = first + second + top;
				expressionStack.push(expression);
			}
		}
		if (!operatorStack.pop().equals(SignalEnum.EOF.signal)) {
			throw new RuntimeException("非法的操作符!");
		}
		return expressionStack.pop();
	}

	public static void main(String[] args) {
		System.out.println(convert("2 + (b - c * d) - 23"));

	}

}
