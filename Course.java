
import java.util.ArrayList;

public class Course {

    String courseCode, title, professorId, timings, location, syllabus, officeHours;
    int credits, semester, enrollmentLimit;
    ArrayList<String> prerequisites;

    Course( String code,
            String title,
            String profId,
            int credits,
            int semester,
            String timings,
            String location,
            ArrayList<String> prereqss,
            String syllabus,
            int enrollmentLimit,
            String officeHours)
    {
        this.courseCode = code;
        this.title = title;
        this.professorId = profId;
        this.credits = credits;
        this.semester = semester;
        this.timings = timings;
        this.location = location;
        this.prerequisites = prereqss != null ? prereqss : new ArrayList<>();
        this.syllabus = syllabus != null ? syllabus : "Not provided";
        this.enrollmentLimit = enrollmentLimit;
        this.officeHours = officeHours != null ? officeHours : "Not provided";
    }
}
