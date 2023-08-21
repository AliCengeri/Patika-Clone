package com.patikadev.Model;

import com.patikadev.Helper.DBConnector;
import com.patikadev.Helper.Helper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Quiz {
    private int id;
    private String quizDesc;
    private String a;
    private String b;
    private String c;
    private String d;
    private String answer;
    private int contID;
    private int eduID;
    private Content content;
    private User educator;

    public Quiz(int id, String quizDesc, String a, String b, String c, String d, String answer, int contID, int eduID) {
        this.id = id;
        this.quizDesc = quizDesc;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.answer = answer;
        this.contID = contID;
        this.eduID = eduID;
        this.content = Content.getFetch(contID);
        this.educator = User.getFetch(eduID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuizDesc() {
        return quizDesc;
    }

    public void setQuizDesc(String quizDesc) {
        this.quizDesc = quizDesc;
    }

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getContID() {
        return contID;
    }

    public void setContID(int contID) {
        this.contID = contID;
    }

    public int getEduID() {
        return eduID;
    }

    public void setEduID(int eduID) {
        this.eduID = eduID;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public User getEducator() {
        return educator;
    }

    public void setEducator(User educator) {
        educator = educator;
    }

    public static ArrayList<Quiz> getList() {
        ArrayList<Quiz> quizList = new ArrayList<>();
        Quiz obj;
        try {
            Statement st = DBConnector.getInstance().createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM quiz");
            while (rs.next()) {
                int id = rs.getInt("id");
                String quizDesc = rs.getString("quiz_desc");
                String a = rs.getString("a");
                String b = rs.getString("b");
                String c = rs.getString("c");
                String d = rs.getString("d");
                String answer = rs.getString("answer");
                int contID = rs.getInt("cont_id");
                int eduID = rs.getInt("edu_id");
                obj = new Quiz(id, quizDesc, a, b, c, d, answer, contID, eduID);
                quizList.add(obj);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.getMessage();
        }
        return quizList;
    }

    public static boolean add(String quizDesc, String a, String b, String c, String d, String answer, String contName, Educator educator) {
        int contentID = 0;
        for (Content obj : Content.getList()) {
            if (obj.getContName().equals(contName)) {
                contentID = obj.getId();
            }
        }
        String query = "INSERT INTO quiz (quiz_desc, a, b, c, d, answer, cont_id, edu_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setString(1, quizDesc);
            pr.setString(2, a);
            pr.setString(3, b);
            pr.setString(4, c);
            pr.setString(5, d);
            pr.setString(6, answer);
            pr.setInt(7, contentID);
            pr.setInt(8, educator.getId());
            int response = pr.executeUpdate();
            pr.close();
            if (response == -1 ) {
                Helper.showMsg("error");
            }
            if (response != 1) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public static boolean delete(int id) {
        String query = "DELETE FROM quiz WHERE id = ?";
        try {
            PreparedStatement pr = DBConnector.getInstance().prepareStatement(query);
            pr.setInt(1, id);
            int response = pr.executeUpdate();
            pr.close();
            return response != -1;
        } catch (SQLException e) {
            e.getMessage();
        }
        return true;
    }
}
