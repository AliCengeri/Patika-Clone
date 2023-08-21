package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Rating;
import com.patikadev.Model.Student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class CommentGUI extends JFrame{
    private JPanel wrapper;
    private JTextArea fld_comment;
    private JComboBox cmb_point;
    private JButton btn_comment;

    public CommentGUI(Student student, int contentID) {
        add(wrapper);
        setSize(500, 300);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);


        btn_comment.addActionListener(e -> {
            int point = Integer.parseInt(cmb_point.getSelectedItem().toString());
            if (Helper.isFildEmpty(fld_comment)) {
                Helper.showMsg("fill");
            } else if (Rating.add(student, contentID, fld_comment.getText(), point)) {
                Helper.showMsg("done");
            }
        });
    }
}
