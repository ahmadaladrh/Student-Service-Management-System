package com.asri.prototype;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static User loggedInUser;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            DBConnection.initializeDatabase();
            new LoginFrame().setVisible(true);
        });
    }
}
