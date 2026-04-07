
import java.util.*;

public class Student extends User {

    int currentSemester;
    ArrayList<String> enrolledCourses;
    HashMap<String, String> grades;

    Student(String id, String name, String email, String password, int semester) {
        super(id, name, email, password);
        this.currentSemester = semester;
        this.enrolledCourses = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    @Override
    void showMenu() {
        try {
            while (true) {

                System.out.println("\nWelcome " + name);
                System.out.println("1. View Available Courses");
                System.out.println("2. Register for Courses");
                System.out.println("3. View Schedule");
                System.out.println("4. Track Academic Progress (GPA)");
                System.out.println("5. Drop Course");
                System.out.println("6. Submit Complaint");
                System.out.println("7. View My Complaints (Status)");
                System.out.println("8. Logout");
                
                int choice = SchoolAPP.sc.nextInt();
                SchoolAPP.sc.nextLine();

                SchoolAPP.clear();
                SchoolAPP.wait(300);

                switch (choice) {
                    case 1 ->
                        viewAvailableCourses();
                    case 2 ->
                        registerCourses();
                    case 3 ->
                        viewSchedule();
                    case 4 ->
                        trackProgress();
                    case 5 ->
                        dropCourse();
                    case 6 ->
                        submitComplaint();
                    case 7 ->
                        viewMyComplaints();
                    case 8 -> {
                        SchoolAPP.currentUser = null;
                        return;
                    }
                    default ->
                        System.out.println("Invalid option.");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input. Returning to menu.");
        }
    }

    //show all the courses that are availabe for student using his/her current semester 
    void viewAvailableCourses() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\nAvailable Courses (Semester " + currentSemester + ")");
        boolean found = false;
        for (Course c : SchoolAPP.courses) {
            if (c.semester == currentSemester) {
                System.out.println(c.courseCode + " | " + c.title + " | Prof: "
                        + SchoolAPP.getProfName(c.professorId)
                        + " | Credits: " + c.credits + " | Prereq: " + c.prerequisites
                        + " | " + c.timings + " | " + c.location);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No courses available for this semester.");
        }
        SchoolAPP.wait(1000);
    }

    // registerCourses() for registering 
    void registerCourses() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\nRegister for Courses (Semester " + currentSemester + ")\n");
        while (true) {
            viewAvailableCourses();
            System.out.print("\nEnter course code to register (or type 'done' to finish): ");
            String code = SchoolAPP.sc.nextLine().trim().toUpperCase();
            /*
                we can type done to exit the registration process , instead of entering
                a course code or looping through every course code if misclicked.
            */ 
            if (code.equals("DONE")) {
                System.out.println("✅ Registration session ended.");
                break;
            }

            Course course = SchoolAPP.findCourse(code);

            if (course == null || course.semester != currentSemester) {
                System.out.println("❌ Invalid course or not available for your semester!");
                continue;
            }

            if (enrolledCourses.contains(code)) {
                System.out.println("❌ You are already enrolled in " + code);
                continue;
            }

            int currentCredits = 0;
            for (String ec : enrolledCourses) {
                Course ecourse = SchoolAPP.findCourse(ec);
                if (ecourse != null) {
                    currentCredits += ecourse.credits;
                }
            }
            if (currentCredits + course.credits > 20) {
                System.out.println("❌ Credit limit 20 exceeded!");
                continue;
            }

            boolean prereqs = true;
            for (String pre : course.prerequisites) {
                if (!grades.containsKey(pre)) {
                    System.out.println("❌ Prerequisite " + pre + " not completed!");
                    prereqs = false;
                    break;
                }
            }
            if (!prereqs) {
                continue;
            }

            enrolledCourses.add(code);
            System.out.println("✅ Successfully registered for " + code + " !");
        }
    }

    // drop out of course
    void dropCourse() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\nYour Enrolled Courses");

        if (enrolledCourses.isEmpty()) {
            System.out.println("No courses enrolled yet.");
            return;
        }

        for (String code : enrolledCourses) {
            System.out.println("→ " + code);
        }

        System.out.print("\nEnter course code to drop: ");

        String code = SchoolAPP.sc.nextLine().trim().toUpperCase();

        if (enrolledCourses.remove(code)) {
            System.out.println("✅ Dropped " + code);
        } else {
            System.out.println("Not enrolled in that course.");
        }
    }

    // Schedule viewer
    void viewSchedule() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\nYour Weekly Schedule");

        if (enrolledCourses.isEmpty()) {
            System.out.println("You have no enrolled courses yet.");
            return;
        }

        for (String code : enrolledCourses) {
            Course c = SchoolAPP.findCourse(code);
            if (c != null) {
                System.out.println(c.courseCode + " | " + c.title + " | " + c.timings + " | " + c.location + " | Prof: " + SchoolAPP.getProfName(c.professorId));
            }
        }
    }

    // Gpa Tracker
    void trackProgress() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\nAcademic Progress (Semester " + currentSemester + ")");

        double totalPoints = 0;
        int totalCredits = 0;

        for (Map.Entry<String, String> entry : grades.entrySet()) {
            Course c = SchoolAPP.findCourse(entry.getKey());
            if (c != null) {
                double gp = getGradePoint(entry.getValue());
                totalPoints += gp * c.credits;
                totalCredits += c.credits;
                System.out.println(entry.getKey() + " - " + entry.getValue() + " (" + gp + ")");
            }
        }

        if (totalCredits > 0) {
            double cgpa = totalPoints / totalCredits;
            System.out.println("CGPA: " + String.format("%.2f", cgpa));
        } else {
            System.out.println("No completed courses yet.");
        }

        System.out.println("Currently enrolled in " + enrolledCourses.size() + " course(s).");
    }

    /* Submit Complaint by creating an object.   */
    void submitComplaint() {

        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.print("Enter complaint description: ");
        String desc = SchoolAPP.sc.nextLine();
        String cid = "C" + (SchoolAPP.complaints.size() + 1001);
        SchoolAPP.complaints.add(new Complaint(cid, id, desc));
        System.out.println("Complaint submitted! ID: " + cid);
    }

    void viewMyComplaints() {
        SchoolAPP.clear();
        SchoolAPP.wait(300);

        System.out.println("\nMy Complaints");
        boolean found = false;

        for (Complaint c : SchoolAPP.complaints) {
            if (c.studentId.equals(this.id)) {
                System.out.println("ID       : " + c.id);
                System.out.println("Description: " + c.description);
                System.out.println("Status     : " + c.status);
                found = true;
            }
        }

        if (!found) {
            System.out.println("You have not submitted any complaints yet.");
        }
        System.out.print("\nPress Enter to return to menu...");
        SchoolAPP.sc.nextLine();
    }

    private double getGradePoint(String grade) {
        return switch (grade.toUpperCase()) {
            case "A" ->
                4.0;
            case "B" ->
                3.0;
            case "C" ->
                2.0;
            case "D" ->
                1.0;
            default ->
                0.0;
        };
    }
}
