import javax.swing.*;

public class LoginFrame extends JFrame {

    JTextField emailField;
    JPasswordField passField;
    JButton loginBtn;
    JComboBox<String> roleBox;

    LoginFrame() {
        setTitle("School Login");
        setSize(400,300);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        roleBox = new JComboBox<>(new String[]{"Student","Professor","Admin"});
        roleBox.setBounds(100,30,200,30);
        add(roleBox);

        emailField = new JTextField();
        emailField.setBounds(100,80,200,30);
        add(emailField);

        passField = new JPasswordField();
        passField.setBounds(100,130,200,30);
        add(passField);

        loginBtn = new JButton("Login");
        loginBtn.setBounds(140,190,120,35);
        add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    void login() {
        String email = emailField.getText();
        String pass = new String(passField.getPassword());

        User user = authenticate(email, pass);

        if(user != null){
            user.showMenu();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,"Invalid Login");
        }
    }

    User authenticate(String email,String pass){
        for(Student s : SchoolAPP.students)
            if(s.login(email,pass)) return s;

        for(Professor p : SchoolAPP.professors)
            if(p.login(email,pass)) return p;

        if(email.equals("admin@uni.edu") && pass.equals("admin123"))
            return new Administrator("A001","Admin",email,pass);

        return null;
    }
}