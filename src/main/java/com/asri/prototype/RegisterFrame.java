package com.asri.prototype;

import java.awt.*;
import javax.swing.*;

public class RegisterFrame extends JFrame {
    private final UserDAO userDAO = new UserDAO();

    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JFormattedTextField dateOfBirthField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleCombo;
    private JComboBox<String> genderCombo;
    private JTextField addressField;
    private JComboBox<String> securityQuestionCombo;
    private JTextField securityAnswerField;

    public RegisterFrame() {
        UIHelper.setupFrame(this, "Create Account", 1050, 720);

        JPanel wrapper = new JPanel(new BorderLayout(20, 20));
        wrapper.setBackground(UIHelper.CARD);
        wrapper.setBorder(BorderFactory.createEmptyBorder(25, 55, 25, 55));
        add(wrapper);

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(UIHelper.CARD);

        JLabel title = UIHelper.title("Create an Account", 30);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel sub = new JLabel("Join us by creating a new account");
        sub.setForeground(UIHelper.TEXT);
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        sub.setHorizontalAlignment(SwingConstants.CENTER);

        header.add(title);
        header.add(sub);
        wrapper.add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIHelper.CARD);
        wrapper.add(form, BorderLayout.CENTER);

        fullNameField = UIHelper.textField();
        emailField = UIHelper.textField();
        phoneField = UIHelper.textField();
        dateOfBirthField = UIHelper.dateField();
        passwordField = UIHelper.passwordField();
        confirmPasswordField = UIHelper.passwordField();
        roleCombo = UIHelper.combo("Customer", "Employee", "Admin");
        genderCombo = UIHelper.combo("Male", "Female", "Prefer not to say");
        addressField = UIHelper.textField();
        securityQuestionCombo = UIHelper.combo("Favorite color?", "First school?", "Best friend?");
        securityAnswerField = UIHelper.textField();

        GridBagConstraints gbc = new GridBagConstraints();
        UIHelper.addField(form, gbc, 0, 0, "Full Name", fullNameField);
        UIHelper.addField(form, gbc, 0, 1, "Email", emailField);
        UIHelper.addField(form, gbc, 1, 0, "Phone Number", phoneField);
        UIHelper.addField(form, gbc, 1, 1, "Date of Birth", dateOfBirthField);
        UIHelper.addField(form, gbc, 2, 0, "Password", passwordField);
        UIHelper.addField(form, gbc, 2, 1, "Confirm Password", confirmPasswordField);
        UIHelper.addField(form, gbc, 3, 0, "Role", roleCombo);
        UIHelper.addField(form, gbc, 3, 1, "Gender", genderCombo);
        UIHelper.addField(form, gbc, 4, 0, "Address", addressField);
        UIHelper.addField(form, gbc, 4, 1, "Security Question", securityQuestionCombo);
        UIHelper.addField(form, gbc, 5, 0, "Security Answer", securityAnswerField);

        JPanel bottom = new JPanel(new GridLayout(1, 2, 15, 0));
        bottom.setBackground(UIHelper.CARD);

        JButton back = UIHelper.grayButton("Back to Login");
        back.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        JButton create = UIHelper.button("Create an Account");
        create.addActionListener(e -> createAccount());

        bottom.add(back);
        bottom.add(create);
        wrapper.add(bottom, BorderLayout.SOUTH);
    }

    private void createAccount() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String dateOfBirth = dateOfBirthField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = roleCombo.getSelectedItem().toString();
        String gender = genderCombo.getSelectedItem().toString();
        String address = addressField.getText().trim();
        String securityQuestion = securityQuestionCombo.getSelectedItem().toString();
        String securityAnswer = securityAnswerField.getText().trim();

        StringBuilder errors = new StringBuilder();

        if (fullName.isEmpty()) errors.append("- Full Name is required.\n");
        else if (fullName.length() < 3) errors.append("- Full Name must be at least 3 characters.\n");

        if (email.isEmpty()) errors.append("- Email is required.\n");
        else if (!ValidationUtil.isValidEmail(email)) errors.append("- Email format is not valid. Example: name@email.com\n");

        if (phone.isEmpty()) errors.append("- Phone Number is required.\n");
        else if (!ValidationUtil.isValidJordanPhone(phone)) errors.append("- Phone must be a valid Jordan number. Example: 0781234567\n");

        if (dateOfBirth.contains("_")) errors.append("- Date of Birth is required. Format: dd/mm/yyyy\n");
        else if (!ValidationUtil.isValidDate(dateOfBirth)) errors.append("- Date of Birth is not valid. Example: 18/05/2003\n");

        if (password.isEmpty()) errors.append("- Password is required.\n");
        else if (password.length() < 6) errors.append("- Password must be at least 6 characters.\n");

        if (confirmPassword.isEmpty()) errors.append("- Confirm Password is required.\n");
        else if (!password.equals(confirmPassword)) errors.append("- Password and Confirm Password do not match.\n");

        if (address.isEmpty()) errors.append("- Address is required.\n");

        if (securityAnswer.isEmpty()) errors.append("- Security Answer is required.\n");
        else if (securityAnswer.length() < 2) errors.append("- Security Answer must be at least 2 characters.\n");

        if (errors.length() > 0) {
            UIHelper.showError(this, errors.toString());
            return;
        }

        try {
            if (userDAO.emailExists(email)) {
                UIHelper.showError(this, "This email already exists. Use another email or login.");
                return;
            }

            User user = new User();
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhone(phone);
            user.setDateOfBirth(dateOfBirth);
            user.setRole(role);
            user.setGender(gender);
            user.setAddress(address);
            user.setSecurityQuestion(securityQuestion);

            userDAO.register(user, password, securityAnswer);

            UIHelper.showInfo(this, "Account created successfully!\nNow login using your email and password.");
            dispose();
            new LoginFrame().setVisible(true);

        } catch (Exception ex) {
            UIHelper.showError(this, "Registration failed:\n" + ex.getMessage());
        }
    }
}
