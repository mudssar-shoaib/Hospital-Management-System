import java.util.Scanner;

public class HospitalManagementSystem {
    static Scanner sc = new Scanner(System.in);

    static String[] empNames = {"Mueez", "Ali", "Ali", "Mohsin"};
    static String[] empIDs   = {"E1", "E2", "E3", "E4"};
    static String[] empPass  = {"1212", "2121", "4321", "1234"};

    static String loggedInName = "";
    static String loggedInID   = "";

    static boolean login() {
        System.out.println("\n  +======================================+");
        System.out.println("  |     HOSPITAL MANAGEMENT SYSTEM       |");
        System.out.println("  |            Staff Login               |");
        System.out.println("  +======================================+");

        System.out.print("\n  Enter Employee ID   : ");
        String id = sc.nextLine();
        System.out.print("  Enter Password      : ");
        String pass = sc.nextLine();

        for (int i = 0; i < empIDs.length; i++) {
            if (empIDs[i].equals(id) && empPass[i].equals(pass)) {
                loggedInName = empNames[i];
                loggedInID   = empIDs[i];
                System.out.println("\n  Welcome, " + loggedInName + "! (" + loggedInID + ")");
                return true;
            }
        }
        System.out.println("\n  Invalid ID or Password! Try again.");
        return false;
    }

    static void mainMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n  +======================================+");
            System.out.println("  |     HOSPITAL MANAGEMENT SYSTEM       |");
            System.out.println("  |     Logged in: " + loggedInName + " (" + loggedInID + ")        ");
            System.out.println("  +======================================+");
            System.out.println("  |  1. Patient Management    (Ali)      |");
            System.out.println("  |  2. Appointment & Emergency (Mueez)  |");
            System.out.println("  |  3. Pharmacy & Medical Records       |");
            System.out.println("  |     (Mohsin)                         |");
            System.out.println("  |  4. Doctor Management                |");
            System.out.println("  |  0. Logout                           |");
            System.out.println("  +======================================+");
            System.out.print("  Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                PatientModule.module1Menu();
            } else if (choice == 2) {
                AppointmentModule.module2Menu();
            } else if (choice == 3) {
               PharmacyModule.module3Menu();
            } else if (choice == 4) {
                DoctorModule.module4Menu();
            } else if (choice == 0) {
                System.out.println("\n  Logged out. Allah Hafiz, " + loggedInName + "!");
            } else {
                System.out.println("\n  Invalid choice!");
            }
        }
    }

    static void loadAllData() {
        PatientModule.loadPatients();
        AppointmentModule.loadModule2Queues();
        MedicineInventory.loadMedicines();
        PrescriptionUndo.loadPrescriptions();
        LabTestQueue.loadLabTests();
        DoctorModule.loadDoctors();
        DoctorModule.loadAssignments();
    }

    public static void main(String[] args) {
        loadAllData();
        boolean loggedIn = false;
        int attempts = 0;
        while (!loggedIn && attempts < 3) {
            loggedIn = login();
            if (!loggedIn) {
                attempts++;
            }
            if (attempts == 3) {
                System.out.println("\n  Too many failed attempts! Exiting...");
                return;
            }
        }
        mainMenu();
    }
}