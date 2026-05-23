package com.asri.prototype;

import java.awt.*;
import javax.swing.*;

public class DashboardFrame extends JFrame {
    public DashboardFrame() {
        UIHelper.setupFrame(this, "Dashboard", 1000, 620);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(UIHelper.BG);
        main.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(main);

        String name = Main.loggedInUser != null ? Main.loggedInUser.getFullName() : "User";
        JLabel title = UIHelper.title("Home Dashboard - Welcome " + name, 28);
        main.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(new GridLayout(2, 2, 20, 20));
        cards.setBackground(UIHelper.BG);
        main.add(cards, BorderLayout.CENTER);

        cards.add(menuCard("Manage Customers", "Add, update, delete, search, and display customers", () -> new CustomerFrame().setVisible(true)));
        cards.add(menuCard("Manage Services", "Add, update, delete, search, and display service orders", () -> new ServiceFrame().setVisible(true)));
        cards.add(menuCard("Reports", "Prototype report area for screenshots", () -> JOptionPane.showMessageDialog(this, "Reports page can be added later.")));
        cards.add(menuCard("Settings", "Logged user: " + (Main.loggedInUser != null ? Main.loggedInUser.getEmail() : ""), () -> JOptionPane.showMessageDialog(this, "Settings page can be added later.")));

        JButton logout = UIHelper.grayButton("Logout");
        logout.addActionListener(e -> {
            Main.loggedInUser = null;
            dispose();
            new LoginFrame().setVisible(true);
        });
        main.add(logout, BorderLayout.SOUTH);
    }

    private JPanel menuCard(String titleText, String description, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(UIHelper.CARD);
        card.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel title = UIHelper.title(titleText, 22);
        JLabel desc = new JLabel("<html>" + description + "</html>");
        desc.setForeground(UIHelper.TEXT);
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JButton open = UIHelper.button("Open");
        open.addActionListener(e -> action.run());

        card.add(title, BorderLayout.NORTH);
        card.add(desc, BorderLayout.CENTER);
        card.add(open, BorderLayout.SOUTH);

        return card;
    }
}
