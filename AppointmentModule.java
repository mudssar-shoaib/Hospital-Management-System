import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppointmentModule {
    static Scanner sc = new Scanner(System.in);

    static class QueueNode {
        int id;
        QueueNode next;

        QueueNode(int id) {
            this.id = id;
            this.next = null;
        }
    }

    // [DS: Queue (Linked List based)]
    static class SimpleQueue {
        QueueNode front;
        QueueNode rear;

        void add(int id) {
            QueueNode newNode = new QueueNode(id);
            if (rear == null) {
                front = newNode;
                rear = newNode;
            } else {
                rear.next = newNode;
                rear = newNode;
            }
        }

        int poll() {
            int id = front.id;
            front = front.next;
            if (front == null) {
                rear = null;
            }
            return id;
        }

        boolean isEmpty() {
            return front == null;
        }

        int size() {
            int count = 0;
            QueueNode temp = front;
            while (temp != null) {
                count++;
                temp = temp.next;
            }
            return count;
        }

        boolean contains(int id) {
            QueueNode temp = front;
            while (temp != null) {
                if (temp.id == id) return true;
                temp = temp.next;
            }
            return false;
        }

        void remove(int id) {
            if (front == null) return;
            if (front.id == id) {
                front = front.next;
                if (front == null) rear = null;
                return;
            }
            QueueNode temp = front;
            while (temp.next != null) {
                if (temp.next.id == id) {
                    temp.next = temp.next.next;
                    if (temp.next == null) rear = temp;
                    return;
                }
                temp = temp.next;
            }
        }
    }

    static SimpleQueue drainQueue = new SimpleQueue();
    static SimpleQueue waitQueue = new SimpleQueue();
    static SimpleQueue normalQueue = new SimpleQueue();

    static class EmergencyPatient {
        int id;
        int priority;
        EmergencyPatient next;

        EmergencyPatient(int id, int priority) {
            this.id = id;
            this.priority = priority;
            this.next = null;
        }

        String priorityLabel() {
            switch (priority) {
                case 1: return "CRITICAL";
                case 2: return "HIGH";
                case 3: return "MEDIUM";
                default: return "LOW";
            }
        }
    }

    // [DS: Priority Queue (sorted Linked List)]
    static class EmergencyPriorityQueue {
        EmergencyPatient front;

        void add(int id, int priority) {
            EmergencyPatient newNode = new EmergencyPatient(id, priority);
            if (front == null || priority < front.priority) {
                newNode.next = front;
                front = newNode;
                return;
            }
            EmergencyPatient temp = front;
            while (temp.next != null && temp.next.priority <= priority) {
                temp = temp.next;
            }
            newNode.next = temp.next;
            temp.next = newNode;
        }

        EmergencyPatient peek() {
            return front;
        }

        EmergencyPatient poll() {
            EmergencyPatient removed = front;
            if (front != null) {
                front = front.next;
            }
            return removed;
        }

        boolean isEmpty() {
            return front == null;
        }
    }

    static EmergencyPriorityQueue emergencyQueue = new EmergencyPriorityQueue();

    static void saveModule2Queues() {
        List<String> d = new ArrayList<>();
        for (QueueNode t = drainQueue.front; t != null; t = t.next) d.add(String.valueOf(t.id));
        FileManager.writeLines("drain_queue.txt", d);

        List<String> w = new ArrayList<>();
        for (QueueNode t = waitQueue.front; t != null; t = t.next) w.add(String.valueOf(t.id));
        FileManager.writeLines("wait_queue.txt", w);

        List<String> n = new ArrayList<>();
        for (QueueNode t = normalQueue.front; t != null; t = t.next) n.add(String.valueOf(t.id));
        FileManager.writeLines("normal_queue.txt", n);

        List<String> e = new ArrayList<>();
        for (EmergencyPatient t = emergencyQueue.front; t != null; t = t.next) e.add(t.id + "," + t.priority);
        FileManager.writeLines("emergency_queue.txt", e);
    }

    static void loadModule2Queues() {
        for (String line : FileManager.readLines("drain_queue.txt")) drainQueue.add(Integer.parseInt(line.trim()));
        for (String line : FileManager.readLines("wait_queue.txt")) waitQueue.add(Integer.parseInt(line.trim()));
        for (String line : FileManager.readLines("normal_queue.txt")) normalQueue.add(Integer.parseInt(line.trim()));
        for (String line : FileManager.readLines("emergency_queue.txt")) {
            String[] p = line.split(",", 2);
            if (p.length == 2) emergencyQueue.add(Integer.parseInt(p[0].trim()), Integer.parseInt(p[1].trim()));
        }
    }

    static int askPriority() {
        System.out.println("  Priority Level: 1.Critical  2.High  3.Medium  4.Low");
        System.out.print("  Choice: ");
        int p = sc.nextInt();
        sc.nextLine();
        while (p < 1 || p > 4) {
            System.out.print("  Invalid! Enter 1-4: ");
            p = sc.nextInt();
            sc.nextLine();
        }
        return p;
    }

    static void displayEmergencyByPriority(EmergencyPriorityQueue queue, String label) {
        if (queue.isEmpty()) {
            System.out.println("  " + label + " Empty!");
            return;
        }
        System.out.println("\n  " + label + ":");
        int i = 1;
        for (EmergencyPatient t = queue.front; t != null; t = t.next) {
            System.out.println("  " + i + ". " + PatientModule.patientLabel(t.id) + " [" + t.priorityLabel() + "]");
            i++;
        }
    }

    static void module2Menu() {
        if (PatientModule.mainList.head == null) {
            System.out.println("\n  No patients found! Please add patients in Module 1 first.");
            PatientModule.module1Menu();
            return;
        }

        int choice = -1;
        while (choice != 9) {
            System.out.println("\n  +======================================+");
            System.out.println("  |  Module 2 - Appointment & Emergency  |");
            System.out.println("  +======================================+");
            System.out.println("  |  1. Drain and Rebuild Queue          |");
            System.out.println("  |  2. Estimate Wait Time               |");
            System.out.println("  |  3. Peek Next Emergency Patient      |");
            System.out.println("  |  4. Promote Patient to Emergency     |");
            System.out.println("  |  9. Back to Main Menu                |");
            System.out.println("  +======================================+");
            System.out.print("  Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.println("\n  -- Drain and Rebuild Queue --");
                System.out.println("  1. Add Patient   2. Drain Queue   3. Rebuild Queue   4. Display");
                System.out.print("  Choice: ");
                int c = sc.nextInt();
                sc.nextLine();

                if (c == 1) {
                    int id = PatientModule.askRegisteredPatientId();
                    drainQueue.add(id);
                    System.out.println("  Patient Added.");
                } else if (c == 2) {
                    if (drainQueue.isEmpty()) {
                        System.out.println("  Queue Already Empty!");
                    } else {
                        while (!drainQueue.isEmpty()) {
                            System.out.println("  Removed: " + PatientModule.patientLabel(drainQueue.poll()));
                        }
                        System.out.println("  Queue Drained!");
                    }
                } else if (c == 3) {
                    int id = PatientModule.askRegisteredPatientId();
                    drainQueue.add(id);
                    System.out.println("  " + PatientModule.patientLabel(id) + " added for Next Shift.");
                } else if (c == 4) {
                    if (drainQueue.isEmpty()) {
                        System.out.println("  Queue Empty!");
                    } else {
                        int i = 1;
                        for (QueueNode t = drainQueue.front; t != null; t = t.next) {
                            System.out.println("  " + i + ". " + PatientModule.patientLabel(t.id));
                            i++;
                        }
                    }
                }
                saveModule2Queues();

            } else if (choice == 2) {
                if (waitQueue.isEmpty()) {
                    System.out.println("  No Patients in Queue.");
                } else {
                    int p = waitQueue.size();
                    System.out.println("  Patients Waiting : " + p);
                    System.out.println("  Estimated Wait   : " + (p * 10) + " Minutes");
                }

            } else if (choice == 3) {
                System.out.println("\n  1. Add Patient   2. Peek   3. Remove   4. Display");
                System.out.print("  Choice: ");
                int c = sc.nextInt();
                sc.nextLine();

                if (c == 1) {
                    int id = PatientModule.askRegisteredPatientId();
                    int p = askPriority();
                    emergencyQueue.add(id, p);
                    System.out.println("  Patient Added.");
                } else if (c == 2) {
                    if (emergencyQueue.isEmpty()) System.out.println("  Queue Empty!");
                    else {
                        EmergencyPatient top = emergencyQueue.peek();
                        System.out.println("  Next: " + PatientModule.patientLabel(top.id) + " [" + top.priorityLabel() + "]");
                    }
                } else if (c == 3) {
                    if (emergencyQueue.isEmpty()) System.out.println("  Queue Empty!");
                    else {
                        EmergencyPatient removed = emergencyQueue.poll();
                        System.out.println("  Removed: " + PatientModule.patientLabel(removed.id));
                    }
                } else if (c == 4) {
                    displayEmergencyByPriority(emergencyQueue, "Emergency Queue");
                }
                saveModule2Queues();

            } else if (choice == 4) {
                System.out.print("  Patient ID to Promote: ");
                int id = sc.nextInt();
                sc.nextLine();
                if (!normalQueue.contains(id)) {
                    System.out.println("  Patient not found in Normal Queue!");
                } else {
                    normalQueue.remove(id);
                    int p = askPriority();
                    emergencyQueue.add(id, p);
                    System.out.println("  Promoted to Emergency!");
                }
                saveModule2Queues();

            } else if (choice == 9) {
                System.out.println("  Returning to Main Menu...");
            }
        }
    }
}