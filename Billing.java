public class Billing {
    static final double LAB_TEST_FEE = 500.0; // flat fee per lab test

    // 6. Generate Bill (Doctor Fee + Prescribed Medicines + Lab Test Fees)   [DS: Stack + Queue + Linked List lookups]
    static void generateBill() {
        int id = PatientModule.askRegisteredPatientId();
        double total = 0;
        System.out.println("\n  ========================================");
        System.out.println("             BILL RECEIPT                 ");
        System.out.println("  ========================================");
        System.out.println("  Patient: " + PatientModule.patientLabel(id));

        // 1) Doctor Fee (Linked List lookup from Module 4)
        int docId = DoctorModule.getAssignedDoctorId(id);
        if (docId != -1) {
            DoctorModule.DoctorNode d = DoctorModule.getDoctorById(docId);
            if (d != null) {
                System.out.println("  Doctor Fee (Dr. " + d.name + ") : " + d.fee + " PKR");
                total += d.fee;
            }
        } else {
            System.out.println("  Doctor Fee : 0 PKR (No Doctor Assigned)");
        }

        // 2) Prescribed Medicines (Stack traversal, price from Medicine Inventory)
        System.out.println("  --- Prescribed Medicines ---");
        PrescriptionUndo.PrescNode temp = PrescriptionUndo.prescTop;
        boolean medFound = false;
        while (temp != null) {
            if (temp.patientId == id) {
                double price = MedicineInventory.findMedPrice(temp.medicine);
                if (price != -1) {
                    System.out.println("  - " + temp.medicine + " : " + price + " PKR");
                    total += price;
                } else {
                    System.out.println("  - " + temp.medicine + " : 0 PKR (Price not listed)");
                }
                medFound = true;
            }
            temp = temp.next;
        }
        if (!medFound) {
            System.out.println("  (No medicines prescribed)");
        }

        // 3) Lab Test Fees (check both Emergency and Normal Queues, flat fee per test)
        System.out.println("  --- Lab Tests ---");
        boolean testFound = false;
        LabTestQueue.LabNode lt = LabTestQueue.emergencyLabFront;
        while (lt != null) {
            if (lt.patientId == id) {
                System.out.println("  - " + lt.test + " [Emergency] : " + LAB_TEST_FEE + " PKR");
                total += LAB_TEST_FEE;
                testFound = true;
            }
            lt = lt.next;
        }
        lt = LabTestQueue.normalLabFront;
        while (lt != null) {
            if (lt.patientId == id) {
                System.out.println("  - " + lt.test + " [Normal] : " + LAB_TEST_FEE + " PKR");
                total += LAB_TEST_FEE;
                testFound = true;
            }
            lt = lt.next;
        }
        if (!testFound) {
            System.out.println("  (No lab tests recorded)");
        }

        System.out.println("  ----------------------------------------");
        System.out.println("  TOTAL BILL: " + total + " PKR");
        System.out.println("  ========================================\n");
    }
}
