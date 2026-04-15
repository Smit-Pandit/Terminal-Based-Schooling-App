import java.awt.*;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Professor extends User {

    Professor(String id, String name, String email, String password) {
        super(id, name, email, password);
    }

    @Override
void showMenu() {
    JFrame frame = new JFrame("Professor Menu");
    frame.setSize(400, 300);
    frame.setLayout(new GridLayout(4, 1, 10, 10));
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JButton b1 = new JButton("Manage Courses");
    JButton b2 = new JButton("View Enrolled Students");
    JButton b3 = new JButton("Logout");

    b1.addActionListener(e -> manageCourses());
    b2.addActionListener(e -> viewEnrolledStudents());

    b3.addActionListener(e -> {
        SchoolAPP.currentUser = null;
        frame.dispose();
    });

    frame.add(b1);
    frame.add(b2);
    frame.add(b3);

    frame.setVisible(true);
}

void manageCourses() {

    ArrayList<Course> myCourses = new ArrayList<>();

    for (Course c : SchoolAPP.courses) {
        if (c.professorId.equals(id)) {
            myCourses.add(c);
        }
    }

    if (myCourses.isEmpty()) {
        JOptionPane.showMessageDialog(null,
                "You have no assigned courses.");
        return;
    }

    String[] options = new String[myCourses.size()];

    for (int i = 0; i < myCourses.size(); i++) {
        options[i] = myCourses.get(i).courseCode + " - " + myCourses.get(i).title;
    }

    String selected = (String) JOptionPane.showInputDialog(
            null,
            "Select Course to Update:",
            "Manage Courses",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
    );

    if (selected == null) return;

    String code = selected.split(" - ")[0];

    for (Course c : myCourses) {
        if (c.courseCode.equals(code)) {
            updateCourse(c);
            break;
        }
    }
}

private void updateCourse(Course c) {

    while (true) {

        String[] fields = {
                "Syllabus",
                "Credits",
                "Prerequisites",
                "Enrollment Limit",
                "Office Hours",
                "Timings",
                "Location",
                "Done / Back"
        };

        String choice = (String) JOptionPane.showInputDialog(
                null,
                "Update Course: " + c.courseCode,
                "Course Update",
                JOptionPane.PLAIN_MESSAGE,
                null,
                fields,
                fields[0]
        );

        if (choice == null || choice.equals("Done / Back")) {
            JOptionPane.showMessageDialog(null,
                    "Course Updated Successfully!");
            return;
        }

        switch (choice) {

            case "Syllabus" -> {
                String input = JOptionPane.showInputDialog(
                        "Enter New Syllabus:"
                );
                if (input != null) c.syllabus = input;
            }

            case "Credits" -> {
                String input = JOptionPane.showInputDialog(
                        "Enter New Credits:"
                );
                if (input != null) {
                    try {
                        c.credits = Integer.parseInt(input);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid Number!");
                    }
                }
            }

            case "Prerequisites" -> {
                String input = JOptionPane.showInputDialog(
                        "Enter Prerequisites (comma separated):"
                );
                if (input != null) {
                    c.prerequisites = new ArrayList<>(
                            java.util.Arrays.asList(input.split(","))
                    );
                }
            }

            case "Enrollment Limit" -> {
                String input = JOptionPane.showInputDialog(
                        "Enter Enrollment Limit:"
                );
                if (input != null) {
                    try {
                        c.enrollmentLimit = Integer.parseInt(input);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null,
                                "Invalid Number!");
                    }
                }
            }

            case "Office Hours" -> {
                String input = JOptionPane.showInputDialog(
                        "Enter Office Hours:"
                );
                if (input != null) c.officeHours = input;
            }

            case "Timings" -> {
                String input = JOptionPane.showInputDialog(
                        "Enter New Timings:"
                );
                if (input != null) c.timings = input;
            }

            case "Location" -> {
                String input = JOptionPane.showInputDialog(
                        "Enter New Location:"
                );
                if (input != null) c.location = input;
            }
        }
    }
}

void viewEnrolledStudents() {

    StringBuilder sb = new StringBuilder();
    sb.append("Enrolled Students In Your Courses\n\n");

    boolean foundAny = false;

    for (Course c : SchoolAPP.courses) {

        if (c.professorId.equals(id)) {

            sb.append("Course: ").append(c.courseCode).append("\n");

            boolean hasStudents = false;

            for (Student s : SchoolAPP.students) {

                if (s.enrolledCourses.contains(c.courseCode)) {

                    sb.append(" - ")
                      .append(s.name)
                      .append(" (")
                      .append(s.email)
                      .append(") Semester: ")
                      .append(s.currentSemester)
                      .append("\n");

                    hasStudents = true;
                    foundAny = true;
                }
            }

            if (!hasStudents) {
                sb.append(" No students yet.\n");
            }

            sb.append("\n");
        }
    }

    if (!foundAny) {
        sb.append("No students enrolled in your courses.");
    }

    JOptionPane.showMessageDialog(null, sb.toString());
}
}