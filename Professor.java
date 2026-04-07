import java.util.ArrayList;

public class Professor extends User {

    Professor(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
    void showMenu() {
        try {
            while (true) {
                
                System.out.println("\nPROFESSOR MENU (" + name + ")");
                System.out.println("1. Manage Courses (Update Details)");
                System.out.println("2. View Enrolled Students");
                System.out.println("3. Logout");
                int choice = SchoolAPP.sc.nextInt();
                SchoolAPP.sc.nextLine();
                
                SchoolAPP.clear();
                SchoolAPP.wait(300);

                switch (choice) {
                    case 1 -> manageCourses();
                    case 2 -> viewEnrolledStudents();
                    case 3 -> {
                        SchoolAPP.currentUser = null;
                        return;
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Returning to menu.");
        }
    }

    void manageCourses() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\n Your Courses ");
        for (Course c : SchoolAPP.courses) {
            // iterates through all courses
            if (c.professorId.equals(id)) {
                System.out.println(c.courseCode + " | " + c.title);
                System.out.print("Update this course? (y/n): ");
                if (SchoolAPP.sc.nextLine().equalsIgnoreCase("y")) {
                    updateCourse(c);
                }
            }
        }
    }

    private void updateCourse(Course c) {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        while (true) {
            System.out.println("\nUpdate Course: " + c.courseCode );
            System.out.println("1. Syllabus");
            System.out.println("2. Credits");
            System.out.println("3. Prerequisites");
            System.out.println("4. Enrollment Limit");
            System.out.println("5. Office Hours");
            System.out.println("6. Timings");
            System.out.println("7. Location");
            System.out.println("8. Done / Back");
            System.out.print("Choose field to update: ");

            int choice = SchoolAPP.sc.nextInt();
            SchoolAPP.sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("New Syllabus: ");
                    c.syllabus = SchoolAPP.sc.nextLine();
                }
                case 2 -> {
                    System.out.print("New Credits (2/4): ");
                    c.credits = SchoolAPP.sc.nextInt();
                    SchoolAPP.sc.nextLine();
                }
                case 3 -> {
                    System.out.print("New Prerequisites seperate by commas: ");
                    String preInput = SchoolAPP.sc.nextLine();
                    c.prerequisites = new ArrayList<>(java.util.Arrays.asList(preInput.split(",")));
                }
                case 4 -> {
                    System.out.print("New Enrollment Limit: ");
                    c.enrollmentLimit = SchoolAPP.sc.nextInt();
                    SchoolAPP.sc.nextLine();
                }
                case 5 -> {
                    System.out.print("New Office Hours: ");
                    c.officeHours = SchoolAPP.sc.nextLine();
                }
                case 6 -> {
                    System.out.print("New Timings: ");
                    c.timings = SchoolAPP.sc.nextLine();
                }
                case 7 -> {
                    System.out.print("New Location: ");
                    c.location = SchoolAPP.sc.nextLine();
                }
                case 8 -> {
                    System.out.println("✅ Course updated successfully!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    void viewEnrolledStudents() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);
        
        System.out.println("\nEnrolled Students in Your Courses");
        
        for (Course c : SchoolAPP.courses) {
            if (c.professorId.equals(id)) {
                System.out.println("Course: " + c.courseCode);
                boolean hasStudents = false;
                for (Student s : SchoolAPP.students) {
                    if (s.enrolledCourses.contains(c.courseCode)) {
                        System.out.println("  - " + s.name + " (" + s.email + ") Semester: " + s.currentSemester);
                        hasStudents = true;
                    }
                }
                if (!hasStudents) System.out.println("  No students yet.");
            }
        }
    }
}