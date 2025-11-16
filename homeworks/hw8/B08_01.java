public class B08_01 {
    
    static class Stack<T> {
        private Node<T> top;
        private int size;
        
        private static class Node<T> {
            T data;
            Node<T> next;
            
            Node(T data, Node<T> next) {
                this.data = data;
                this.next = next;
            }
        }
        
        public Stack() {
            this.top = null;
            this.size = 0;
        }
        
        public void push(T item) {
            top = new Node<>(item, top);
            size++;
        }
        
        public T pop() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            T data = top.data;
            top = top.next;
            size--;
            return data;
        }
        
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Stack is empty");
            }
            return top.data;
        }
        
        public boolean isEmpty() {
            return top == null;
        }
        
        public int size() {
            return size;
        }
    }
    
    public static void main(String[] args) {
        Stack<Integer> intStack = new Stack<>();
        intStack.push(10);
        intStack.push(20);
        intStack.push(30);
        
        System.out.println("Integer Stack:");
        while (!intStack.isEmpty()) {
            System.out.println(intStack.pop());
        }
        
        Stack<String> stringStack = new Stack<>();
        stringStack.push("First");
        stringStack.push("Second");
        stringStack.push("Third");
        
        System.out.println("\nString Stack:");
        while (!stringStack.isEmpty()) {
            System.out.println(stringStack.pop());
        }
        
        Stack<Double> doubleStack = new Stack<>();
        doubleStack.push(1.5);
        doubleStack.push(2.7);
        doubleStack.push(3.9);
        
        System.out.println("\nDouble Stack:");
        while (!doubleStack.isEmpty()) {
            System.out.println(doubleStack.pop());
        }
    }
}

