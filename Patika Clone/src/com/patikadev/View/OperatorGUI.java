package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Helper.Item;
import com.patikadev.Model.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {
    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_user_list;
    private JScrollPane scrl_user_list;
    private JTable tbl_user_list;
    private JPanel pnl_user_form;
    private JTextField fld_user_name;
    private JTextField fld_user_uname;
    private JTextField fld_user_pass;
    private JComboBox cmb_user_type;
    private JButton btn_user_add;
    private JTextField fld_user_id;
    private JButton btn_user_delete;
    private JTextField fld_sh_user_name;
    private JTextField fld_sh_user_uname;
    private JComboBox cmb_sh_user_type;
    private JButton btn_user_sh;
    private JScrollPane scrl_patika_list;
    private JTable tbl_patika_list;
    private JPanel pnl_patika_add;
    private JTextField fld_patika_name;
    private JButton btn_patika_add;
    private JPanel pnl_course_list;
    private JPanel pnl_patika_list;
    private JScrollPane scrl_course_list;
    private JTable tbl_course_list;
    private JPanel pnl_course_add;
    private JTextField fld_course_name;
    private JTextField fld_course_lang;
    private JComboBox cmb_course_patika;
    private JComboBox cmb_course_user;
    private JButton btn_course_add;
    private JButton btn_course_delete;
    private JTextField fld_course_name_dlt;
    private JTable tbl_cont_list;
    private JTable tbl_quiz_list;
    private JScrollPane scrl_cont_list;
    private JPanel pnl_cont_list;
    private JPanel pnl_quiz_list;
    private JScrollPane scrl_quiz_list;
    private JTextField fld_sh_course_name;
    private JTextField fld_sh_cont_title;
    private JButton btn_cont_sh;
    private JButton btn_cont_delete;
    private JTextField fld_cont_id;
    private DefaultTableModel mdl_user_list;
    private Object[] row_user_list;
    private DefaultTableModel mdl_patika_list;
    private Object[] row_patika_list;
    private final Operator operator;
    private JPopupMenu patikaMenu;
    private DefaultTableModel mdl_course_list;
    private Object[] row_course_list;
    private DefaultTableModel mdl_cont_list;
    private Object[] row_cont_list;
    private DefaultTableModel mdl_quiz_list;
    private Object[] row_quiz_list;

    public OperatorGUI(Operator operator) {
        this.operator = operator;
        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldiniz: " + operator.getName());

        mdl_user_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_user_list = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_user_list.setColumnIdentifiers(col_user_list);
        row_user_list = new Object[col_user_list.length];
        loadUserModel();

        tbl_user_list.setModel(mdl_user_list);
        tbl_user_list.getTableHeader().setReorderingAllowed(false);

        tbl_user_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_user_id = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString();
                fld_user_id.setText(select_user_id);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });

        tbl_user_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int user_id = Integer.parseInt(tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 0).toString());
                String user_name = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 1).toString();
                String user_uname = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 2).toString();
                String user_pass = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 3).toString();
                String user_utype = tbl_user_list.getValueAt(tbl_user_list.getSelectedRow(), 4).toString();
                if (User.update(user_id, user_name, user_uname, user_pass, user_utype)) {
                    Helper.showMsg("done");
                }
                loadUserModel();
                loadEducatorCombo();
                loadCourseModel();
            }
        });

        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(select_id));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_patika_list.getValueAt(tbl_patika_list.getSelectedRow(), 0).toString());
                if (Patika.delete(select_id)) {
                    Helper.showMsg("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        mdl_patika_list = new DefaultTableModel();
        Object[] col_patika_list = {"ID", "Patika Adı"};
        mdl_patika_list.setColumnIdentifiers(col_patika_list);
        row_patika_list = new Object[col_patika_list.length];
        loadPatikaModel();

        tbl_patika_list.setModel(mdl_patika_list);
        tbl_patika_list.setComponentPopupMenu(patikaMenu);
        tbl_patika_list.getTableHeader().setReorderingAllowed(false);
        tbl_patika_list.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_patika_list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selected_row = tbl_patika_list.rowAtPoint(point);
                tbl_patika_list.setRowSelectionInterval(selected_row, selected_row);
            }
        });

        mdl_course_list = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_course_list.setColumnIdentifiers(col_courseList);
        row_course_list = new Object[col_courseList.length];
        loadCourseModel();

        tbl_course_list.setModel(mdl_course_list);
        tbl_course_list.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_course_list.getTableHeader().setReorderingAllowed(false);
        loadPatikaCombo();
        loadEducatorCombo();

        btn_user_add.addActionListener(e -> {
            if (Helper.isFildEmpty(fld_user_name) || Helper.isFildEmpty(fld_user_uname) || Helper.isFildEmpty(fld_user_pass)) {
                Helper.showMsg("fill");
            } else {
                String name = fld_user_name.getText();
                String uname = fld_user_uname.getText();
                String pass = fld_user_pass.getText();
                String utype = cmb_user_type.getSelectedItem().toString();
                if (User.add(name, uname, pass, utype)) {
                    Helper.showMsg("done");
                    loadUserModel();
                    loadEducatorCombo();
                    fld_user_name.setText(null);
                    fld_user_uname.setText(null);
                    fld_user_pass.setText(null);
                }
            }
        });

        btn_user_delete.addActionListener(e -> {
            if (Helper.isFildEmpty(fld_user_id)) {
                Helper.showMsg("fill");
            } else {
                if (Helper.confirm("sure")) {
                    int user_id = Integer.parseInt(fld_user_id.getText());
                    if (User.delete(user_id)) {
                        Helper.showMsg("done");
                        loadUserModel();
                        loadQuizModel();
                        loadCourseModel();
                        loadContentModel();
                        loadEducatorCombo();
                        fld_user_id.setText(null);
                    } else {
                        Helper.showMsg("error");
                    }
                }
            }
        });

        btn_user_sh.addActionListener(e -> {
            String name = fld_sh_user_name.getText();
            String uname = fld_sh_user_uname.getText();
            String utype = cmb_sh_user_type.getSelectedItem().toString();
            String query = User.SearchQuery(name, uname, utype);
            ArrayList<User> searchingUser = User.searchUserList(query);
            loadUserModel(searchingUser);
        });

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });

        btn_patika_add.addActionListener(e -> {
            if (Helper.isFildEmpty(fld_patika_name)) {
                Helper.showMsg("fill");
            } else {
                if (Patika.add(fld_patika_name.getText())) {
                    Helper.showMsg("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadEducatorCombo();
                    fld_patika_name.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        btn_course_add.addActionListener(e -> {
            Item patikaItem = (Item) cmb_course_patika.getSelectedItem();
            Item userItem = (Item) cmb_course_user.getSelectedItem();
            if (Helper.isFildEmpty(fld_course_name) || Helper.isFildEmpty(fld_course_lang)) {
                Helper.showMsg("fill");
            } else {
                if (Course.add(userItem.getKey(), patikaItem.getKey(), fld_course_name.getText(), fld_course_lang.getText())) {
                    Helper.showMsg("done");
                    loadCourseModel();
                    fld_course_lang.setText(null);
                    fld_course_name.setText(null);
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        tbl_course_list.getSelectionModel().addListSelectionListener(e -> {
            try {
                String select_course_name = tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 0).toString();
                fld_course_name_dlt.setText(select_course_name);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        });

        btn_course_delete.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 0).toString());
                if (Course.delete(select_id)) {
                    Helper.showMsg("done");
                    loadCourseModel();
                    loadContentModel();
                    loadQuizModel();
                } else {
                    Helper.showMsg("error");
                }
            }
        });

        tbl_course_list.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int course_id = Integer.parseInt(tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 0).toString());
                String course_name = tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 1).toString();
                String course_lang = tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 2).toString();
                String patika_name = tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 3).toString();
                String user_name = tbl_course_list.getValueAt(tbl_course_list.getSelectedRow(), 4).toString();
                if (Course.update(course_id, patika_name, user_name, course_name, course_lang)) {
                    Helper.showMsg("done");
                }
                loadUserModel();
                loadEducatorCombo();
                loadCourseModel();
            }
        });

        mdl_cont_list = new DefaultTableModel();
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
                String eduName = tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 5).toString();
                if (Content.update(contID, contName, contDesc, contURL,eduName, courseName)) {
                    Helper.showMsg("done");
                }
            }
        });

        btn_cont_sh.addActionListener(e -> {
            String name = fld_sh_course_name.getText();
            String title = fld_sh_cont_title.getText();
            String query = Content.SearchQueryAll(name, title);
            ArrayList<Content> searchingCont = Content.searchContentList(query);
            System.out.println(searchingCont.size());
            loadContentModel(searchingCont);
        });

        btn_cont_delete.addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int select_id = Integer.parseInt(tbl_cont_list.getValueAt(tbl_cont_list.getSelectedRow(), 0).toString());
                if (Content.delete(select_id)) {
                    Helper.showMsg("done");
                    loadContentModel();
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
        tbl_quiz_list.setModel(mdl_quiz_list);
        tbl_quiz_list.getTableHeader().setReorderingAllowed(false);
    }

    private void loadCourseModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_course_list.getModel();
        clearModel.setRowCount(0);

        for (Course obj : Course.getList()) {
            row_course_list[0] = obj.getId();
            row_course_list[1] = obj.getName();
            row_course_list[2] = obj.getLang();
            row_course_list[3] = obj.getPatika().getName();
            row_course_list[4] = obj.getEducator().getName();
            mdl_course_list.addRow(row_course_list);
        }
    }

    private void loadPatikaModel() {
        DefaultTableModel clearmodel = (DefaultTableModel) tbl_patika_list.getModel();
        clearmodel.setRowCount(0);
        for (Patika obj : Patika.getList()) {
            row_patika_list[0] = obj.getId();
            row_patika_list[1] = obj.getName();
            mdl_patika_list.addRow(row_patika_list);
        }
    }

    public void loadUserModel() {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        for (User obj : User.getList()) {
            row_user_list[0] = obj.getId();
            row_user_list[1] = obj.getName();
            row_user_list[2] = obj.getUname();
            row_user_list[3] = obj.getPass();
            row_user_list[4] = obj.getUtype();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadUserModel(ArrayList<User> list) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_user_list.getModel();
        clearModel.setRowCount(0);
        for (User obj : list) {
            row_user_list[0] = obj.getId();
            row_user_list[1] = obj.getName();
            row_user_list[2] = obj.getUname();
            row_user_list[3] = obj.getPass();
            row_user_list[4] = obj.getUtype();
            mdl_user_list.addRow(row_user_list);
        }
    }

    public void loadPatikaCombo() {
        cmb_course_patika.removeAllItems();
        for (Patika obj : Patika.getList()) {
            cmb_course_patika.addItem(new Item(obj.getId(), obj.getName()));
        }
    }

    public void loadEducatorCombo() {
        cmb_course_user.removeAllItems();
        for (User obj : User.getList()) {
            if (obj.getUtype().equals("Educator")) {
                cmb_course_user.addItem(new Item(obj.getId(), obj.getName()));
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
