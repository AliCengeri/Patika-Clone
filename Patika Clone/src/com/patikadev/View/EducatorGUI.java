package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.Content;
import com.patikadev.Model.Course;
import com.patikadev.Model.Educator;
import com.patikadev.Model.Quiz;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EducatorGUI extends JFrame {
    private JPanel wrapper;
    private JButton btn_logout;
    private JPanel pnl_top;
    private JScrollPane scrl_edu_list;
    private JTable tbl_edu_list;
    private JTextField fld_sh_course_name;
    private JTextField fld_sh_cont_title;
    private JButton btn_cont_sh;
    private JScrollPane scrl_cont_list;
    private JTable tbl_cont_list;
    private JTextField fld_cont_name;
    private JTextField fld_cont_desc;
    private JTextField fld_cont_url;
    private JComboBox cmb_course_name;
    private JButton btn_cont_add;
    private JButton btn_cont_delete;
    private JTextField fld_cont_id;
    private JTextField fld_quiz_desc;
    private JButton btn_quiz_add;
    private JTextField fld_quiz_1;
    private JTextField fld_quiz_2;
    private JTextField fld_quiz_3;
    private JTextField fld_quiz_4;
    private JComboBox cmb_quiz_ans;
    private JTable tbl_quiz_list;
    private JScrollPane scrl_quiz_list;
    private JComboBox cmb_quiz_cont_name;
    private JPanel pnl_edu_list;
    private JPanel pnl_cont_list;
    private JPanel pnl_quiz_list;
    private JPanel pnl_add_content;
    private JPanel pnl_add_quiz;
    private JTabbedPane tab_operator;
    private JButton btn_quiz_delete;
    private JTextField fld_quiz_id;
    private JTextField fld_edu_id;
    private DefaultTableModel mdl_edu_list;
    private Object[] row_edu_list;
    private DefaultTableModel mdl_cont_list;
    private Object[] row_cont_list;
    private DefaultTableModel mdl_quiz_list;
    private Object[] row_quiz_list;

    public EducatorGUI(Educator educator) {
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);
        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });

        mdl_edu_list = new DefaultTableModel();
        Object[] col_edu_list = {"ID", "Ders Adı", "Programlama Dili", "Patika Adı"};
        mdl_edu_list.setColumnIdentifiers(col_edu_list);
        row_edu_list = new Object[col_edu_list.length];
        loadEduModel(educator);
        loadCourseCombo(educator);
        tbl_edu_list.setModel(mdl_edu_list);
        tbl_edu_list.getTableHeader().setReorderingAllowed(false);

        mdl_cont_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 4 || column == 5) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };

        Object[] col_cont_list = {"ID", "İçerik Adı", "Açıklama", "URL", "Dersin Adı", "Eğitmen"};
        mdl_cont_list.setColumnIdentifiers(col_cont_list);
        row_cont_list = new Object[col_cont_list.length];
        loadContentModel();
        tbl_cont_list.setModel(mdl_cont_list);
        tbl_cont_list.getTableHeader().setReorderingAllowed(false);

        tbl_cont_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_cont_id = tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 0).toString();
                fld_cont_id.setText(select_cont_id);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });

        tbl_cont_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int contID = Integer.parseInt(tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 0).toString());
                String contName = tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 1).toString();
                String contDesc = tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 2).toString();
                String contURL = tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 3).toString();
                String courseName = tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 4).toString();
                if (Content.update(contID, contName, contDesc, contURL, educator.getName(), courseName)) {
                    Helper.showMsg("done");
                    loadContentCombo(educator);
                    loadCourseCombo(educator);
                }
            }
        });

        btn_cont_sh.addActionListener(e -> {
            String name = fld_sh_course_name.getText();
            String title = fld_sh_cont_title.getText();
            String query = Content.SearchQuery(name, title, educator.getId());
            ArrayList<Content> searchingCont = Content.searchContentList(query);
            System.out.println(searchingCont.size());
            loadContentModel(searchingCont);
        });

        btn_cont_add.addActionListener(e -> {
            if (Helper.isFildEmpty(fld_cont_name) || Helper.isFildEmpty(fld_cont_desc) || Helper.isFildEmpty(fld_cont_url)) {
                Helper.showMsg("fill");
            } else {
                String contName = fld_cont_name.getText();
                String contDesc = fld_cont_desc.getText();
                String contURL = fld_cont_url.getText();
                String courseName = cmb_course_name.getSelectedItem().toString();
                if (Content.add(contName, contDesc, contURL, courseName, educator)) {
                    Helper.showMsg("done");
                    loadContentModel();
                    loadContentCombo(educator);
                    fld_cont_name.setText(null);
                    fld_cont_desc.setText(null);
                    fld_cont_url.setText(null);
                }
            }
        });

        btn_cont_delete.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 0).toString());
                if (Content.delete(select_id)) {
                    Helper.showMsg("done");
                    loadContentModel();
                    loadContentCombo(educator);
                    loadQuizModel();
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        mdl_quiz_list = new DefaultTableModel();
        Object[] col_quiz_list = {"ID", "Soru", "A Şıkkı", "B Şıkkı", "C Şıkkı", "D şıkkı", "Doğru Şık", "İçeriğin Adı", "Eğitmen"};
        mdl_quiz_list.setColumnIdentifiers(col_quiz_list);
        row_quiz_list = new Object[col_quiz_list.length];
        loadQuizModel();
        loadContentCombo(educator);
        tbl_quiz_list.setModel(mdl_quiz_list);
        tbl_quiz_list.getTableHeader().setReorderingAllowed(false);

        btn_quiz_add.addActionListener(e -> {
            if (Helper.isFildEmpty(fld_quiz_desc) || Helper.isFildEmpty(fld_quiz_1) || Helper.isFildEmpty(fld_quiz_2) ||
                    Helper.isFildEmpty(fld_quiz_3) || Helper.isFildEmpty(fld_quiz_4)) {
                Helper.showMsg("fill");
            } else {
                String contName = cmb_quiz_cont_name.getSelectedItem().toString();
                String quizDesc = fld_quiz_desc.getText();
                String a = fld_quiz_1.getText();
                String b = fld_quiz_2.getText();
                String c = fld_quiz_3.getText();
                String d = fld_quiz_4.getText();
                String answer = cmb_quiz_ans.getSelectedItem().toString();
                if (Quiz.add(quizDesc, a, b, c, d, answer, contName, educator)) {
                    Helper.showMsg("done");
                    loadQuizModel();
                    fld_quiz_desc.setText(null);
                    fld_quiz_1.setText(null);
                    fld_quiz_2.setText(null);
                    fld_quiz_3.setText(null);
                    fld_quiz_4.setText(null);
                }
            }
        });

        tbl_quiz_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_quiz_id = tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(), 0).toString();
                fld_quiz_id.setText(select_quiz_id);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });

        btn_quiz_delete.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int quizID = Integer.parseInt(tbl_quiz_list.getValueAt(tbl_quiz_list.getSelectedRow(), 0).toString());
                if (Quiz.delete(quizID)) {
                    Helper.showMsg("done");
                    loadQuizModel();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
    }

    private void loadEduModel(Educator educator) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_edu_list.getModel();
        clearModel.setRowCount(0);
        for (Course obj : Course.getList()) {
            if (educator.getName().equals(obj.getEducator().getName())) {
                row_edu_list[0] = obj.getId();
                row_edu_list[1] = obj.getName();
                row_edu_list[2] = obj.getLang();
                row_edu_list[3] = obj.getPatika().getName();
                mdl_edu_list.addRow(row_edu_list);
            }
        }
    }

    private void loadContentModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_cont_list.getModel();
        clearModel.setRowCount(0);
        for (Content obj : Content.getList()) {
            row_cont_list[0] = obj.getId();
            row_cont_list[1] = obj.getContName();
            row_cont_list[2] = obj.getContDesc();
            row_cont_list[3] = obj.getContURL();
            row_cont_list[4] = obj.getCourse().getName();
            row_cont_list[5] = obj.getEducator().getName();
            mdl_cont_list.addRow(row_cont_list);
        }
    }

    private void loadContentModel(ArrayList<Content> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_cont_list.getModel();
        clearModel.setRowCount(0);
        for (Content obj : list) {
            row_cont_list[0] = obj.getId();
            row_cont_list[1] = obj.getContName();
            row_cont_list[2] = obj.getContDesc();
            row_cont_list[3] = obj.getContURL();
            row_cont_list[4] = obj.getCourse().getName();
            row_cont_list[5] = obj.getEducator().getName();
            mdl_cont_list.addRow(row_cont_list);
        }
    }

    public void loadCourseCombo(Educator educator) {
        cmb_course_name.removeAllItems();
        for (Course obj : Course.getList()) {
            if (obj.getUser_id() == educator.getId()) {
                cmb_course_name.addItem(new Item(obj.getId(), obj.getName()));
            }
        }
    }

    public void loadContentCombo(Educator educator) {
        cmb_quiz_cont_name.removeAllItems();
        for (Content obj : Content.getList()) {
            if (obj.getEduID() == educator.getId()) {
                cmb_quiz_cont_name.addItem(new Item(obj.getId(), obj.getContName()));
            }
        }
    }

    public void loadQuizModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_quiz_list.getModel();
        clearModel.setRowCount(0);
        for (Quiz obj : Quiz.getList()) {
            row_quiz_list[0] = obj.getId();
            row_quiz_list[1] = obj.getQuizDesc();
            row_quiz_list[2] = obj.getA();
            row_quiz_list[3] = obj.getB();
            row_quiz_list[4] = obj.getC();
            row_quiz_list[5] = obj.getD();
            row_quiz_list[6] = obj.getAnswer();
            row_quiz_list[7] = obj.getContent().getContName();
            row_quiz_list[8] = obj.getEducator().getName();
            mdl_quiz_list.addRow(row_quiz_list);
        }
    }
}