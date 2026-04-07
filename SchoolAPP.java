
import java.io.*;
import java.util.*;

public class SchoolAPP {

    public static final ArrayList<Student> students = new ArrayList<>();
    public static final ArrayList<Professor> professors = new ArrayList<>();
    public static final ArrayList<Course> courses = new ArrayList<>();
    public static final ArrayList<Complaint> complaints = new ArrayList<>();

    public static Scanner sc = new Scanner(System.in);
    public static User currentUser = null; // type of current logged in user

    /*
        Used static block to get all the data at the very start for later manipulation without performing file operations.
     */
    static {
        // for loading all the students from students.txt and store their details in students arraylist
        loadStudents(); 
        loadProfessors();
        loadCourses();
        loadComplaints();
    }

    public static void main(String[] args) {
        try {
            clear();
            System.out.println("Welcome to the School App");
            wait(550);
            while (true) {
                System.out.println("\n1. Enter the Application");
                System.out.println("2. Exit the Application\n");
                int choice = sc.nextInt();
                if (choice == 2) {
                    saveAllData();
                    System.out.println("All changes saved. Goodbye!");
                    break;
                }
                loginScreen();
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Exiting application.");
        }
    }

    // for asthetics
    public static void clear() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    public static void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("System error occured.");
        }
    }
    //for asthetics end

    //loadVariables() Start
    /*
        loadVariables() takes the .txt file data , create an Variable type object and save 
        it inside an arraylist Variables
     */
    private static void loadStudents() {
        File file = new File("students.txt");
        // Good Practice to check if file is present or not.
        if (!file.exists()) {
            System.out.println("students file not found.");
            return;
        }
        try (FileReader fr = new FileReader(file); Scanner fsc = new Scanner(fr);) {
            while (fsc.hasNextLine()) {
                String line = fsc.nextLine();
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] p = line.split("\\|");
                // here , Student = {id,name,email,pass,semester,courses&grades}
                Student s = new Student(p[0], p[1], p[2], p[3], Integer.parseInt(p[4]));
                /*
                    at first the student object only contain few info like id,name,email,pass and semester
                    then we pass the arraylist of students enrolled courses
                 */
                if (p.length > 5 && !p[5].isEmpty()) {
                    s.enrolledCourses = new ArrayList<>(Arrays.asList(p[5].split(",")));
                }
                // if their are grades and enrolled courses thhen only we add them
                if (p.length > 6 && !p[6].isEmpty()) {
                    String[] gPairs = p[6].split(",");
                    for (String gp : gPairs) {
                        String[] kv = gp.split(":");
                        if (kv.length == 2) {
                            // just a check if it is in the format (code,grade) and then puts it in a hashmap
                            s.grades.put(kv[0], kv[1]);
                        }
                    }
                }
                students.add(s);
            }
        } catch (IOException e) {
            System.out.println("Could not load the file.");
        }
    }

    private static void loadProfessors() {
        File file = new File("professors.txt");
        if (!file.exists()) {
            System.out.println("professors.txt file not found.");
            return;
        }
        try (FileReader fr = new FileReader(file); Scanner fsc = new Scanner(fr)) {
            while (fsc.hasNextLine()) {
                String line = fsc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] p = line.split("\\|");
                professors.add(new Professor(p[0], p[1], p[2], p[3]));
            }
        } catch (IOException e) {
            System.out.println("Could not load files.");
        }
    }

    private static void loadCourses() {
        File file = new File("courses.txt");
        if (!file.exists()) {
            System.out.println("courses.txt file not found.");
            return;
        }
        try (FileReader fr = new FileReader(file); Scanner fsc = new Scanner(fr)) {
            while (fsc.hasNextLine()) {
                String line = fsc.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] p = line.split("\\|");
                ArrayList<String> pre = new ArrayList<>();
                //prerequisites
                if (p.length > 7 && !p[7].isEmpty()) {
                    pre = new ArrayList<>(java.util.Arrays.asList(p[7].split(",")));
                }
                String syllabus = (p.length > 8) ? p[8] : "Not provided";
                int limit = (p.length > 9) ? Integer.parseInt(p[9]) : 30;
                String office = (p.length > 10) ? p[10] : "Not provided";

                courses.add(new Course(p[0], p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]),
                        p[5], p[6], pre, syllabus, limit, office));
            }
        } catch (Exception e) {
            System.out.println("Could not load courses.txt");
        }
    }

    private static void loadComplaints() {
        try (FileReader fr = new FileReader("complaints.txt"); Scanner fsc = new Scanner(fr)) {
            while (fsc.hasNextLine()) {
                String line = fsc.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] p = line.split("\\|");
                Complaint c = new Complaint(p[0], p[1], p[2]);
                c.status = p[3];
                complaints.add(c);
            }
        } catch (IOException e) {
            System.out.println("Could not load files.");
        }
    }
    //loadVariables() end

    private static void saveAllData() {
        saveStudents();
        saveProfessors();
        saveCourses();
        saveComplaints();
    }

    /*
        Save variables logic , Taking all the data from all the arralist and objects and writing them in their respective files.
        For student , converted the enrolled courses arraylist into a string and 
        also convverted the grades hashman into a code:grade format for further simplicity
     */
    private static void saveStudents() {
        try (FileWriter fw = new FileWriter("students.txt")) {
            for (Student s : students) {
                String enrolled = String.join(",", s.enrolledCourses); // make the string of all the items that are in enrolled courses arraylist and separate them by comma
                StringBuilder gradeStr = new StringBuilder(); // it contains code:grade form of all courses enrolled by the student and their grades in those courses
                // using string builder because it is mutable and i need to append in the string multiple times in the loop
                for (Map.Entry<String, String> e : s.grades.entrySet()) {
                    gradeStr.append(e.getKey()).append(":").append(e.getValue()).append(",");
                }
                String line = s.id + "|" + s.name + "|" + s.email + "|" + s.password + "|"
                        + s.currentSemester + "|" + enrolled + "|" + gradeStr;
                fw.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not save the data.");
        }
    }

    private static void saveProfessors() {
        //similar as saveStudents just the extra steps aint required 
        try (FileWriter fw = new FileWriter("professors.txt")) {
            for (Professor p : professors) {
                String line = p.id + "|" + p.name + "|" + p.email + "|" + p.password;
                fw.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not save the data.");
        }
    }

    private static void saveCourses() {
        try (FileWriter fw = new FileWriter("courses.txt")) {
            for (Course c : courses) {
                String pre = String.join(",", c.prerequisites);
                String line = c.courseCode + "|" + c.title + "|" + c.professorId + "|" + c.credits + "|"
                        + c.semester + "|" + c.timings + "|" + c.location + "|" + pre + "|"
                        + c.syllabus + "|" + c.enrollmentLimit + "|" + c.officeHours;
                fw.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Could not save courses.txt");
        }
    }

    private static void saveComplaints() {
        try (FileWriter fw = new FileWriter("complaints.txt")) {
            for (Complaint c : complaints) {
                String line = c.id + "|" + c.studentId + "|" + c.description + "|" + c.status;
                fw.write(line + "\n");
            }
        } catch (IOException ignored) {
        }
    }

    //menu
    private static void loginScreen() {
        try {
            clear();
            wait(500);
            System.out.println("\nLogin as:");
            System.out.println("1. Student");
            System.out.println("2. Professor");
            System.out.println("3. Administrator\n");
            int role = sc.nextInt();
            sc.nextLine();
            wait(500);
            System.out.print("Email: ");
            String email = sc.nextLine().trim();
            System.out.print("Password: ");
            String pass = sc.nextLine().trim();
            System.out.println(email + " " + pass);
            currentUser = null;

            switch (role) {
                case 1 -> {
                    for (Student s : students) {
                        if (s.login(email, pass)) {
                            currentUser = s; // sets current user as student
                            break;
                        }
                    }
                    if (currentUser == null) {
                        //register the student if not found
                        System.out.print("No account found. Signup? (y/n): ");
                        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                            signupStudent(email);
                        }
                    }
                }
                case 2 -> {
                    for (Professor p : professors) {
                        if (p.login(email, pass)) {
                            currentUser = p;
                            break;
                        }
                    }
                    // if (currentUser == null) {
                    //     // register the professor if not found
                    //     System.out.print("No account found. Signup? (y/n): ");
                    //     if (sc.nextLine().trim().equalsIgnoreCase("y")) {
                    //         signupProfessor(email);
                    //     }
                    // }
                }
                case 3 -> {
                    /*
                        admin email is "admin@uni.edu"
                        pass = "admin123" hardcoded
                     */
                    if ("admin@uni.edu".equals(email) && "admin123".equals(pass)) {
                        currentUser = new Administrator("A001", "Admin User", email, pass);
                        //create a new admin object and assing it to current user
                    }
                }
                default -> {
                }
            }

            if (currentUser != null) {
                // call upon the showMenu to show their available options..
                currentUser.showMenu();
            } else {
                System.out.println("Invalid login credentials!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Returning to main menu.");
        }
    }

    // Student Signup
    private static void signupStudent(String email) {
        System.out.print("Enter your name: ");
        String name = sc.nextLine().trim();
        String id = "S" + (students.size() + 1001);
        System.out.print("Enter your password: ");
        String password = sc.nextLine().trim(); // add a password verifying system
        Student newStudent = new Student(id, name, email, password, 1);
        students.add(newStudent);
        currentUser = newStudent;
        System.out.println("Signup successful! ID: " + id);
    }

    /*
        just taking a all info , making the type of object and assinging it to current user
     */
    // //Professor Signup
    // private static void signupProfessor(String email) {
    //     System.out.print("Enter your name: ");
    //     String name = sc.nextLine().trim();
    //     String id = "P" + (professors.size() + 1001);
    //     String password = sc.nextLine().trim(); // add a password verifying system
    //     Professor newProf = new Professor(id, name, email, password);
    //     professors.add(newProf);
    //     currentUser = newProf;
    //     System.out.println("Signup successful! ID: " + id);
    // }
    //Helper function start
    // Handy functions that are highly reuseable
    public static Course findCourse(String code) {
        for (Course c : courses) {
            if (c.courseCode.equalsIgnoreCase(code)) {
                return c;
            }
        }
        return null;
    }

    public static Student findStudentById(String id) {
        for (Student s : students) {
            if (s.id.equals(id)) {
                return s;
            }
        }
        return null;
    }

    public static Professor findProfessorById(String id) {
        for (Professor p : professors) {
            if (p.id.equals(id)) {
                return p;
            }
        }
        return null;
    }

    public static String getProfName(String profId) {
        Professor p = findProfessorById(profId);
        return p != null ? p.name : "Not Assigned";
    }
    //Helper function end
}
