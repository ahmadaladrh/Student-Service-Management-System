package com.asri.prototype;

import java.awt.*;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;
import javax.swing.text.MaskFormatter;

public class UIHelper {
    public static final Color BG = new Color(28, 28, 28);
    public static final Color CARD = new Color(36, 36, 36);
    public static final Color TEXT = new Color(235, 235, 235);
    public static final Color FIELD = new Color(45, 45, 45);
    public static final Color BORDER = new Color(80, 80, 80);
    public static final Color PRIMARY = new Color(0, 150, 255);
    public static final Color DANGER = new Color(180, 60, 60);

    public static void setupFrame(JFrame frame, String title, int w, int h) {
        frame.setTitle(title);
        frame.setSize(w, h);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BG);
    }

    public static JLabel title(String text, int size) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, size));
        return label;
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        return label;
    }

    public static JTextField textField() {
        JTextField field = new JTextField();
        styleField(field);
        return field;
    }

    public static JFormattedTextField dateField() {
        try {
            MaskFormatter formatter = new MaskFormatter("##/##/####");
            formatter.setPlaceholderCharacter('_');
            JFormattedTextField field = new JFormattedTextField(formatter);
            styleField(field);
            field.setToolTipText("Format: dd/mm/yyyy");
            return field;
        } catch (ParseException e) {
            JFormattedTextField field = new JFormattedTextField();
            styleField(field);
            return field;
        }
    }

    public static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        styleField(field);
        return field;
    }

    public static JComboBox<String> combo(String... items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(FIELD);
        combo.setForeground(Color.WHITE);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBorder(new LineBorder(BORDER));
        combo.setOpaque(true);

        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

                JLabel label = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                label.setBackground(isSelected ? PRIMARY : FIELD);
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                return label;
            }
        });

        return combo;
    }

    public static JTextArea textArea() {
        JTextArea area = new JTextArea(4, 20);
        area.setBackground(FIELD);
        area.setForeground(TEXT);
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBorder(new EmptyBorder(8, 8, 8, 8));
        return area;
    }

    public static JButton button(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        return btn;
    }

    public static JButton grayButton(String text) {
        JButton btn = button(text);
        btn.setBackground(new Color(95, 95, 95));
        return btn;
    }

    public static JButton dangerButton(String text) {
        JButton btn = button(text);
        btn.setBackground(DANGER);
        return btn;
    }

    public static JPanel cardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD);
        panel.setBorder(new CompoundBorder(
                new LineBorder(new Color(210, 160, 60), 2),
                new EmptyBorder(25, 25, 25, 25)
        ));
        return panel;
    }

    public static void addField(JPanel panel, GridBagConstraints gbc, int row, int col, String label, JComponent field) {
        gbc.gridx = col;
        gbc.gridy = row * 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 10, 3, 10);
        panel.add(label(label), gbc);

        gbc.gridy = row * 2 + 1;
        gbc.insets = new Insets(0, 10, 12, 10);
        panel.add(field, gbc);
    }

    public static void styleTable(JTable table) {
        table.setBackground(FIELD);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setRowHeight(28);
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(65, 65, 65));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    public static void showError(Component parent, String message) {
        JTextArea area = new JTextArea(message);
        area.setEditable(false);
        area.setOpaque(false);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JOptionPane.showMessageDialog(parent, area, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void styleField(JTextField field) {
        field.setBackground(FIELD);
        field.setForeground(TEXT);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(new CompoundBorder(new LineBorder(BORDER), new EmptyBorder(8, 8, 8, 8)));
    }
}
