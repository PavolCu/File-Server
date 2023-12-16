import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        //String input = "<html><a>hello</a><h1><h4>nestedHello</h4><h3>nestedWorld</h3><h6><br>top</br></h6></h1><br>world</br></html>";
        processNestedData(input);
    }

    private static void processNestedData(String input) {
        Deque<String> tagDeque = new LinkedList<>();
        Deque<String> contentDeque = new LinkedList<>();
        contentDeque.push("");

        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);

            if (ch == '<') {
                if (input.charAt(i + 1) == '/') {
                    String tag = tagDeque.pop();
                    String content = contentDeque.pop();
                    System.out.println(content);
                    if (!contentDeque.isEmpty()) {
                        contentDeque.push(contentDeque.pop() + "<" + tag + ">" + content + "</" + tag + ">");
                    }
                    i += tag.length() + 2; // Skip over the closing tag
                } else {
                    int end = input.indexOf('>', i);
                    String tag = input.substring(i + 1, end);
                    tagDeque.push(tag);
                    contentDeque.push("");
                    i = end;
                }
            } else if (ch != '>') {
                contentDeque.push(contentDeque.pop() + ch);
            }
        }
    }
}