package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;
import com.patikadev.View.EducatorGUI;
import com.patikadev.View.OperatorGUI;
import com.patikadev.View.StudentGUI;
import com.sun.source.tree.WhileLoopTree;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    private int id;
    private String name;
    private String uname;
    private String pass;
    private String utype;

    public User() {
    }

    public User(int id, String name, String uname, String pass, String utype) {
        this.id = id;
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.utype = utype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public static ArrayList<User> getList() {
        ArrayList<User> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setUtype(rs.getString("utype"));
                userList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static boolean add(String name, String uname, String pass, String utype) {
        String query = "INSERT INTO users (name, uname, pass, utype) VALUES (?, ?, ?, ?)";
        User findUser = User.getFetch(uname);
        if (findUser != null) {
            Helper.showMsg("Duplicate");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, uname);
            pr.setString(3, pass);
            pr.setString(4, utype);
            int response = pr.executeUpdate();
            pr.close();
            if (response == -1) {
                Helper.showMsg("error");
            }
            return response != -1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static User getFetch(String uname) {
        User obj = null;
        String query = "SELECT * FROM users WHERE uname = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setUtype(rs.getString("utype"));
            }
            pr.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static User getFetch(String uname, String pass) {
        User obj = null;
        String query = "SELECT * FROM users WHERE uname = ? AND pass = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, uname);
            pr.setString(2, pass);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                switch (rs.getString("utype")) {
                    case "Operator":
                        obj = new Operator();
                        break;
                    case "Educator":
                        obj = new Educator();
                        break;
                    case "Student":
                        obj = new Student();
                        break;
                }
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setUtype(rs.getString("utype"));
            }
            pr.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static User getFetch(int id) {
        User obj = null;
        String query = "SELECT * FROM users WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setUtype(rs.getString("utype"));
            }
            pr.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        ArrayList<Course> courseList = Course.getListByUser(id);
        for (Course c : courseList) {
            Course.delete(c.getId());
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            int responce = pr.executeUpdate();
            pr.close();
            return responce != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean update(int id, String name, String uname, String pass, String utype) {
        if (!utype.equals("Operator") && !utype.equals("Educator") && !utype.equals("Student")) {
            Helper.showMsg("error");
            return false;
        }
        String query = "UPDATE users SET name = ?, uname = ?, pass = ?, utype = ? WHERE id = ? ";
        User findUser = User.getFetch(uname);
        if (findUser != null && findUser.getId() != id) {
            Helper.showMsg("Duplicate");
            return false;
        }
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, name);
            pr.setString(2, uname);
            pr.setString(3, pass);
            pr.setString(4, utype);
            pr.setInt(5, id);
            int responce = pr.executeUpdate();
            pr.close();
            return responce != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static ArrayList<User> searchUserList(String query) {
        ArrayList<User> userList = new ArrayList<>();
        User obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                obj = new User();
                obj.setId(rs.getInt("id"));
                obj.setName(rs.getString("name"));
                obj.setUname(rs.getString("uname"));
                obj.setPass(rs.getString("pass"));
                obj.setUtype(rs.getString("utype"));
                userList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public static String SearchQuery(String name, String uname, String utype) {
        String query = "SELECT * FROM users WHERE name ILIKE '%" + name + "%' AND uname ILIKE '%" + uname + "%' AND utype ILIKE '%" + utype + "%'";
        return query;
    }
}