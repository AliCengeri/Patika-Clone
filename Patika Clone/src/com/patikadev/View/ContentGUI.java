package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContentGUI extends JFrame {
    private JPanel wrapper;
    private JButton btn_logout;
    private JLabel fld_cont_name;
    private JLabel fld_content_url;
    private JLabel fld_content_desc;
    private JButton btn_comment;
    private JButton btn_comments;
    private JButton btn_quiz;

    public ContentGUI(Student student, int contentID, String courseName, String courseDesc, String courseURL) {
        add(wrapper);
        setSize(800, 400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        fld_cont_name.setText(courseName);
        fld_content_desc.setText(courseDesc);
        fld_content_url.setText(courseURL);

        btn_logout.addActionListener(e -> {
            StudentGUI studentGUI = new StudentGUI(student);
            dispose();
        });

        btn_comment.addActionListener(e -> {
            CommentGUI commentGUI = new CommentGUI(student, contentID);
        });

        btn_comments.addActionListener(e -> {
            AllCommentsGUI allCommentsGUI = new AllCommentsGUI(student, contentID);
        });

        btn_quiz.addActionListener(e -> {
            QuizGUI quizGUI = new QuizGUI(student);
        });
    }
}
