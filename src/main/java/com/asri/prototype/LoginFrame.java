package com.asri.prototype;

import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private final UserDAO userDAO = new UserDAO();

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox rememberMe;

    public LoginFrame() {
        UIHelper.setupFrame(this, "Login", 950, 600);

        JPanel main = new JPanel(new GridLayout(1, 2));
        main.setBackground(UIHelper.BG);
        main.setBorder(BorderFactory.createEmptyBorder(35, 35, 35, 35));
        add(main);

        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(24, 24, 24));
        left.setBorder(BorderFactory.createLineBorder(new Color(75, 75, 75)));

        JLabel logo = UIHelper.title("REAL SWING APP", 28);
        logo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel hint = new JLabel("<html><center>SQLite Database<br>Register • Login • Manage Data</center></html>");
        hint.setForeground(UIHelper.TEXT);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        hint.setHorizontalAlignment(SwingConstants.CENTER);

        left.add(logo, BorderLayout.CENTER);
        left.add(hint, BorderLayout.SOUTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIHelper.CARD);
        form.setBorder(BorderFactory.createEmptyBorder(40, 65, 40, 65));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel title = UIHelper.title("Welcome Back!", 30);
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 5, 0);
        form.add(title, gbc);

        JLabel sub = new JLabel("Please login to your account");
        sub.setForeground(UIHelper.TEXT);
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 55, 0);
        form.add(sub, gbc);

        emailField = UIHelper.textField();
        passwordField = UIHelper.passwordField();

        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 4, 0);
        form.add(UIHelper.label("Email"), gbc);
        gbc.gridy = 3; gbc.insets = new Insets(0, 0, 18, 0);
        form.add(emailField, gbc);

        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 4, 0);
        form.add(UIHelper.label("Password"), gbc);
        gbc.gridy = 5; gbc.insets = new Insets(0, 0, 15, 0);
        form.add(passwordField, gbc);

        rememberMe = new JCheckBox("Remember me");
        rememberMe.setBackground(UIHelper.CARD);
        rememberMe.setForeground(UIHelper.TEXT);
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 35, 0);
        form.add(rememberMe, gbc);

        JButton loginBtn = UIHelper.button("Login");
        loginBtn.addActionListener(e -> login());
        gbc.gridy = 7; gbc.insets = new Insets(0, 0, 25, 0);
        form.add(loginBtn, gbc);

        JButton registerBtn = UIHelper.grayButton("Don't have an account? Create one");
        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterFrame().setVisible(true);
        });
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 0, 0);
        form.add(registerBtn, gbc);

        main.add(left);
        main.add(form);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        StringBuilder errors = new StringBuilder();

        if (email.isEmpty()) {
            errors.append("- Email is required.\n");
        } else if (!ValidationUtil.isValidEmail(email)) {
            errors.append("- Email format is not valid.\n");
        }

        if (password.isEmpty()) {
            errors.append("- Password is required.\n");
        }

        if (errors.length() > 0) {
            UIHelper.showError(this, errors.toString());
            return;
        }

        try {
            User user = userDAO.login(email, password);

            if (user == null) {
                UIHelper.showError(this, "Email or password is wrong.");
                return;
            }

            Main.loggedInUser = user;
            dispose();
            new DashboardFrame().setVisible(true);

        } catch (Exception ex) {
            UIHelper.showError(this, "Login failed:\n" + ex.getMessage());
        }
    }
}
