package com.patikadev.View;

import com.patikadev.Helper.Config;
import com.patikadev.Helper.Helper;
import com.patikadev.Model.Content;
import com.patikadev.Model.Rating;
import com.patikadev.Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AllCommentsGUI extends JFrame{
    private JPanel wrapper;
    private JScrollPane scrl_comment_list;
    private JTable tbl_comment_list;
    private JButton btn_logout;
    private DefaultTableModel mdl_comment_list;
    private Object[] row_comment_list;
    public AllCommentsGUI(Student student, int contentID) {
        add(wrapper);
        setSize(600, 400);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setResizable(false);
        setVisible(true);

        mdl_comment_list = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0 || column == 1 || column == 2) {
                    return false;
                }
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_comment_list = {"Yorumun Sahibi", "Yorum", "Puan"};
        mdl_comment_list.setColumnIdentifiers(col_comment_list);
        row_comment_list = new Object[col_comment_list.length];
        loadCommentModel(contentID);
        tbl_comment_list.setModel(mdl_comment_list);
        tbl_comment_list.getTableHeader().setReorderingAllowed(false);
    }

    public void loadCommentModel(int contentID) {
        DefaultTableModel clearModel = (DefaultTableModel) tbl_comment_list.getModel();
        clearModel.setRowCount(0);
        for (Rating obj : Rating.getList()) {
            row_comment_list[0] = obj.getStudent().getName();
            row_comment_list[1] = obj.getComment();
            row_comment_list[2] = obj.getPoint();
            mdl_comment_list.addRow(row_comment_list);
        }
    }
}
