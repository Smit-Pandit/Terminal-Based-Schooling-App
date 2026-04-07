public class Complaint {
    String id, studentId, description, status;

    Complaint(String id, String studentId, String description) {
        this.id = id;
        this.studentId = studentId;
        this.description = description;
        this.status = "Pending";
    }
}