import java.util.Scanner;

public class HospitalPathFinder {
    static Scanner sc = new Scanner(System.in);

    static String[] departments = {"Reception", "OPD", "Emergency", "Lab", "Pharmacy", "Ward"};

    // 5. Hospital Path Finder Sub-menu   [DS: Array Traversal]
    static void pathMenu() {
        int c = -1;
        while (c != 0) {
            System.out.println("\n  --- Hospital Path Finder ---");
            System.out.println("  Departments: Reception, OPD, Emergency, Lab, Pharmacy, Ward");
            System.out.println("  1. Find Path");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");
            c = sc.nextInt(); sc.nextLine();
            if (c == 1) {
                System.out.print("  Current Department  : "); String from = sc.nextLine();
                System.out.print("  Destination Department: "); String to = sc.nextLine();
                findPath(from, to);
            } else if (c == 0) {
                break;
            }
        }
    }

    // Walks the departments array between the current and target index   [DS: Array Traversal]
    static void findPath(String from, String to) {
        int si = -1, ti = -1;
        for (int i = 0; i < departments.length; i++) {
            if (departments[i].equalsIgnoreCase(from)) si = i;
            if (departments[i].equalsIgnoreCase(to)) ti = i;
        }
        if (si == -1) {
            System.out.println("  >> Starting Department not found!");
            return;
        }
        if (ti == -1) {
            System.out.println("  >> Destination Department not found!");
            return;
        }
        if (si == ti) {
            System.out.println("  >> Already at " + to + "!");
            return;
        }
        System.out.println("\n  --- Routing Path ---");
        if (si < ti) {
            for (int i = si; i <= ti; i++) {
                System.out.println("  --> " + departments[i] + (i == ti ? " [DESTINATION]" : ""));
            }
            System.out.println("  Steps: " + (ti - si));
        } else {
            for (int i = si; i >= ti; i--) {
                System.out.println("  --> " + departments[i] + (i == ti ? " [DESTINATION]" : ""));
            }
            System.out.println("  Steps: " + (si - ti));
        }
        System.out.println("  --------------------\n");
    }
}
