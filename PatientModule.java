import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PatientModule {
    static Scanner sc = new Scanner(System.in);
    static PatientList mainList = new PatientList();

    static class Patient {
        int id;
        String name;
        int age;
        String ward;
        Patient next;

        Patient(int id, String name, int age, String ward) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.ward = ward;
            this.next = null;
        }

        void display() {
            System.out.println("ID: " + id + "  |  Name: " + name + "  |  Age: " + age + "  |  Ward: " + ward);
        }
    }

    static class PatientList {
        Patient head;
        int size;

        PatientList() {
            head = null;
            size = 0;
        }

        // [DS: Linked List Traversal]
        boolean isIdExists(int id) {
            Patient temp = head;
            while (temp != null) {
                if (temp.id == id) return true;
                temp = temp.next;
            }
            return false;
        }

        Patient getPatientById(int id) {
            Patient temp = head;
            while (temp != null) {
                if (temp.id == id) return temp;
                temp = temp.next;
            }
            return null;
        }

        void addPatient(int id, String name, int age, String ward) {
            Patient newPatient = new Patient(id, name, age, ward);
            if (head == null) {
                head = newPatient;
            } else {
                Patient temp = head;
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = newPatient;
            }
            size++;
            System.out.println(">> Patient Added Successfully!");
        }

        void displayAll() {
            if (head == null) {
                System.out.println(">> No patient records found.");
                return;
            }
            System.out.println("\n----- Patient List (Admission Order) -----");
            Patient temp = head;
            int count = 1;
            while (temp != null) {
                System.out.print(count + ". ");
                temp.display();
                temp = temp.next;
                count++;
            }
            System.out.println("-------------------------------------------\n");
        }

        void updatePatient(int id, String name, int age, String ward) {
            Patient temp = head;
            while (temp != null) {
                if (temp.id == id) {
                    temp.name = name;
                    temp.age = age;
                    temp.ward = ward;
                    System.out.println(">> Patient Updated Successfully!");
                    return;
                }
                temp = temp.next;
            }
            System.out.println(">> Patient with ID " + id + " not found.");
        }

        void deletePatient(int id) {
            if (head == null) {
                System.out.println(">> List is empty.");
                return;
            }
            if (head.id == id) {
                head = head.next;
                size--;
                System.out.println(">> Patient Deleted Successfully!");
                return;
            }
            Patient prev = head;
            Patient curr = head.next;
            while (curr != null) {
                if (curr.id == id) {
                    prev.next = curr.next;
                    size--;
                    System.out.println(">> Patient Deleted Successfully!");
                    return;
                }
                prev = curr;
                curr = curr.next;
            }
            System.out.println(">> Patient with ID " + id + " not found.");
        }

        PatientList getPatientsByWard(String wardName) {
            PatientList result = new PatientList();
            Patient temp = head;
            while (temp != null) {
                if (temp.ward.trim().equalsIgnoreCase(wardName.trim())) {
                    result.addPatientQuietly(temp.id, temp.name, temp.age, temp.ward);
                }
                temp = temp.next;
            }
            return result;
        }

        void addPatientQuietly(int id, String name, int age, String ward) {
            Patient newPatient = new Patient(id, name, age, ward);
            if (head == null) {
                head = newPatient;
            } else {
                Patient temp = head;
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = newPatient;
            }
            size++;
        }

        // [DS: Linked List Merge]
        void mergeLists(PatientList otherList) {
            if (this.head == null) {
                this.head = otherList.head;
            } else {
                Patient temp = this.head;
                while (temp.next != null) {
                    temp = temp.next;
                }
                temp.next = otherList.head;
            }
            this.size += otherList.size;
            System.out.println(">> Two Patient Lists Merged Successfully!");
        }

        // [DS: Array - Nested Loop Comparison]
        void detectDuplicateID() {
            if (size == 0) {
                System.out.println(">> No patients to check.");
                return;
            }
            int[] idArray = new int[size];
            Patient temp = head;
            int index = 0;
            while (temp != null) {
                idArray[index] = temp.id;
                index++;
                temp = temp.next;
            }
            boolean duplicateFound = false;
            for (int i = 0; i < idArray.length; i++) {
                for (int j = i + 1; j < idArray.length; j++) {
                    if (idArray[i] == idArray[j]) {
                        System.out.println(">> Duplicate Patient ID Found: " + idArray[i]);
                        duplicateFound = true;
                    }
                }
            }
            if (!duplicateFound) {
                System.out.println(">> No Duplicate Patient ID Found.");
            }
        }

        // [DS: Linked List In-place Reversal]
        void reverseAdmissionOrder() {
            Patient prev = null;
            Patient curr = head;
            Patient next;
            while (curr != null) {
                next = curr.next;
                curr.next = prev;
                prev = curr;
                curr = next;
            }
            head = prev;
            System.out.println(">> Patient Admission Order Reversed Successfully!");
        }

        // [DS: Linked List - Slow/Fast Pointer]
        void findMiddlePatient() {
            if (head == null) {
                System.out.println(">> No patients in the list.");
                return;
            }
            Patient slow = head;
            Patient fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
            }
            System.out.println(">> Middle Patient is:");
            slow.display();
        }
    }

    static int askRegisteredPatientId() {
        System.out.print("  Patient ID: ");
        int id = sc.nextInt();
        sc.nextLine();
        while (!mainList.isIdExists(id)) {
            System.out.print("  No patient registered with ID " + id + "! Register them in Module 1 first, or enter a different ID: ");
            id = sc.nextInt();
            sc.nextLine();
        }
        return id;
    }

    static String patientLabel(int id) {
        Patient p = mainList.getPatientById(id);
        return (p != null) ? (id + " - " + p.name) : (id + " - (Unknown)");
    }

    static void savePatients() {
        List<String> lines = new ArrayList<>();
        Patient temp = mainList.head;
        while (temp != null) {
            lines.add(temp.id + "," + temp.name + "," + temp.age + "," + temp.ward);
            temp = temp.next;
        }
        FileManager.writeLines("patients.txt", lines);
    }

    static void loadPatients() {
        for (String line : FileManager.readLines("patients.txt")) {
            String[] p = line.split(",", 4);
            if (p.length == 4) {
                int id = Integer.parseInt(p[0].trim());
                if (!mainList.isIdExists(id)) {
                    mainList.addPatientQuietly(id, p[1].trim(), Integer.parseInt(p[2].trim()), p[3].trim());
                }
            }
        }
    }

    static void module1Menu() {
        int choice = -1;
        while (choice != 9) {
            System.out.println("\n  +======================================+");
            System.out.println("  |     Module 1 - Patient Management    |");
            System.out.println("  +======================================+");
            System.out.println("  |  1. Add Patient                      |");
            System.out.println("  |  2. Display Patients by Ward         |");
            System.out.println("  |  3. Update Patient                   |");
            System.out.println("  |  4. Delete Patient                   |");
            System.out.println("  |  -- Unique Methods --                |");
            System.out.println("  |  5. Merge Two Patient Lists          |");
            System.out.println("  |  6. Detect Duplicate Patient ID      |");
            System.out.println("  |  7. Reverse Patient Admission Order  |");
            System.out.println("  |  8. Find Middle Patient              |");
            System.out.println("  |  9. Back to Main Menu                |");
            System.out.println("  +======================================+");
            System.out.print("  Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("  Patient ID   : ");
                int id = sc.nextInt();
                sc.nextLine();
                while (mainList.isIdExists(id)) {
                    System.out.print("  ID exists! Enter different ID: ");
                    id = sc.nextInt();
                    sc.nextLine();
                }
                String name = readAlphaInput("  Name         : ");
                System.out.print("  Age          : ");
                int age = sc.nextInt();
                sc.nextLine();
                System.out.println("  Ward: 1. Ward A   2. Ward B");
                System.out.print("  Choice: ");
                int w = sc.nextInt();
                sc.nextLine();
                String ward = (w == 1) ? "Ward A" : "Ward B";
                mainList.addPatient(id, name, age, ward);

                AppointmentModule.waitQueue.add(id);
                AppointmentModule.drainQueue.add(id);
                AppointmentModule.normalQueue.add(id);
                System.out.println("  " + name + " (ID " + id + ") also added to Module 2.");
                savePatients();
                AppointmentModule.saveModule2Queues();

            } else if (choice == 2) {
                PatientList wA = mainList.getPatientsByWard("Ward A");
                PatientList wB = mainList.getPatientsByWard("Ward B");
                System.out.println("\n  === WARD A ===");
                wA.displayAll();
                System.out.println("  === WARD B ===");
                wB.displayAll();

            } else if (choice == 3) {
                System.out.print("  Patient ID to update: ");
                int id = sc.nextInt();
                sc.nextLine();
                String name = readAlphaInput("  New Name : ");
                System.out.print("  New Age  : ");
                int age = sc.nextInt();
                sc.nextLine();
                System.out.println("  Ward: 1. Ward A   2. Ward B");
                System.out.print("  Choice: ");
                int w = sc.nextInt();
                sc.nextLine();
                String ward = (w == 1) ? "Ward A" : "Ward B";
                mainList.updatePatient(id, name, age, ward);
                savePatients();

            } else if (choice == 4) {
                System.out.print("  Patient ID to delete: ");
                int id = sc.nextInt();
                sc.nextLine();
                mainList.deletePatient(id);
                savePatients();

            } else if (choice == 5) {
                PatientList wA = mainList.getPatientsByWard("Ward A");
                PatientList wB = mainList.getPatientsByWard("Ward B");
                wA.mergeLists(wB);
                wA.displayAll();

            } else if (choice == 6) {
                mainList.detectDuplicateID();

            } else if (choice == 7) {
                mainList.reverseAdmissionOrder();
                mainList.displayAll();

            } else if (choice == 8) {
                mainList.findMiddlePatient();

            } else if (choice == 9) {
                System.out.println("  Returning to Main Menu...");
            } else {
                System.out.println("  Invalid choice!");
            }
        }
    }

    static String readAlphaInput(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("  Invalid input! This field cannot be empty. Try again.");
            } else if (!input.matches("[a-zA-Z ]+")) {
                System.out.println("  Invalid input! Only alphabets are allowed. Try again.");
            } else {
                return input;
            }
        }
    }
}