import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MedicineInventory {
    static Scanner sc = new Scanner(System.in);

    // Medicine Inventory Node
    static class MedNode {
        String name;
        int qty;
        double price;
        MedNode next;

        MedNode(String name, int qty, double price) {
            this.name = name;
            this.qty = qty;
            this.price = price;
            this.next = null;
        }
    }
    static MedNode medHead = null;

    // --- File Handling Save & Load ---
    static void saveMedicines() {
        List<String> lines = new ArrayList<>();
        MedNode t = medHead;
        while (t != null) {
            lines.add(t.name + "," + t.qty + "," + t.price);
            t = t.next;
        }
        FileManager.writeLines("medicines.txt", lines);
    }

    // Rebuilds both this Linked List AND MedicineSearch's array from the same file,
    // so Inventory and Search always start back up in sync with each other.
    static void loadMedicines() {
        medHead = null;
        MedicineSearch.medCount = 0;
        for (String line : FileManager.readLines("medicines.txt")) {
            String[] p = line.split(",", 3);
            if (p.length == 3) {
                addMed(p[0].trim(), Integer.parseInt(p[1].trim()), Double.parseDouble(p[2].trim()));
                MedicineSearch.addMedArr(p[0].trim(), Integer.parseInt(p[1].trim()), Double.parseDouble(p[2].trim()));
            }
        }
        MedicineSearch.sortMedArr();
    }

    // Inserts a new medicine node at the end of the inventory list   [DS: Linked List]
    static void addMed(String name, int qty, double price) {
        MedNode newNode = new MedNode(name, qty, price);
        if (medHead == null) medHead = newNode;
        else {
            MedNode t = medHead;
            while (t.next != null) t = t.next;
            t.next = newNode;
        }
    }

    // Removes from BOTH the inventory linked list AND the search array, so the two stay linked   [DS: Linked List + Array]
    static void removeMed(String name) {
        if (medHead == null) {
            System.out.println("  >> Inventory is empty.");
            return;
        }
        MedicineSearch.removeMedArr(name);
        if (medHead.name.equalsIgnoreCase(name)) {
            medHead = medHead.next;
            System.out.println("  >> Medicine Removed Successfully!");
            return;
        }
        MedNode t = medHead;
        while (t.next != null) {
            if (t.next.name.equalsIgnoreCase(name)) {
                t.next = t.next.next;
                System.out.println("  >> Medicine Removed Successfully!");
                return;
            }
            t = t.next;
        }
        System.out.println("  >> Medicine not found!");
    }

    // Adds up qty * price for every node in the inventory   [DS: Linked List Traversal]
    static void totalInventoryValue() {
        if (medHead == null) {
            System.out.println("  >> Inventory is empty.");
            return;
        }
        double total = 0;
        System.out.println("\n  ----- Inventory Value -----");
        MedNode t = medHead;
        while (t != null) {
            double value = t.qty * t.price;
            System.out.println("  " + t.name + " = " + value + " PKR");
            total += value;
            t = t.next;
        }
        System.out.println("  Total Value: " + total + " PKR\n");
    }

    // Looks up a medicine's price from the inventory list (used by Billing)   [DS: Linked List Traversal]
    static double findMedPrice(String name) {
        MedNode t = medHead;
        while (t != null) {
            if (t.name.equalsIgnoreCase(name)) return t.price;
            t = t.next;
        }
        return -1;
    }

    // 1. Medicine Inventory Sub-menu   [DS: Linked List]
    static void medInventoryMenu() {
        int c = -1;
        while (c != 0) {
            System.out.println("\n  --- Medicine Inventory ---");
            System.out.println("  1. Add Medicine");
            System.out.println("  2. Display Medicines");
            System.out.println("  3. Remove Medicine");
            System.out.println("  4. Total Inventory Value");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");
            c = sc.nextInt();
            sc.nextLine();
            if (c == 1) {
                System.out.print("  Medicine Name : "); String n = sc.nextLine();
                System.out.print("  Quantity      : "); int q = sc.nextInt();
                System.out.print("  Price (PKR)   : "); double p = sc.nextDouble(); sc.nextLine();
                addMed(n, q, p);
                MedicineSearch.addMedArr(n, q, p);
                MedicineSearch.sortMedArr();
                saveMedicines();
                System.out.println("  >> Medicine Added Successfully! (also linked to Medicine Search)");
            } else if (c == 2) {
                if (medHead == null) {
                    System.out.println("  >> Inventory is empty.");
                } else {
                    System.out.println("\n  ----- Medicine Stock -----");
                    MedNode t = medHead;
                    int i = 1;
                    while (t != null) {
                        System.out.println("  " + i + ". " + t.name + " | Qty: " + t.qty + " | Price: " + t.price + " PKR");
                        t = t.next;
                        i++;
                    }
                    System.out.println("---------------------------\n");
                }
            } else if (c == 3) {
                System.out.print("  Medicine Name to Remove: "); String n = sc.nextLine();
                removeMed(n);
                saveMedicines();
            } else if (c == 4) {
                totalInventoryValue();
            } else if (c == 0) {
                break;
            }
        }
    }
}
