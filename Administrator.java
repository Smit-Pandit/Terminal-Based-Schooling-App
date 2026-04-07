
import java.util.ArrayList;
import java.util.InputMismatchException;

public class Administrator extends User {

    Administrator(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    void showMenu() {
        try {
            while (true) {

                System.out.println("\nADMIN MENU");
                System.out.println("1. Manage Course Catalog (add/delete)");
                System.out.println("2. Manage Student Records (update grades)");
                System.out.println("3. Assign Professors to Courses");
                System.out.println("4. Handle Complaints");
                System.out.println("5. Logout");

                int choice = SchoolAPP.sc.nextInt();

                SchoolAPP.clear();
                SchoolAPP.sc.nextLine();
                SchoolAPP.wait(400);

                switch (choice) {
                    case 1 ->
                        manageCourse();
                    case 2 ->
                        manageStudentRecords();
                    case 3 ->
                        manageProfessors();
                    case 4 ->
                        manageComplaints();
                    case 5 -> {
                        SchoolAPP.currentUser = null;
                        return;
                    }
                    default ->
                        System.out.println("Invalid option.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Returning to menu.");
        }
    }

    private void manageCourse() {
        try {

            SchoolAPP.clear();
            SchoolAPP.wait(300);

            System.out.println("\n1. View all courses");
            System.out.println("2. Add new course");
            System.out.println("3. Delete course");

            int ch = SchoolAPP.sc.nextInt();

            switch (ch) {
                case 1 -> {
                    //show all courses
                    for (Course c : SchoolAPP.courses) {
                        System.out.println(c.courseCode + " | " + c.title + " | Semester " + c.semester);
                    }
                }
                case 2 -> {
                    //takes the data and create the object
                    System.out.print("Course code: ");
                    String code = SchoolAPP.sc.nextLine();

                    System.out.print("Title: ");
                    String title = SchoolAPP.sc.nextLine();

                    System.out.print("Professor ID: ");
                    String pid = SchoolAPP.sc.nextLine();

                    System.out.print("Credits (2/4): ");
                    int cr = SchoolAPP.sc.nextInt();
                    SchoolAPP.sc.nextLine();

                    System.out.print("Semester: ");
                    int sem = SchoolAPP.sc.nextInt();
                    SchoolAPP.sc.nextLine();

                    System.out.print("Timings: ");
                    String time = SchoolAPP.sc.nextLine();

                    System.out.print("Location: ");
                    String loc = SchoolAPP.sc.nextLine();

                    System.out.print("Syllabus: ");
                    String syllabus = SchoolAPP.sc.nextLine();

                    System.out.print("Enrollment Limit: ");
                    int limit = SchoolAPP.sc.nextInt();
                    SchoolAPP.sc.nextLine();

                    System.out.print("Office Hours: ");
                    String officeHours = SchoolAPP.sc.nextLine();

                    SchoolAPP.courses.add(new Course(code, title, pid, cr, sem, time, loc, new ArrayList<>(), syllabus, limit, officeHours));
                    System.out.println("Course added.");
                }
                case 3 -> {
                    for (Course c : SchoolAPP.courses) {
                        System.out.println(c.courseCode + " | " + c.title + " | Semester " + c.semester);
                    }
                    //remove from arraylist
                    System.out.print("Course code to delete: ");
                    String code = SchoolAPP.sc.nextLine();
                    SchoolAPP.courses.removeIf(c -> c.courseCode.equalsIgnoreCase(code));
                    System.out.println("Deleted if existed.");
                }
                default -> {
                    System.out.println("Invalid Option");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Returning to menu.");
        }
    }

    private void manageStudentRecords() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\n1. View All Students");
        System.out.println("2. Update Student Personal Info");
        System.out.println("3. Update Student Grades");
        System.out.print("Choose option: ");
        int opt = SchoolAPP.sc.nextInt();
        SchoolAPP.sc.nextLine();

        switch (opt) {
            case 1 -> {
                // view all students
                for (Student s : SchoolAPP.students) {
                    System.out.println(s.id + " | " + s.name + " | " + s.email + " | Semester " + s.currentSemester);
                }
            }
            case 2 -> {
                // update personal info
                System.out.print("Enter Student ID: ");
                String sid = SchoolAPP.sc.nextLine();
                Student s = SchoolAPP.findStudentById(sid);
                if (s == null) {
                    System.out.println("Student not found.");
                    return;
                }
                System.out.print("New Name : ");
                String newName = SchoolAPP.sc.nextLine().trim();
                if (!newName.isEmpty()) {
                    s.name = newName;
                }
                System.out.print("New Email : ");
                String newEmail = SchoolAPP.sc.nextLine().trim();
                if (!newEmail.isEmpty()) {
                    s.email = newEmail;
                }
                System.out.println("Personal info updated.");
            }
            case 3 -> {
                // update grades
                System.out.print("Enter Student ID: ");
                String sid = SchoolAPP.sc.nextLine();
                Student s = SchoolAPP.findStudentById(sid);
                if (s == null) {
                    System.out.println("Student not found.");
                    return;
                }
                System.out.println("Student: " + s.name + " | Current semester: " + s.currentSemester);
                System.out.print("Enter course code to assign grade: ");
                String code = SchoolAPP.sc.nextLine();
                System.out.print("Grade (A/B/C/D/F): ");
                String grade = SchoolAPP.sc.nextLine().toUpperCase();
                s.grades.put(code, grade);
                s.enrolledCourses.remove(code);
                System.out.println("Grade assigned. Semester progress updated.");
            }
            default -> {
            }
        }
    }

    // Add professor for the specific course
    private void manageProfessors() {

        System.out.println("Available courses:");

        for (Course c : SchoolAPP.courses) {
            System.out.println(c.courseCode + " | " + c.title);
        }

        System.out.print("Course code: ");
        String code = SchoolAPP.sc.nextLine();

        Course c = SchoolAPP.findCourse(code);
        if (c == null) {
            return;
        }

        System.out.println("Available professors:");

        for (Professor p : SchoolAPP.professors) {
            System.out.println(p.id + " | " + p.name);
        }

        System.out.print("Professor ID to assign: ");
        String pid = SchoolAPP.sc.nextLine();
        if (SchoolAPP.findProfessorById(pid) != null) {
            c.professorId = pid;
            System.out.println("Professor assigned.");
        }
    }

    // receive complaints
    private void manageComplaints() {
        System.out.println("\n1. View All Complaints");
        System.out.println("2. Filter by Status (Pending/Resolved)");
        System.out.println("3. Update Complaint Status + Resolution");
        System.out.print("Choose option: ");
        int opt = SchoolAPP.sc.nextInt();
        SchoolAPP.sc.nextLine();

        switch (opt) {
            case 1 -> {
                System.out.println("\nAll Complaints:");
                for (Complaint c : SchoolAPP.complaints) {
                    System.out.println(c.id + " | Student " + c.studentId + " | " + c.description + " | Status: " + c.status);
                }
            }
            case 2 -> {
                System.out.print("Enter status to filter (Pending/Resolved): ");
                String filter = SchoolAPP.sc.nextLine().trim();
                System.out.println("\nFiltered Complaints:");
                for (Complaint c : SchoolAPP.complaints) {
                    if (c.status.equalsIgnoreCase(filter)) {
                        System.out.println(c.id + " | Student " + c.studentId + " | " + c.description + " | Status: " + c.status);
                    }
                }
            }
            case 3 -> {
                
                System.out.println("\nAll Complaints:");
                for (Complaint c : SchoolAPP.complaints) {
                    System.out.println(c.id + " | Student " + c.studentId + " | " + c.description + " | Status: " + c.status);
                }

                System.out.print("Enter complaint ID to update: ");
                String cid = SchoolAPP.sc.nextLine();
                for (Complaint c : SchoolAPP.complaints) {
                    if (c.id.equals(cid)) {
                        System.out.print("New status (Pending/Resolved): ");
                        c.status = SchoolAPP.sc.nextLine();
                        System.out.print("Resolution: ");
                        String resolution = SchoolAPP.sc.nextLine();
                        System.out.println("Status updated with resolution: " + resolution);
                        return;
                    }
                }
                System.out.println("Complaint not found.");
            }
        }
    }
}
