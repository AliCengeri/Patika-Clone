package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentCourse {
    private int id;
    private int stuID;
    private int courseID;
    private User student;
    private Course course;

    public StudentCourse(int id, int stuID, int courseID) {
        this.id = id;
        this.stuID = stuID;
        this.courseID = courseID;
        this.student = User.getFetch(stuID);
        this.course = Course.getFetch(courseID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStuID() {
        return stuID;
    }

    public void setStuID(int stuID) {
        this.stuID = stuID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public static ArrayList<StudentCourse> getList() {
        ArrayList<StudentCourse> studentCourseList = new ArrayList<>();
        String query = "SELECT * FROM stucourse";
        StudentCourse obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new StudentCourse(rs.getInt("id"), rs.getInt("stu_id"), rs.getInt("course_id"));
                studentCourseList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentCourseList;
    }

    public static boolean add(Student student, String courseName) {
        int courseID = 0;
        for (Course obj : Course.getList()) {
            if (obj.getName().equals(courseName))
                courseID = obj.getId();
        }
        StudentCourse findStuCourse= StudentCourse.getFetch(student, courseID);
        if (findStuCourse != null) {
            Helper.showMsg("Duplicate2");
            return false;
        }
        String query = "INSERT INTO stucourse (stu_id, course_id) VALUES (?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,student.getId());
            pr.setInt(2,courseID);
            int response = pr.executeUpdate();
            pr.close();
            if (response == -1) {
                Helper.showMsg("error");
            }
            if (response != 1) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static StudentCourse getFetch(Student student, int courseID) {
        StudentCourse obj = null;
        String query = "SELECT * FROM stucourse WHERE stu_id = ? AND course_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, student.getId());
            pr.setInt(2, courseID);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new StudentCourse(rs.getInt("id"), rs.getInt("stu_id"), rs.getInt("course_id"));
            }
            pr.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}
