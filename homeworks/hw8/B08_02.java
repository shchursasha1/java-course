import java.util.Stack;
import java.util.HashMap;
import java.util.Map;

public class B08_02 {
    
    public static boolean isValidBrackets(String str) {
        Stack<Character> stack = new Stack<>();
        Map<Character, Character> brackets = new HashMap<>();
        brackets.put(')', '(');
        brackets.put(']', '[');
        brackets.put('}', '{');
        
        for (char ch : str.toCharArray()) {
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else if (ch == ')' || ch == ']' || ch == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char top = stack.pop();
                if (top != brackets.get(ch)) {
                    return false;
                }
            }
        }
        
        return stack.isEmpty();
    }
    
    public static void main(String[] args) {
        String[] testCases = {
            "()",
            "()[]{}",
            "(]",
            "([)]",
            "{[]}",
            "((()))",
            "({[()]})",
            "(()",
            "())",
            "{[(])}",
            "",
            "(([])){}"
        };
        
        for (String test : testCases) {
            System.out.println("\"" + test + "\" -> " + (isValidBrackets(test) ? "Valid" : "Invalid"));
        }
    }
}

