
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Administrator extends User {

    Administrator(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
void showMenu() {
    JFrame frame = new JFrame("Admin Menu");
    frame.setSize(400, 400);
    frame.setLayout(new GridLayout(6, 1, 10, 10));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JButton b1 = new JButton("Manage Course Catalog");
    JButton b2 = new JButton("Manage Student Records");
    JButton b3 = new JButton("Assign Professors");
    JButton b4 = new JButton("Handle Complaints");
    JButton b5 = new JButton("Logout");

    b1.addActionListener(e -> manageCourse());
    b2.addActionListener(e -> manageStudentRecords());
    b3.addActionListener(e -> manageProfessors());
    b4.addActionListener(e -> manageComplaints());

    b5.addActionListener(e -> {
        SchoolAPP.currentUser = null;
        frame.dispose();
    });

    frame.add(b1);
    frame.add(b2);
    frame.add(b3);
    frame.add(b4);
    frame.add(b5);

    frame.setVisible(true);
}

    private void manageCourse() {

    String[] options = {
            "View All Courses",
            "Add New Course",
            "Delete Course"
    };

    String choice = (String) JOptionPane.showInputDialog(
            null,
            "Manage Course Catalog",
            "Course Catalog",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
    );

    if (choice == null) return;

    switch (choice) {

        case "View All Courses" -> {
            StringBuilder sb = new StringBuilder();

            for (Course c : SchoolAPP.courses) {
                sb.append(c.courseCode)
                  .append(" | ")
                  .append(c.title)
                  .append(" | Semester ")
                  .append(c.semester)
                  .append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString());
        }

        case "Add New Course" -> {
            try {
                String code = JOptionPane.showInputDialog("Course Code:");
                String title = JOptionPane.showInputDialog("Title:");
                String pid = JOptionPane.showInputDialog("Professor ID:");
                int cr = Integer.parseInt(JOptionPane.showInputDialog("Credits:"));
                int sem = Integer.parseInt(JOptionPane.showInputDialog("Semester:"));
                String time = JOptionPane.showInputDialog("Timings:");
                String loc = JOptionPane.showInputDialog("Location:");
                String syllabus = JOptionPane.showInputDialog("Syllabus:");
                int limit = Integer.parseInt(JOptionPane.showInputDialog("Enrollment Limit:"));
                String office = JOptionPane.showInputDialog("Office Hours:");

                SchoolAPP.courses.add(
                        new Course(code, title, pid, cr, sem, time, loc,
                                new ArrayList<>(), syllabus, limit, office)
                );

                JOptionPane.showMessageDialog(null, "Course Added Successfully!");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid Input!");
            }
        }

        case "Delete Course" -> {
            ArrayList<String> codes = new ArrayList<>();

            for (Course c : SchoolAPP.courses) {
                codes.add(c.courseCode);
            }

            String code = (String) JOptionPane.showInputDialog(
                    null,
                    "Select Course to Delete:",
                    "Delete Course",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    codes.toArray(),
                    codes.get(0)
            );

            if (code != null) {
                SchoolAPP.courses.removeIf(c -> c.courseCode.equalsIgnoreCase(code));
                JOptionPane.showMessageDialog(null, "Deleted Successfully!");
            }
        }
    }
}

   private void manageStudentRecords() {

    String[] options = {
            "View All Students",
            "Update Student Personal Info",
            "Update Student Grades"
    };

    String choice = (String) JOptionPane.showInputDialog(
            null,
            "Manage Student Records",
            "Student Records",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
    );

    if (choice == null) return;

    switch (choice) {

        case "View All Students" -> {
            StringBuilder sb = new StringBuilder();

            for (Student s : SchoolAPP.students) {
                sb.append(s.id).append(" | ")
                  .append(s.name).append(" | ")
                  .append(s.email).append(" | Semester ")
                  .append(s.currentSemester).append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString());
        }

        case "Update Student Personal Info" -> {
            String sid = JOptionPane.showInputDialog("Enter Student ID:");

            Student s = SchoolAPP.findStudentById(sid);

            if (s == null) {
                JOptionPane.showMessageDialog(null, "Student Not Found!");
                return;
            }

            String newName = JOptionPane.showInputDialog("New Name:", s.name);
            String newEmail = JOptionPane.showInputDialog("New Email:", s.email);

            if (newName != null && !newName.isEmpty()) s.name = newName;
            if (newEmail != null && !newEmail.isEmpty()) s.email = newEmail;

            JOptionPane.showMessageDialog(null, "Updated Successfully!");
        }

        case "Update Student Grades" -> {
            String sid = JOptionPane.showInputDialog("Enter Student ID:");

            Student s = SchoolAPP.findStudentById(sid);

            if (s == null) {
                JOptionPane.showMessageDialog(null, "Student Not Found!");
                return;
            }

            String code = JOptionPane.showInputDialog("Course Code:");
            String grade = JOptionPane.showInputDialog("Grade (A/B/C/D/F):");

            s.grades.put(code.toUpperCase(), grade.toUpperCase());
            s.enrolledCourses.remove(code.toUpperCase());

            JOptionPane.showMessageDialog(null, "Grade Updated!");
        }
    }
}
    // Add professor for the specific course
private void manageProfessors() {

    ArrayList<String> coursesList = new ArrayList<>();

    for (Course c : SchoolAPP.courses) {
        coursesList.add(c.courseCode);
    }

    String code = (String) JOptionPane.showInputDialog(
            null,
            "Select Course:",
            "Assign Professor",
            JOptionPane.PLAIN_MESSAGE,
            null,
            coursesList.toArray(),
            coursesList.get(0)
    );

    if (code == null) return;

    Course course = SchoolAPP.findCourse(code);

    ArrayList<String> profList = new ArrayList<>();

    for (Professor p : SchoolAPP.professors) {
        profList.add(p.id + " - " + p.name);
    }

    String selectedProf = (String) JOptionPane.showInputDialog(
            null,
            "Select Professor:",
            "Assign Professor",
            JOptionPane.PLAIN_MESSAGE,
            null,
            profList.toArray(),
            profList.get(0)
    );

    if (selectedProf == null) return;

    String pid = selectedProf.split(" - ")[0];

    course.professorId = pid;

    JOptionPane.showMessageDialog(null,
            "Professor Assigned Successfully!");
}

    // receive complaints
    private void manageComplaints() {

    String[] options = {
            "View All Complaints",
            "Filter by Status",
            "Update Complaint Status"
    };

    String choice = (String) JOptionPane.showInputDialog(
            null,
            "Manage Complaints",
            "Complaints",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
    );

    if (choice == null) return;

    switch (choice) {

        case "View All Complaints" -> {
            StringBuilder sb = new StringBuilder();

            for (Complaint c : SchoolAPP.complaints) {
                sb.append(c.id)
                  .append(" | Student ")
                  .append(c.studentId)
                  .append(" | ")
                  .append(c.description)
                  .append(" | ")
                  .append(c.status)
                  .append("\n");
            }

            JOptionPane.showMessageDialog(null, sb.toString());
        }

        case "Filter by Status" -> {
            String status = JOptionPane.showInputDialog(
                    "Enter Status (Pending/Resolved):"
            );

            StringBuilder sb = new StringBuilder();

            for (Complaint c : SchoolAPP.complaints) {
                if (c.status.equalsIgnoreCase(status)) {
                    sb.append(c.id)
                      .append(" | ")
                      .append(c.description)
                      .append("\n");
                }
            }

            JOptionPane.showMessageDialog(null, sb.toString());
        }

        case "Update Complaint Status" -> {
            String cid = JOptionPane.showInputDialog("Complaint ID:");

            for (Complaint c : SchoolAPP.complaints) {
                if (c.id.equals(cid)) {

                    String status = JOptionPane.showInputDialog("New Status:");
                    c.status = status;

                    JOptionPane.showMessageDialog(null,
                            "Complaint Updated!");

                    return;
                }
            }

            JOptionPane.showMessageDialog(null,
                    "Complaint Not Found!");
        }
    }
}
}
