import java.util.Scanner;

public class PharmacyModule {
    static Scanner sc = new Scanner(System.in);

    // --- Module 3 Main Menu (routes to the 6 separate files below) ---
    static void module3Menu() {
        int choice = -1;
        while (choice != 9) {
            System.out.println("\n  +======================================+");
            System.out.println("  |  Module 3 - Pharmacy & Med Records   |");
            System.out.println("  +======================================+");
            System.out.println("  |  1. Medicine Inventory               |");
            System.out.println("  |  2. Prescription Undo System         |");
            System.out.println("  |  3. Lab Test Queue                   |");
            System.out.println("  |  4. Medicine Search                  |");
            System.out.println("  |  5. Hospital Path Finder             |");
            System.out.println("  |  6. Generate Bill                    |");
            System.out.println("  |  9. Back to Main Menu                |");
            System.out.println("  +======================================+");
            System.out.print("  Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) MedicineInventory.medInventoryMenu();
            else if (choice == 2) PrescriptionUndo.prescMenu();
            else if (choice == 3) LabTestQueue.labMenu();
            else if (choice == 4) MedicineSearch.medSearchMenu();
            else if (choice == 5) HospitalPathFinder.pathMenu();
            else if (choice == 6) Billing.generateBill();
            else if (choice == 9) System.out.println("  Returning to Main Menu...");
            else System.out.println("  Invalid choice!");
        }
    }
}
