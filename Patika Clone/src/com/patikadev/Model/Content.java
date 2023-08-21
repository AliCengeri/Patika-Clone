package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Content {
    private int id;
    private String contName;
    private String contDesc;
    private String contURL;
    private int courseID;
    private int eduID;
    private Course course;
    private User educator;

    public Content(int id, String contName, String contDesc, String contURL, int courseID, int eduID) {
        this.id = id;
        this.contName = contName;
        this.contDesc = contDesc;
        this.contURL = contURL;
        this.courseID = courseID;
        this.eduID = eduID;
        this.course = Course.getFetch(courseID);
        this.educator = User.getFetch(eduID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getContDesc() {
        return contDesc;
    }

    public void setContDesc(String contDesc) {
        this.contDesc = contDesc;
    }

    public String getContURL() {
        return contURL;
    }

    public void setContURL(String contURL) {
        this.contURL = contURL;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getEduID() {
        return eduID;
    }

    public void setEduID(int eduID) {
        this.eduID = eduID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }

    public static ArrayList<Content> getList() {
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM content");
            while (rs.next()) {
                int id = rs.getInt("id");
                String contName = rs.getString("cont_name");
                String contDesc = rs.getString("cont_desc");
                String contURL = rs.getString("cont_url");
                int courseID = rs.getInt("course_id");
                int eduID = rs.getInt("edu_id");
                obj = new Content(id, contName, contDesc, contURL, courseID, eduID);
                contentList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return contentList;
    }

    public static String SearchQuery(String name, String title, int edu_id) {
        int courseID = 0;
        for (Course obj : Course.getList()) {
            if (name.equals(obj.getName())) {
                courseID = obj.getId();
            }
        }
        String query = "SELECT * FROM content WHERE course_id = " + courseID + " AND cont_name ILIKE '%" + title + "%' AND edu_id = " + edu_id;
        return query;
    }

    public static String SearchQueryAll(String name, String title) {
        int courseID = 0;
        for (Course obj : Course.getList()) {
            if (name.equals(obj.getName())) {
                courseID = obj.getId();
            }
        }
        String query = "SELECT * FROM content WHERE course_id = " + courseID + " AND cont_name ILIKE '%" + title + "%'";
        return query;
    }

    public static ArrayList<Content> searchContentList(String query) {
        ArrayList<Content> contentList = new ArrayList<>();
        Content obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String contName = rs.getString("cont_name");
                String contDesc = rs.getString("cont_desc");
                String contURL = rs.getString("cont_url");
                int courseID = rs.getInt("course_id");
                int eduID = rs.getInt("edu_id");
                obj = new Content(id, contName, contDesc, contURL, courseID, eduID);
                contentList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentList;
    }

    public static boolean add(String contName, String contDesc, String contURL, String courseName, Educator educator) {
        int course_id = 0;
        for (Course obj : Course.getList()) {
            if (courseName.equals(obj.getName())) {
                course_id = obj.getId();
            }
        }
        String query = "INSERT INTO content (cont_name, cont_desc, cont_url, course_id, edu_id) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, contName);
            pr.setString(2, contDesc);
            pr.setString(3,contURL);
            pr.setInt(4,course_id);
            pr.setInt(5, educator.getId());
            int response = pr.executeUpdate();
            pr.close();
            if (response == -1) {
                Helper.showMsg("error");
            }
            if (response != 1) {
                return true;
            }
        } catch (SQLException e) {
            e.getMessage();
        }
        return true;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM content WHERE id = ?";
        for (Quiz obj : Quiz.getList()) {
            if (obj.getContID() == id) {
                Quiz.delete(obj.getId());
            }
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            int response = pr.executeUpdate();
            pr.close();
            return response != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean update(int contID, String contName, String contDesc, String contURL, String eduName, String courseName) {
        int eduID = 0;
        int courseID = 0;
        for (Course obj : Course.getList()) {
            if (courseName.equals(obj.getName())) {
                courseID = obj.getId();
            }
        }
        for (User obj : User.getList()) {
            if (obj.getName().equals(eduName) && obj.getUtype().equals("Educator")) {
                eduID = obj.getId();
            }
        }
        if (eduID == 0 || courseID == 0) {
            Helper.showMsg("error");
            return false;
        }
        String query = "UPDATE content SET cont_name = ?, cont_desc = ?, cont_url = ?, course_id = ?, edu_id = ? WHERE id = ? ";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, contName);
            pr.setString(2, contDesc);
            pr.setString(3, contURL);
            pr.setInt(4, courseID);
            pr.setInt(5, eduID);
            pr.setInt(6, contID);
            int response = pr.executeUpdate();
            pr.close();
            return response != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Content getFetch(int id) {
        Content obj = null;
        String query = "SELECT * FROM content WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                obj = new Content(rs.getInt("id"),
                        rs.getString("cont_name"),
                        rs.getString("cont_desc"),
                        rs.getString("cont_url"),
                        rs.getInt("edu_id"),
                        rs.getInt("course_id"));
            }
            pr.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
