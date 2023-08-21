package com.patikadev.Helper;

import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Helper {

    public static void setLayout() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static int screenCenter(String axis, Dimension size) {
        int point;
        switch (axis) {
            case "x":
                point = (Toolkit.getDefaultToolkit().getScreenSize().width - size.width) / 2;
                break;
            case "y":
                point = (Toolkit.getDefaultToolkit().getScreenSize().height - size.height) / 2;
                break;
            default:
                point = 0;
        }
        return point;
    }

    public static boolean isFildEmpty(JTextField field) {
        return field.getText().trim().isEmpty();
    }

    public static boolean isFildEmpty(JTextArea field) {
        return field.getText().trim().isEmpty();
    }

    public static void showMsg(String str) {
        optionPageTR();
        String msg;
        String title;
        switch (str) {
            case "fill":
                msg = "Lütfen tüm alanları doldurunuz!";
                title = "Hata!";
                break;
            case "done":
                msg = "İşlem Başarılı!";
                title = "Sonuç";
                break;
            case "error":
                msg = "Bir hata oluştu!";
                title = "Hata";
                break;
            case "Duplicate":
                msg = "Bu kullanıcı adı daha önceden eklenmiş. Lütfen farklı bir kullanıcı adı giriniz.";
                title = "Hata";
                break;
            case "Duplicate2":
                msg = "Bu patikaya/derse zaten katıldınız.";
                title = "Hata";
                break;
            case "Duplicate Comment":
                msg = "Bu içeriği zaten değerlendirdiniz.";
                title = "Hata";
                break;
            case "Correct":
                msg = "Doğru cevap!";
                title = "Tebrikler";
                break;
            case "False":
                msg = "Yanlış cevap";
                title = "Hata";
                break;
            default:
                msg = str;
                title = "Mesaj";
        }
        JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirm (String str) {
        optionPageTR();
        String msg;
        switch (str) {
            case "sure":
                msg = "Bu işlemi gerçekleştirmek istediğine emin misiniz?";
                break;
            default:
                msg = str;
        }
        return JOptionPane.showConfirmDialog(null, msg,"Son kararın mı?", JOptionPane.YES_NO_OPTION) == 0;
    }

    public static void optionPageTR() {
        UIManager.put("OptionPane.okButtonText", "Tamam");
        UIManager.put("OptionPane.yesButtonText", "Evet");
        UIManager.put("OptionPane.noButtonText", "Hayır");
    }
    
}
