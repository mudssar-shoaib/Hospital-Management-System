import java.util.Scanner;

public class MedicineSearch {
    static Scanner sc = new Scanner(System.in);

    // Arrays for Binary Search
    static String[] medArr = new String[100];
    static int[] medQty = new int[100];
    static double[] medPrice = new double[100];
    static int medCount = 0;

    // Appends a new entry at the end of the search array   [DS: Array]
    static void addMedArr(String name, int qty, double price) {
        if (medCount < medArr.length) {
            medArr[medCount] = name;
            medQty[medCount] = qty;
            medPrice[medCount] = price;
            medCount++;
        }
    }

    // Bubble sort so the array stays alphabetical for Binary Search   [DS: Array - Bubble Sort]
    static void sortMedArr() {
        for (int i = 0; i < medCount - 1; i++) {
            for (int j = 0; j < medCount - i - 1; j++) {
                if (medArr[j].compareTo(medArr[j + 1]) > 0) {
                    String tn = medArr[j]; medArr[j] = medArr[j + 1]; medArr[j + 1] = tn;
                    int tq = medQty[j]; medQty[j] = medQty[j + 1]; medQty[j + 1] = tq;
                    double tp = medPrice[j]; medPrice[j] = medPrice[j + 1]; medPrice[j + 1] = tp;
                }
            }
        }
    }

    // Removes one entry from the search array (used so Inventory's Remove stays linked to Search too)   [DS: Array]
    static void removeMedArr(String name) {
        int idx = -1;
        for (int i = 0; i < medCount; i++) {
            if (medArr[i].equalsIgnoreCase(name)) { idx = i; break; }
        }
        if (idx == -1) return;
        for (int i = idx; i < medCount - 1; i++) {
            medArr[i] = medArr[i + 1];
            medQty[i] = medQty[i + 1];
            medPrice[i] = medPrice[i + 1];
        }
        medCount--;
    }

    // 4. Medicine Search Sub-menu   [DS: Sorted Array + Binary Search]
    static void medSearchMenu() {
        int c = -1;
        while (c != 0) {
            System.out.println("\n  --- Medicine Search ---");
            System.out.println("  1. Search Medicine (Binary Search)");
            System.out.println("  2. Low Stock Alert");
            System.out.println("  3. Most Expensive Medicine");
            System.out.println("  4. Restock Medicine");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");
            c = sc.nextInt(); sc.nextLine();
            if (c == 1) {
                System.out.print("  Enter Medicine Name to Search: ");
                String n = sc.nextLine();
                int l = 0, r = medCount - 1;
                boolean found = false;
                while (l <= r) {
                    int m = (l + r) / 2;
                    int res = medArr[m].compareToIgnoreCase(n);
                    if (res == 0) {
                        System.out.println("  >> Found! " + medArr[m] + " | Qty: " + medQty[m] + " | Price: " + medPrice[m] + " PKR");
                        found = true;
                        break;
                    } else if (res < 0) {
                        l = m + 1;
                    } else {
                        r = m - 1;
                    }
                }
                if (!found) {
                    System.out.println("  >> Medicine not found in inventory!");
                }
            } else if (c == 2) {
                System.out.println("\n  --- Low Stock Alert (Qty < 20) ---");
                boolean any = false;
                for (int i = 0; i < medCount; i++) {
                    if (medQty[i] < 20) {
                        System.out.println("  WARNING: " + medArr[i] + " | Qty: " + medQty[i]);
                        any = true;
                    }
                }
                if (!any) System.out.println("  >> All medicines are well stocked!");
            } else if (c == 3) {
                if (medCount == 0) {
                    System.out.println("  >> No medicines found.");
                } else {
                    int mx = 0;
                    for (int i = 1; i < medCount; i++) {
                        if (medPrice[i] > medPrice[mx]) mx = i;
                    }
                    System.out.println("  >> Most Expensive: " + medArr[mx] + " | " + medPrice[mx] + " PKR");
                }
            } else if (c == 4) {
                System.out.print("  Medicine to Restock: "); String n = sc.nextLine();
                if (n.trim().isEmpty()) {
                    System.out.println("  >> Medicine name cannot be empty!");
                    continue;
                }
                System.out.print("  Qty to Add: "); int q = sc.nextInt(); sc.nextLine();
                if (q <= 0) {
                    System.out.println("  >> Quantity to add must be greater than 0!");
                    continue;
                }
                boolean foundMed = false;
                for (int i = 0; i < medCount; i++) {
                    if (medArr[i].equalsIgnoreCase(n)) {
                        int old = medQty[i];
                        medQty[i] += q;
                        System.out.println("  >> Restocked! " + old + " + " + q + " = " + medQty[i]);
                        MedicineInventory.MedNode t = MedicineInventory.medHead;
                        while (t != null) {
                            if (t.name.equalsIgnoreCase(n)) { t.qty = medQty[i]; break; }
                            t = t.next;
                        }
                        MedicineInventory.saveMedicines();
                        foundMed = true;
                        break;
                    }
                }
                if (!foundMed) System.out.println("  >> Medicine not found!");
            } else if (c == 0) {
                break;
            }
        }
    }
}
