package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Operator;
import com.patikadev.Model.Student;
import com.patikadev.Model.User;

import javax.swing.*;

public class LoginGUI extends JFrame {
    private JPanel wrapper;
    private JPanel wbottom;
    private JPanel wtop;
    private JTextField fld_user_uname;
    private JTextField fld_user_pass;
    private JButton btn_login;
    private JTabbedPane tabbedPane1;
    private JTextField fld_sign_name;
    private JTextField fld_sign_uname;
    private JTextField fld_sign_pass;
    private JButton btn_sign;

    public LoginGUI() {
        add(wrapper);
        setSize(400, 400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        btn_login.addActionListener(e -> {
            if (Helper.isFildEmpty(fld_user_uname) || Helper.isFildEmpty(fld_user_pass)) {
                Helper.showMsg("fill");
            } else {
                User u = User.getFetch(fld_user_uname.getText(), fld_user_pass.getText());
                if (u == null) {
                    Helper.showMsg("Kullanıcı bulunamadı !");
                } else {
                    switch (u.getUtype()) {
                        case "Operator":
                            Helper.setLayout();
                            OperatorGUI opGUI = new OperatorGUI((Operator) u);
                            break;
                        case "Educator":
                            Helper.setLayout();
                            EducatorGUI edGUI = new EducatorGUI((Educator) u);
                            break;
                        case "Student":
                            Helper.setLayout();
                            StudentGUI stGUI = new StudentGUI((Student) u);
                            break;
                    }
                    dispose();
                }
            }
        });
        btn_sign.addActionListener(e -> {
            if (Helper.isFildEmpty(fld_sign_name) || Helper.isFildEmpty(fld_sign_uname) || Helper.isFildEmpty(fld_sign_pass)) {
                Helper.showMsg("fill");
            } else {
                if (User.add(fld_sign_name.getText(), fld_sign_uname.getText(), fld_sign_pass.getText(), "Student")) {
                    Helper.showMsg("done");
                    fld_sign_name.setText(null);
                    fld_sign_uname.setText(null);
                    fld_sign_pass.setText(null);
                }
            }
        });
    }

    public static void main(String[] args) {
        LoginGUI login = new LoginGUI();
    }
}