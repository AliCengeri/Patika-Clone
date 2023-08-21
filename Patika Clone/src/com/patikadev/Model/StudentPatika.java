package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class StudentPatika {
    private int id;
    private int stuID;
    private int patikaID;
    private User student;
    private Patika patika;

    public StudentPatika(int id, int stuID, int patikaID) {
        this.id = id;
        this.stuID = stuID;
        this.patikaID = patikaID;
        this.student = User.getFetch(stuID);
        this.patika = Patika.getFetch(patikaID);
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

    public int getPatikaID() {
        return patikaID;
    }

    public void setPatikaID(int patikaID) {
        this.patikaID = patikaID;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Patika getPatika() {
        return patika;
    }

    public void setPatika(Patika patika) {
        this.patika = patika;
    }

    public static ArrayList<StudentPatika> getList() {
        ArrayList<StudentPatika> studentPatikaList = new ArrayList<>();
        String query = "SELECT * FROM stupatika";
        StudentPatika obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new StudentPatika(rs.getInt("id"), rs.getInt("stu_id"), rs.getInt("patika_id"));
                studentPatikaList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return studentPatikaList;
    }

    public static boolean add(Student student, String patikaName) {
        int patikaID = 0;
        for (Patika obj : Patika.getList()) {
            if (obj.getName().equals(patikaName))
                patikaID = obj.getId();
        }
        StudentPatika findStuPatika = StudentPatika.getFetch(student, patikaID);
        if (findStuPatika != null) {
            Helper.showMsg("Duplicate2");
            return false;
        }
        String query = "INSERT INTO stupatika (stu_id, patika_id) VALUES (?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1,student.getId());
            pr.setInt(2,patikaID);
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

    public static StudentPatika getFetch(Student student, int patikaID) {
        StudentPatika obj = null;
        String query = "SELECT * FROM stupatika WHERE stu_id = ? AND patika_id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, student.getId());
            pr.setInt(2, patikaID);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new StudentPatika(rs.getInt("id"), rs.getInt("stu_id"), rs.getInt("patika_id"));
            }
            pr.close();
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }
}
