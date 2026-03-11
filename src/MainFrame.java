package ui;

import javax.swing.*;
import java.awt.*;

public class AppTheme {

    public static void apply() {
        try {
            // Nimbus daha modern. (Ek kütüphane yok)
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Dark-ish palette
        UIManager.put("control", new Color(30, 30, 34));
        UIManager.put("info", new Color(30, 30, 34));
        UIManager.put("nimbusBase", new Color(50, 90, 160));
        UIManager.put("nimbusBlueGrey", new Color(45, 45, 50));
        UIManager.put("text", new Color(230, 230, 230));
        UIManager.put("nimbusLightBackground", new Color(22, 22, 26));

        Font f = new Font("Segoe UI", Font.PLAIN, 14);
        setGlobalFont(f);
    }

    private static void setGlobalFont(Font f) {
        var keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof Font) UIManager.put(key, f);
        }
    }

    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(60, 120, 220));
        b.setForeground(Color.WHITE);
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return b;
    }

    public static JButton ghostButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(new Color(40, 40, 46));
        b.setForeground(new Color(230, 230, 230));
        b.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        return b;
    }

    public static JPanel card() {
        JPanel p = new JPanel();
        p.setBackground(new Color(26, 26, 30));
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(55, 55, 60), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }
}
