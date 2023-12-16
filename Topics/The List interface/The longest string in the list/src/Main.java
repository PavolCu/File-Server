import java.util.*;

public class Main {

    static void changeList(List<String> list) {
        // write your code here
        if (!list.isEmpty()) {
            String longest = list.get(0);
            for (String s : list) {
                if (s.length() > longest.length()) {
                    longest = s;
                }
            }
            Collections.fill(list, longest);
        }
    }

    /* Do not change code below */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        List<String> lst = Arrays.asList(s.split(" "));
        changeList(lst);
        lst.forEach(e -> System.out.print(e + " "));
    }
}