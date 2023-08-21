package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Course {
    private int id;
    private int user_id;
    private int patika_id;
    private String name;
    private String lang;
    private Patika patika;
    private User educator;

    public Course(int id, int user_id, int patika_id, String name, String lang) {
        this.id = id;
        this.user_id = user_id;
        this.patika_id = patika_id;
        this.name = name;
        this.lang = lang;
        this.patika = Patika.getFetch(patika_id);
        this.educator = User.getFetch(user_id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPatika_id() {
        return patika_id;
    }

    public void setPatika_id(int patika_id) {
        this.patika_id = patika_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        this.educator = educator;
    }

    public static ArrayList<Course> getList() {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course");
            while (rs.next()) {
                int id = rs.getInt("id");
                int user_id = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, user_id, patika_id, name, lang);
                courseList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return courseList;
    }

    public static boolean add(int user_id, int patika_id, String name, String lang) {
        String query = "INSERT INTO course (user_id, patika_id, name, lang) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, user_id);
            pr.setInt(2, patika_id);
            pr.setString(3, name);
            pr.setString(4, lang);
            int response = pr.executeUpdate();
            pr.close();
            return response != -1;
        } catch (SQLException e) {
            e.getMessage();
        }
        return true;
    }

    public static ArrayList<Course> getListByUser(int user_id) {
        ArrayList<Course> courseList = new ArrayList<>();
        Course obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM course WHERE user_id = " + user_id);
            while (rs.next()) {
                int id = rs.getInt("id");
                int userID = rs.getInt("user_id");
                int patika_id = rs.getInt("patika_id");
                String name = rs.getString("name");
                String lang = rs.getString("lang");
                obj = new Course(id, userID, patika_id, name, lang);
                courseList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return courseList;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM course WHERE id = ?";
        for (Content obj : Content.getList()) {
            if (obj.getCourseID() == id) {
                Content.delete(obj.getId());
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

    public static boolean update(int id, String patikaName, String userName, String course_name, String lang) {
        int patikaID = 0;
        int userID = 0;
        for (Patika obj : Patika.getList()) {
            if (obj.getName().equals(patikaName)) {
                patikaID = obj.getId();
            }
        }
        for (User obj : User.getList()) {
            if (obj.getName().equals(userName) && obj.getUtype().equals("Educator")) {
                userID = obj.getId();
            }
        }
        if (patikaID == 0 || userID == 0) {
            Helper.showMsg("error");
            return false;
        }
        String query = "UPDATE course SET user_id = ?, patika_id = ?, name = ?, lang = ? WHERE id = ? ";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, userID);
            pr.setInt(2, patikaID);
            pr.setString(3, course_name);
            pr.setString(4, lang);
            pr.setInt(5, id);
            int response = pr.executeUpdate();
            pr.close();
            return response != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static Course getFetch(int id) {
        Course obj = null;
        String query = "SELECT * FROM course WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                obj = new Course(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("patika_id"), rs.getString("name"), rs.getString("lang"));
            }
            pr.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
