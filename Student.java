
import java.awt.*;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

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
    JFrame frame = new JFrame("Student Menu");
    frame.setSize(400, 500);
    frame.setLayout(new GridLayout(8, 1, 10, 10));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel label = new JLabel("Welcome " + name, SwingConstants.CENTER);

    JButton btn1 = new JButton("View Available Courses");
    JButton btn2 = new JButton("Register for Courses");
    JButton btn3 = new JButton("View Schedule");
    JButton btn4 = new JButton("Track Academic Progress");
    JButton btn5 = new JButton("Drop Course");
    JButton btn6 = new JButton("Submit Complaint");
    JButton btn7 = new JButton("View My Complaints");
    JButton btn8 = new JButton("Logout");

    btn1.addActionListener(e -> viewAvailableCourses());
    btn2.addActionListener(e -> registerCourses());
    btn3.addActionListener(e -> viewSchedule());
    btn4.addActionListener(e -> trackProgress());
    btn5.addActionListener(e -> dropCourse());
    btn6.addActionListener(e -> submitComplaint());
    btn7.addActionListener(e -> viewMyComplaints());

    btn8.addActionListener(e -> {
        SchoolAPP.currentUser = null;
        frame.dispose();
    });

    frame.add(label);
    frame.add(btn1);
    frame.add(btn2);
    frame.add(btn3);
    frame.add(btn4);
    frame.add(btn5);
    frame.add(btn6);
    frame.add(btn7);
    frame.add(btn8);

    frame.setVisible(true);
}

    //show all the courses that are availabe for student using his/her current semester 
   void viewAvailableCourses() {
    StringBuilder sb = new StringBuilder();
    sb.append("Available Courses (Semester ").append(currentSemester).append(")\n\n");

    boolean found = false;

    for (Course c : SchoolAPP.courses) {
        if (c.semester == currentSemester) {
            sb.append(c.courseCode).append(" | ")
              .append(c.title).append(" | Prof: ")
              .append(SchoolAPP.getProfName(c.professorId)).append(" | Credits: ")
              .append(c.credits).append(" | Prereq: ")
              .append(c.prerequisites).append(" | ")
              .append(c.timings).append(" | ")
              .append(c.location).append("\n");

            found = true;
        }
    }

    if (!found) {
        sb.append("No courses available for this semester.");
    }

    JOptionPane.showMessageDialog(null, sb.toString());
}
    // registerCourses() for registering 
    void registerCourses() {
    while (true) {

        ArrayList<String> available = new ArrayList<>();

        for (Course c : SchoolAPP.courses) {
            if (c.semester == currentSemester) {
                available.add(c.courseCode);
            }
        }

        available.add("DONE");

        String code = (String) JOptionPane.showInputDialog(
                null,
                "Select Course to Register:",
                "Course Registration",
                JOptionPane.PLAIN_MESSAGE,
                null,
                available.toArray(),
                available.get(0)
        );

        if (code == null || code.equals("DONE")) {
            JOptionPane.showMessageDialog(null, "Registration Ended.");
            break;
        }

        Course course = SchoolAPP.findCourse(code);

        if (enrolledCourses.contains(code)) {
            JOptionPane.showMessageDialog(null, "Already Enrolled in " + code);
            continue;
        }

        int currentCredits = 0;

        for (String ec : enrolledCourses) {
            Course ecourse = SchoolAPP.findCourse(ec);
            if (ecourse != null) currentCredits += ecourse.credits;
        }

        if (currentCredits + course.credits > 20) {
            JOptionPane.showMessageDialog(null, "Credit Limit Exceeded!");
            continue;
        }

        boolean prereqs = true;

        for (String pre : course.prerequisites) {
            if (!grades.containsKey(pre)) {
                JOptionPane.showMessageDialog(null,
                        "Prerequisite " + pre + " Not Completed!");
                prereqs = false;
                break;
            }
        }

        if (!prereqs) continue;

        enrolledCourses.add(code);

        JOptionPane.showMessageDialog(null,
                "Successfully Registered for " + code);
    }
}

    // drop out of course
    void dropCourse() {
    if (enrolledCourses.isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "No Courses Enrolled Yet.");
        return;
    }

    String code = (String) JOptionPane.showInputDialog(
            null,
            "Select Course to Drop:",
            "Drop Course",
            JOptionPane.PLAIN_MESSAGE,
            null,
            enrolledCourses.toArray(),
            enrolledCourses.get(0)
    );

    if (code == null) return;

    enrolledCourses.remove(code);

    JOptionPane.showMessageDialog(null,
            "Dropped " + code);
}

    // Schedule viewer
    void viewSchedule() {
    if (enrolledCourses.isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "You have no enrolled courses yet.");
        return;
    }

    StringBuilder sb = new StringBuilder("Your Weekly Schedule\n\n");

    for (String code : enrolledCourses) {
        Course c = SchoolAPP.findCourse(code);

        if (c != null) {
            sb.append(c.courseCode).append(" | ")
              .append(c.title).append(" | ")
              .append(c.timings).append(" | ")
              .append(c.location).append(" | Prof: ")
              .append(SchoolAPP.getProfName(c.professorId))
              .append("\n");
        }
    }

    JOptionPane.showMessageDialog(null, sb.toString());
}
    // Gpa Tracker
    void trackProgress() {
    StringBuilder sb = new StringBuilder();
    sb.append("Academic Progress (Semester ")
      .append(currentSemester)
      .append(")\n\n");

    double totalPoints = 0;
    int totalCredits = 0;

    for (Map.Entry<String, String> entry : grades.entrySet()) {
        Course c = SchoolAPP.findCourse(entry.getKey());

        if (c != null) {
            double gp = getGradePoint(entry.getValue());

            totalPoints += gp * c.credits;
            totalCredits += c.credits;

            sb.append(entry.getKey())
              .append(" - ")
              .append(entry.getValue())
              .append(" (")
              .append(gp)
              .append(")\n");
        }
    }

    if (totalCredits > 0) {
        double cgpa = totalPoints / totalCredits;

        sb.append("\nCGPA: ")
          .append(String.format("%.2f", cgpa));
    } else {
        sb.append("\nNo completed courses yet.");
    }

    sb.append("\nCurrently enrolled in ")
      .append(enrolledCourses.size())
      .append(" course(s).");

    JOptionPane.showMessageDialog(null, sb.toString());
}
    /* Submit Complaint by creating an object.   */
    void submitComplaint() {
    String desc = JOptionPane.showInputDialog(
            null,
            "Enter Complaint Description:"
    );

    if (desc == null || desc.trim().isEmpty()) return;

    String cid = "C" + (SchoolAPP.complaints.size() + 1001);

    SchoolAPP.complaints.add(
            new Complaint(cid, id, desc)
    );

    JOptionPane.showMessageDialog(null,
            "Complaint Submitted!\nID: " + cid);
}

   void viewMyComplaints() {
    StringBuilder sb = new StringBuilder("My Complaints\n\n");

    boolean found = false;

    for (Complaint c : SchoolAPP.complaints) {
        if (c.studentId.equals(this.id)) {
            sb.append("ID: ").append(c.id).append("\n")
              .append("Description: ").append(c.description).append("\n")
              .append("Status: ").append(c.status).append("\n\n");

            found = true;
        }
    }

    if (!found) {
        sb.append("You have not submitted any complaints yet.");
    }

    JOptionPane.showMessageDialog(null, sb.toString());
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
