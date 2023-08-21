package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Rating {
    private int id;
    private int stuID;
    private int contentID;
    private String comment;
    private int point;
    private Content content;
    private User student;

    public Rating(int id, int stuID, int contentID, String comment, int point) {
        this.id = id;
        this.stuID = stuID;
        this.contentID = contentID;
        this.comment = comment;
        this.point = point;
        this.content = Content.getFetch(contentID);
        this.student = User.getFetch(stuID);
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

    public int getContentID() {
        return contentID;
    }

    public void setContentID(int contentID) {
        this.contentID = contentID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public static ArrayList<Rating> getList() {
        ArrayList<Rating> ratingList = new ArrayList<>();
        Rating obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM rating");
            while (rs.next()) {
                int id = rs.getInt("id");
                int stuID = rs.getInt("stu_id");
                int contentID = rs.getInt("content_id");
                String comment = rs.getString("comment");
                int point = rs.getInt("point");
                obj = new Rating(id, stuID, contentID, comment, point);
                ratingList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return ratingList;
    }

    public static boolean add(Student student, int contentID, String comment, int point) {
        String query = "INSERT INTO rating (stu_id, content_id, comment, point) VALUES (?, ?, ?, ?)";
        for (Rating obj : Rating.getList()) {
            if (obj.getStuID() == student.getId()) {
                Helper.showMsg("Duplicate Comment");
                return false;
            }
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,student.getId());
            pr.setInt(2, contentID);
            pr.setString(3,comment);
            pr.setInt(4, point);
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
}
