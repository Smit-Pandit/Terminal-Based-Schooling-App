import javax.swing.*;

public class StudentDashboard extends JFrame {

    Student student;

    StudentDashboard(Student s) {
        this.student = s;

        setTitle("Student Dashboard");
        setSize(500,400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton viewCourses = new JButton("View Courses");
        viewCourses.setBounds(150,50,200,40);
        add(viewCourses);

        JButton register = new JButton("Register Course");
        register.setBounds(150,110,200,40);
        add(register);

        viewCourses.addActionListener(e -> showCourses());

        setVisible(true);
    }

    void showCourses() {
        StringBuilder sb = new StringBuilder();

        for(Course c : SchoolAPP.courses){
            if(c.semester == student.currentSemester){
                sb.append(c.courseCode)
                  .append(" - ")
                  .append(c.title)
                  .append("\n");
            }
        }

        JOptionPane.showMessageDialog(this,sb.toString());
    }
}