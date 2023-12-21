import java.util.*;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int size = scanner.nextInt();
        Set<String> uniqueStrings = new HashSet<>();

        // Read strings and add them to the set
        for (int i = 0; i < size; i++) {
            uniqueStrings.add(scanner.next());
        }

        // Convert the set to a list and sort it lexicographically
        List<String> sortedStrings = new ArrayList<>(uniqueStrings);
        Collections.sort(sortedStrings);

        // Print the sorted strings without duplicates
        for (String s : sortedStrings) {
            System.out.println(s);
        }
    }
}
