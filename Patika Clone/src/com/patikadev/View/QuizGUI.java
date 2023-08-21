package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Quiz;
import com.patikadev.Model.Student;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class QuizGUI extends JFrame {
    private JPanel wrapper;
    private JButton btn_d;
    private JButton btn_c;
    private JButton btn_b;
    private JButton btn_a;
    private JButton btn_next;
    private JButton btn_prev;
    private JLabel fld_quiz_desc;
    private int i = 0;

    public QuizGUI(Student student) {
        add(wrapper);
        setSize(600, 400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        ArrayList<Quiz> quizList = Quiz.getList();
        fld_quiz_desc.setText(quizList.get(i).getQuizDesc());
        btn_a.setText(quizList.get(i).getA());
        btn_b.setText(quizList.get(i).getB());
        btn_c.setText(quizList.get(i).getC());
        btn_d.setText(quizList.get(i).getD());

        btn_next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                i++;
                if (i == quizList.size()) {
                    i = 0;
                }
                fld_quiz_desc.setText(quizList.get(i).getQuizDesc());
                btn_a.setText(quizList.get(i).getA());
                btn_b.setText(quizList.get(i).getB());
                btn_c.setText(quizList.get(i).getC());
                btn_d.setText(quizList.get(i).getD());
            }
        });

        btn_prev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                i--;
                if (i == -1) {
                    i = quizList.size() - 1;
                }
                fld_quiz_desc.setText(quizList.get(i).getQuizDesc());
                btn_a.setText(quizList.get(i).getA());
                btn_b.setText(quizList.get(i).getB());
                btn_c.setText(quizList.get(i).getC());
                btn_d.setText(quizList.get(i).getD());
            }
        });

        btn_a.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn_a.getText().equals(quizList.get(i).getAnswer())) {
                    Helper.showMsg("Correct");
                } else {
                    Helper.showMsg("False");
                }
            }
        });

        btn_b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn_b.getText().equals(quizList.get(i).getAnswer())) {
                    Helper.showMsg("Correct");
                } else {
                    Helper.showMsg("False");
                }
            }
        });

        btn_c.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn_c.getText().equals(quizList.get(i).getAnswer())) {
                    Helper.showMsg("Correct");
                } else {
                    Helper.showMsg("False");
                }
            }
        });

        btn_d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btn_d.getText().equals(quizList.get(i).getAnswer())) {
                    Helper.showMsg("Correct");
                } else {
                    Helper.showMsg("False");
                }
            }
        });
    }
}
