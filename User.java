public abstract class User {
    String id;
    String name;
    String email;
    String password;

    User(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    boolean login(String inputEmail, String inputPass) {
        return email.equals(inputEmail) && password.equals(inputPass);
    }

    abstract void showMenu();
}