import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DoctorModule {
    static Scanner sc = new Scanner(System.in);

    static class DoctorNode {
        int id;
        String name;
        String specialization;
        double fee;
        DoctorNode next;

        DoctorNode(int id, String name, String specialization, double fee) {
            this.id = id;
            this.name = name;
            this.specialization = specialization;
            this.fee = fee;
            this.next = null;
        }
    }
    static DoctorNode doctorHead = null;

    static class AssignNode {
        int patientId;
        int doctorId;
        AssignNode next;

        AssignNode(int patientId, int doctorId) {
            this.patientId = patientId;
            this.doctorId = doctorId;
            this.next = null;
        }
    }
    static AssignNode assignHead = null;

    // Checks if a doctor ID already exists   [DS: Linked List Traversal]
    static boolean isDoctorIdExists(int id) {
        DoctorNode t = doctorHead;
        while (t != null) {
            if (t.id == id) return true;
            t = t.next;
        }
        return false;
    }

    // Looks up a doctor node by ID   [DS: Linked List Traversal]
    static DoctorNode getDoctorById(int id) {
        DoctorNode t = doctorHead;
        while (t != null) {
            if (t.id == id) return t;
            t = t.next;
        }
        return null;
    }

    // Inserts a new doctor node at the end of the list   [DS: Linked List]
    static void addDoctor(int id, String name, String spec, double fee) {
        DoctorNode node = new DoctorNode(id, name, spec, fee);
        if (doctorHead == null) doctorHead = node;
        else {
            DoctorNode t = doctorHead;
            while (t.next != null) t = t.next;
            t.next = node;
        }
    }

    // Removes a doctor node by ID   [DS: Linked List]
    static void removeDoctor(int id) {
        if (doctorHead == null) {
            System.out.println("  No doctors found!");
            return;
        }
        if (doctorHead.id == id) {
            doctorHead = doctorHead.next;
            System.out.println("  Doctor Removed!");
            return;
        }
        DoctorNode t = doctorHead;
        while (t.next != null) {
            if (t.next.id == id) {
                t.next = t.next.next;
                System.out.println("  Doctor Removed!");
                return;
            }
            t = t.next;
        }
        System.out.println("  Doctor with ID " + id + " not found.");
    }

    // Looks up the doctor currently assigned to a patient   [DS: Linked List Traversal]
    static int getAssignedDoctorId(int patientId) {
        AssignNode t = assignHead;
        while (t != null) {
            if (t.patientId == patientId) return t.doctorId;
            t = t.next;
        }
        return -1;
    }

    // Assigns a doctor to a patient (updates existing assignment if one already exists)   [DS: Linked List]
    static void assignDoctor(int patientId, int doctorId) {
        AssignNode t = assignHead;
        while (t != null) {
            if (t.patientId == patientId) {
                t.doctorId = doctorId;
                return;
            }
            t = t.next;
        }
        AssignNode node = new AssignNode(patientId, doctorId);
        if (assignHead == null) assignHead = node;
        else {
            t = assignHead;
            while (t.next != null) t = t.next;
            t.next = node;
        }
    }

    static void saveDoctors() {
        List<String> lines = new ArrayList<>();
        DoctorNode t = doctorHead;
        while (t != null) {
            lines.add(t.id + "," + t.name + "," + t.specialization + "," + t.fee);
            t = t.next;
        }
        FileManager.writeLines("doctors.txt", lines);
    }

    static void loadDoctors() {
        for (String line : FileManager.readLines("doctors.txt")) {
            String[] p = line.split(",", 4);
            if (p.length == 4) {
                int id = Integer.parseInt(p[0].trim());
                if (!isDoctorIdExists(id)) {
                    addDoctor(id, p[1].trim(), p[2].trim(), Double.parseDouble(p[3].trim()));
                }
            }
        }
    }

    static void saveAssignments() {
        List<String> lines = new ArrayList<>();
        AssignNode t = assignHead;
        while (t != null) {
            lines.add(t.patientId + "," + t.doctorId);
            t = t.next;
        }
        FileManager.writeLines("assignments.txt", lines);
    }

    static void loadAssignments() {
        for (String line : FileManager.readLines("assignments.txt")) {
            String[] p = line.split(",", 2);
            if (p.length == 2) {
                assignDoctor(Integer.parseInt(p[0].trim()), Integer.parseInt(p[1].trim()));
            }
        }
    }

    // Prints every doctor node   [DS: Linked List Traversal]
    static void displayDoctors() {
        if (doctorHead == null) {
            System.out.println("  No doctors found!");
            return;
        }
        System.out.println("\n  ID | Name | Specialization | Fee");
        DoctorNode t = doctorHead;
        while (t != null) {
            System.out.println("  " + t.id + " | Dr. " + t.name + " | " + t.specialization + " | " + t.fee + " PKR");
            t = t.next;
        }
    }

    // --- Module 4 Main Menu ---   [DS: Linked List (Doctors) + Linked List (Assignments)]
    static void module4Menu() {
        int choice = -1;
        while (choice != 9) {
            System.out.println("\n  +======================================+");
            System.out.println("  |     Module 4 - Doctor Management     |");
            System.out.println("  +======================================+");
            System.out.println("  |  1. Add Doctor                       |");
            System.out.println("  |  2. Remove Doctor                    |");
            System.out.println("  |  3. Display Doctors                  |");
            System.out.println("  |  4. Assign Doctor to Patient         |");
            System.out.println("  |  5. View Patient's Assigned Doctor   |");
            System.out.println("  |  9. Back to Main Menu                |");
            System.out.println("  +======================================+");
            System.out.print("  Enter choice: ");
            choice = sc.nextInt(); sc.nextLine();

            if (choice == 1) {
                System.out.print("  Doctor ID: "); int id = sc.nextInt(); sc.nextLine();
                while (isDoctorIdExists(id)) {
                    System.out.print("  ID exists! Enter different ID: ");
                    id = sc.nextInt(); sc.nextLine();
                }
                String name = PatientModule.readAlphaInput("  Name: ");
                String spec = PatientModule.readAlphaInput("  Specialization: ");
                System.out.print("  Fee: "); double fee = sc.nextDouble(); sc.nextLine();
                addDoctor(id, name, spec, fee);
                saveDoctors();
                System.out.println("  Doctor Added!");
            } else if (choice == 2) {
                System.out.print("  Doctor ID to Remove: "); int id = sc.nextInt(); sc.nextLine();
                removeDoctor(id);
                saveDoctors();
            } else if (choice == 3) {
                displayDoctors();
            } else if (choice == 4) {
                int pid = PatientModule.askRegisteredPatientId();
                if (doctorHead == null) {
                    System.out.println("  No doctors registered yet! Add a doctor first.");
                } else {
                    displayDoctors();
                    System.out.print("  Doctor ID to Assign: "); int did = sc.nextInt(); sc.nextLine();
                    while (!isDoctorIdExists(did)) {
                        System.out.print("  No doctor with that ID! Enter a different Doctor ID: ");
                        did = sc.nextInt(); sc.nextLine();
                    }
                    assignDoctor(pid, did);
                    saveAssignments();
                    System.out.println("  " + PatientModule.patientLabel(pid) + " assigned to Dr. " + getDoctorById(did).name + "!");
                }
            } else if (choice == 5) {
                int pid = PatientModule.askRegisteredPatientId();
                int did = getAssignedDoctorId(pid);
                if (did == -1) {
                    System.out.println("  No doctor assigned to " + PatientModule.patientLabel(pid) + " yet.");
                } else {
                    DoctorNode d = getDoctorById(did);
                    System.out.println("  " + PatientModule.patientLabel(pid) + " --> Dr. " + (d != null ? d.name + " (" + d.specialization + ")" : "Unknown"));
                }
            } else if (choice == 9) {
                System.out.println("  Returning to Main Menu...");
            }
        }
    }
}