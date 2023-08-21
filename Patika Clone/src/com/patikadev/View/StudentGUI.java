package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StudentGUI extends JFrame {
    private JPanel wrapper;
    private JButton btn_logout;
    private JTabbedPane tab_operator;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JButton btn_add_patika;
    private JTextField fld_patika_name;
    private JPanel pnl_top;
    private JTable tbl_course_list;
    private JPanel pnl_course_list;
    private JScrollPane scrl_course_list;
    private JButton btn_add_course;
    private JTextField fld_course_name;
    private JPanel pnl_content_list;
    private JScrollPane scrl_content_list;
    private JTable tbl_content_list;
    private JButton btn_content_exm;
    private JTextField fld_content_id;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private DefaultTableModel mdl_content_list;
    private Object[] row_content_list;

    public StudentGUI(Student student) {
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

        mdl_patika_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();
        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);

        tbl_patika_list.getSelectionModel().addListSelectionListener(e -> {
            String select_patika_name = tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 1).toString();
            fld_patika_name.setText(select_patika_name);
        });

        btn_add_patika.addActionListener(e -> {
            String patikaName = fld_patika_name.getText();
            if (StudentPatika.add(student, patikaName)) {
                Helper.showMsg("done");
                fld_patika_name.setText(null);
                loadCourseModel(student);
            }
        });

        mdl_course_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == 2 || column == 3 || column == 4) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_course_list = {"ID", "Dersin Adı", "Kullanılan Dil", "Patika Adı", "Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_course_list);
        row_course_list = new Object[col_course_list.length];
        loadCourseModel(student);
        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);

        tbl_course_list.getSelectionModel().addListSelectionListener(e -> {
            String select_course_name = tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 1).toString();
            fld_course_name.setText(select_course_name);
        });

        btn_add_course.addActionListener(e -> {
            String courseName = fld_course_name.getText();
            if (StudentCourse.add(student, courseName)) {
                Helper.showMsg("done");
                fld_course_name.setText(null);
            }
        });

        mdl_content_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == 2 || column == 3 || column == 4 || column == 5) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_content_list = {"ID", "İçeriğin Adı", "İçeriğin Açıklaması", "URL", "Dersin Adı", "Eğitmen"};
        mdl_content_list.setColumnIdentifiers(col_content_list);
        row_content_list = new Object[col_content_list.length];
        loadContentModel(student);
        tbl_content_list.setModel(mdl_content_list);
        tbl_content_list.getTableHeader().setReorderingAllowed(false);

        tbl_content_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int contentID = Integer.parseInt(tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 0).toString());
                    String contentName = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 1).toString();
                    String contentDesc = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 2).toString();
                    String contentURL = tbl_content_list.getValueAt(tbl_content_list.getSelectedRow(), 3).toString();
                    ContentGUI contentGUI = new ContentGUI(student, contentID, contentName, contentDesc, contentURL);
                    dispose();
                }
            }
        });

    }

    public void loadPatikaModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patika_list.getModel();
        clearModel.setRowCount(0);
        for (Patika obj : Patika.getList()) {
            row_patika_list[0] = obj.getId();
            row_patika_list[1] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }

    public void loadCourseModel(Student student) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);
        for (Course obj : Course.getList()) {
            for (StudentPatika obj2 : StudentPatika.getList()) {
                if (obj.getPatika_id() == obj2.getPatikaID() && student.getId() == obj2.getStuID()) {
                    row_course_list[0] = obj.getId();
                    row_course_list[1] = obj.getName();
                    row_course_list[2] = obj.getLang();
                    row_course_list[3] = obj.getPatika().getName();
                    row_course_list[4] = obj.getEducator().getName();
                    mdl_course_list.addRow(row_course_list);
                }
            }
        }
    }

    public void loadContentModel(Student student) {
        DefaultTableModel clearmodel = (DefaultTableModel) tbl_content_list.getModel();
        clearmodel.setRowCount(0);
        for (StudentCourse obj : StudentCourse.getList()) {
            for (Content obj2 : Content.getList()) {
                if (obj.getStuID() == student.getId() && obj.getCourseID() == obj2.getCourseID()) {
                    row_content_list[0] = obj2.getId();
                    row_content_list[1] = obj2.getContName();
                    row_content_list[2] = obj2.getContDesc();
                    row_content_list[3] = obj2.getContURL();
                    row_content_list[4] = obj2.getCourse().getName();
                    row_content_list[5] = obj2.getEducator().getName();
                    mdl_content_list.addRow(row_content_list);
                }
            }
        }
    }
}
