import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LabTestQueue {
    static Scanner sc = new Scanner(System.in);

    // Lab Queue Node
    static class LabNode {
        int patientId;
        String test;
        LabNode next;

        LabNode(int patientId, String test) {
            this.patientId = patientId;
            this.test = test;
            this.next = null;
        }
    }
    static LabNode normalLabFront = null;
    static LabNode normalLabRear = null;
    static LabNode emergencyLabFront = null;
    static LabNode emergencyLabRear = null;

    static void saveLabTests() {
        List<String> lines = new ArrayList<>();
        LabNode t = emergencyLabFront;
        while (t != null) {
            lines.add("EMERGENCY," + t.patientId + "," + t.test);
            t = t.next;
        }
        t = normalLabFront;
        while (t != null) {
            lines.add("NORMAL," + t.patientId + "," + t.test);
            t = t.next;
        }
        FileManager.writeLines("labtests.txt", lines);
    }

    static void loadLabTests() {
        normalLabFront = null; normalLabRear = null;
        emergencyLabFront = null; emergencyLabRear = null;
        for (String line : FileManager.readLines("labtests.txt")) {
            String[] p = line.split(",", 3);
            if (p.length == 3) {
                int id = Integer.parseInt(p[1].trim());
                String test = p[2].trim();
                LabNode n = new LabNode(id, test);
                if (p[0].trim().equals("EMERGENCY")) {
                    if (emergencyLabRear == null) { emergencyLabFront = n; emergencyLabRear = n; }
                    else { emergencyLabRear.next = n; emergencyLabRear = n; }
                } else {
                    if (normalLabRear == null) { normalLabFront = n; normalLabRear = n; }
                    else { normalLabRear.next = n; normalLabRear = n; }
                }
            }
        }
    }

    // 3. Lab Test Queue Sub-menu   [DS: Queue (Linked List) - Normal + Emergency]
    static void labMenu() {
        int c = -1;
        while (c != 0) {
            System.out.println("\n  --- Lab Test Queue ---");
            System.out.println("  1. Add Normal Lab Test");
            System.out.println("  2. Add Emergency Lab Test");
            System.out.println("  3. Process Lab Test");
            System.out.println("  4. Display Lab Queues");
            System.out.println("  5. Estimate Wait Time");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");
            c = sc.nextInt(); sc.nextLine();
            if (c == 1) {
                int id = PatientModule.askRegisteredPatientId();
                System.out.print("  Test Name : "); String t = sc.nextLine();
                LabNode n = new LabNode(id, t);
                if (normalLabRear == null) { normalLabFront = n; normalLabRear = n; }
                else { normalLabRear.next = n; normalLabRear = n; }
                saveLabTests();
                System.out.println("  >> Normal Lab Test Added.");
            } else if (c == 2) {
                int id = PatientModule.askRegisteredPatientId();
                System.out.print("  Test Name : "); String t = sc.nextLine();
                LabNode n = new LabNode(id, t);
                if (emergencyLabRear == null) { emergencyLabFront = n; emergencyLabRear = n; }
                else { emergencyLabRear.next = n; emergencyLabRear = n; }
                saveLabTests();
                System.out.println("  >> Emergency Lab Test Added.");
            } else if (c == 3) {
                // Emergency is ALWAYS checked first, even if patients are waiting in Normal
                if (emergencyLabFront != null) {
                    System.out.println("  >> Processing Emergency Lab for Patient ID " + emergencyLabFront.patientId + " (" + emergencyLabFront.test + ")");
                    emergencyLabFront = emergencyLabFront.next;
                    if (emergencyLabFront == null) emergencyLabRear = null;
                    saveLabTests();
                } else if (normalLabFront != null) {
                    System.out.println("  >> Processing Normal Lab for Patient ID " + normalLabFront.patientId + " (" + normalLabFront.test + ")");
                    normalLabFront = normalLabFront.next;
                    if (normalLabFront == null) normalLabRear = null;
                    saveLabTests();
                } else {
                    System.out.println("  >> Both Lab Queues are Empty!");
                }
            } else if (c == 4) {
                System.out.println("\n  --- Emergency Lab Queue ---");
                LabNode tempE = emergencyLabFront;
                while (tempE != null) {
                    System.out.println("  Patient ID: " + tempE.patientId + " | Test: " + tempE.test);
                    tempE = tempE.next;
                }
                System.out.println("\n  --- Normal Lab Queue ---");
                LabNode tempN = normalLabFront;
                while (tempN != null) {
                    System.out.println("  Patient ID: " + tempN.patientId + " | Test: " + tempN.test);
                    tempN = tempN.next;
                }
            } else if (c == 5) {
                System.out.print("  Patient ID: ");
                int id = sc.nextInt(); sc.nextLine();
                int pos = 0;
                boolean found = false;
                LabNode t = emergencyLabFront;
                while (t != null) {
                    pos++;
                    if (t.patientId == id) {
                        System.out.println("  >> Position: " + pos + "  Estimated Wait: " + (pos * 15) + " min [Emergency Queue]");
                        found = true;
                        break;
                    }
                    t = t.next;
                }
                if (!found) {
                    int emergencyCount = pos;
                    t = normalLabFront;
                    while (t != null) {
                        pos++;
                        if (t.patientId == id) {
                            System.out.println("  >> Position: " + (pos - emergencyCount) + "  Estimated Wait: " + (pos * 15) + " min [Normal Queue]");
                            found = true;
                            break;
                        }
                        t = t.next;
                    }
                }
                if (!found) {
                    System.out.println("  >> Patient not found in either lab queue!");
                }
            } else if (c == 0) {
                break;
            }
        }
    }
}
