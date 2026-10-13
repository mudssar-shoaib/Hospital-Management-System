import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrescriptionUndo {
    static Scanner sc = new Scanner(System.in);

    // Prescription Stack Node (Undo System)
    static class PrescNode {
        int patientId;
        String medicine;
        PrescNode next;

        PrescNode(int patientId, String medicine) {
            this.patientId = patientId;
            this.medicine = medicine;
            this.next = null;
        }
    }
    static PrescNode prescTop = null;

    static void savePrescriptions() {
        List<String> lines = new ArrayList<>();
        PrescNode t = prescTop;
        while (t != null) {
            lines.add(t.patientId + "," + t.medicine);
            t = t.next;
        }
        FileManager.writeLines("prescriptions.txt", lines);
    }

    // File is written top-of-stack first; on load we push back in reverse
    // order so the stack (LIFO order) comes back exactly as it was saved.
    static void loadPrescriptions() {
        prescTop = null;
        List<String> lines = FileManager.readLines("prescriptions.txt");
        for (int i = lines.size() - 1; i >= 0; i--) {
            String[] p = lines.get(i).split(",", 2);
            if (p.length == 2) {
                PrescNode n = new PrescNode(Integer.parseInt(p[0].trim()), p[1].trim());
                n.next = prescTop;
                prescTop = n;
            }
        }
    }

    // 2. Prescription Undo Sub-menu   [DS: Stack (Linked List)]
    static void prescMenu() {
        int c = -1;
        while (c != 0) {
            System.out.println("\n  --- Prescription Undo System ---");
            System.out.println("  1. Add Prescription");
            System.out.println("  2. Undo Last Prescription");
            System.out.println("  3. Display Prescriptions");
            System.out.println("  4. Count Total Prescriptions");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");
            c = sc.nextInt(); sc.nextLine();
            if (c == 1) {
                int id = PatientModule.askRegisteredPatientId();
                System.out.print("  Medicine Name : "); String m = sc.nextLine();
                PrescNode n = new PrescNode(id, m);
                n.next = prescTop;
                prescTop = n;
                savePrescriptions();
                System.out.println("  >> Prescription Added Successfully!");
            } else if (c == 2) {
                if (prescTop != null) {
                    prescTop = prescTop.next;
                    savePrescriptions();
                    System.out.println("  >> Last Prescription Undone Successfully!");
                } else {
                    System.out.println("  >> No prescriptions to undo!");
                }
            } else if (c == 3) {
                if (prescTop == null) {
                    System.out.println("  >> No prescription records found.");
                } else {
                    System.out.println("\n  ----- Prescription Records (Top to Bottom) -----");
                    PrescNode temp = prescTop;
                    int count = 1;
                    while (temp != null) {
                        System.out.println("  " + count + ". Patient ID: " + temp.patientId + " | Medicine: " + temp.medicine);
                        temp = temp.next;
                        count++;
                    }
                    System.out.println("--------------------------------------------------\n");
                }
            } else if (c == 4) {
                int count = 0;
                PrescNode t = prescTop;
                while (t != null) {
                    count++;
                    t = t.next;
                }
                System.out.println("  >> Total Prescriptions: " + count);
            } else if (c == 0) {
                break;
            }
        }
    }
}
