package com.asri.prototype;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CustomerFrame extends JFrame {
    private final CustomerDAO customerDAO = new CustomerDAO();

    private JTextField idField;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextField addressField;
    private JComboBox<String> typeCombo;
    private JTextArea notesArea;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public CustomerFrame() {
        UIHelper.setupFrame(this, "Manage Customers", 1100, 680);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(UIHelper.BG);
        main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(main);

        JLabel title = UIHelper.title("Customer Management", 30);
        main.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIHelper.CARD);
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        idField = UIHelper.textField();
        idField.setEditable(false);
        nameField = UIHelper.textField();
        phoneField = UIHelper.textField();
        emailField = UIHelper.textField();
        addressField = UIHelper.textField();
        typeCombo = UIHelper.combo("Normal", "VIP", "Blocked");
        notesArea = UIHelper.textArea();

        GridBagConstraints gbc = new GridBagConstraints();
        UIHelper.addField(form, gbc, 0, 0, "Customer ID", idField);
        UIHelper.addField(form, gbc, 0, 1, "Customer Name", nameField);
        UIHelper.addField(form, gbc, 1, 0, "Phone", phoneField);
        UIHelper.addField(form, gbc, 1, 1, "Email", emailField);
        UIHelper.addField(form, gbc, 2, 0, "Address", addressField);
        UIHelper.addField(form, gbc, 2, 1, "Customer Type", typeCombo);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(UIHelper.label("Notes"), gbc);

        gbc.gridy = 7;
        form.add(new JScrollPane(notesArea), gbc);

        JPanel buttons = new JPanel(new GridLayout(2, 3, 10, 10));
        buttons.setBackground(UIHelper.CARD);

        JButton addBtn = UIHelper.button("Add");
        JButton updateBtn = UIHelper.button("Update");
        JButton deleteBtn = UIHelper.dangerButton("Delete");
        JButton clearBtn = UIHelper.grayButton("Clear");
        JButton refreshBtn = UIHelper.grayButton("Refresh");
        JButton backBtn = UIHelper.grayButton("Back");

        addBtn.addActionListener(e -> addCustomer());
        updateBtn.addActionListener(e -> updateCustomer());
        deleteBtn.addActionListener(e -> deleteCustomer());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> loadCustomers());
        backBtn.addActionListener(e -> dispose());

        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);
        buttons.add(refreshBtn);
        buttons.add(backBtn);

        gbc.gridy = 8;
        form.add(buttons, gbc);

        main.add(form, BorderLayout.WEST);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(UIHelper.BG);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(UIHelper.BG);
        searchField = UIHelper.textField();
        JButton searchBtn = UIHelper.button("Search");
        searchBtn.addActionListener(e -> searchCustomers());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Phone", "Email", "Address", "Type", "Notes"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        UIHelper.styleTable(table);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillFormFromSelectedRow();
            }
        });

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        main.add(tablePanel, BorderLayout.CENTER);

        loadCustomers();
    }

    private void addCustomer() {
        String errors = validateCustomer(false);
        if (!errors.isEmpty()) {
            UIHelper.showError(this, errors);
            return;
        }

        try {
            customerDAO.add(readCustomerFromForm(false));
            UIHelper.showInfo(this, "Customer added successfully.");
            clearForm();
            loadCustomers();
        } catch (Exception ex) {
            UIHelper.showError(this, "Add failed:\n" + ex.getMessage());
        }
    }

    private void updateCustomer() {
        String errors = validateCustomer(true);
        if (!errors.isEmpty()) {
            UIHelper.showError(this, errors);
            return;
        }

        try {
            customerDAO.update(readCustomerFromForm(true));
            UIHelper.showInfo(this, "Customer updated successfully.");
            clearForm();
            loadCustomers();
        } catch (Exception ex) {
            UIHelper.showError(this, "Update failed:\n" + ex.getMessage());
        }
    }

    private void deleteCustomer() {
        if (idField.getText().trim().isEmpty()) {
            UIHelper.showError(this, "Select a customer from the table first.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this customer?\nServices connected to this customer should be deleted first.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (result != JOptionPane.YES_OPTION) return;

        try {
            customerDAO.delete(Integer.parseInt(idField.getText().trim()));
            UIHelper.showInfo(this, "Customer deleted successfully.");
            clearForm();
            loadCustomers();
        } catch (Exception ex) {
            UIHelper.showError(this, "Delete failed:\nThis customer may have service orders.\nDelete their service orders first.");
        }
    }

    private void loadCustomers() {
        try {
            fillTable(customerDAO.getAll());
        } catch (Exception ex) {
            UIHelper.showError(this, "Load customers failed:\n" + ex.getMessage());
        }
    }

    private void searchCustomers() {
        try {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                loadCustomers();
            } else {
                fillTable(customerDAO.search(keyword));
            }
        } catch (Exception ex) {
            UIHelper.showError(this, "Search failed:\n" + ex.getMessage());
        }
    }

    private void fillTable(List<Customer> customers) {
        model.setRowCount(0);

        for (Customer c : customers) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getPhone(),
                    c.getEmail(),
                    c.getAddress(),
                    c.getCustomerType(),
                    c.getNotes()
            });
        }
    }

    private Customer readCustomerFromForm(boolean includeId) {
        Customer customer = new Customer();
        if (includeId) customer.setId(Integer.parseInt(idField.getText().trim()));
        customer.setName(nameField.getText().trim());
        customer.setPhone(phoneField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setAddress(addressField.getText().trim());
        customer.setCustomerType(typeCombo.getSelectedItem().toString());
        customer.setNotes(notesArea.getText().trim());
        return customer;
    }

    private String validateCustomer(boolean requireId) {
        StringBuilder errors = new StringBuilder();

        if (requireId && idField.getText().trim().isEmpty()) {
            errors.append("- Select a customer from the table first.\n");
        }

        if (nameField.getText().trim().isEmpty()) {
            errors.append("- Customer Name is required.\n");
        } else if (nameField.getText().trim().length() < 3) {
            errors.append("- Customer Name must be at least 3 characters.\n");
        }

        if (phoneField.getText().trim().isEmpty()) {
            errors.append("- Phone is required.\n");
        } else if (!ValidationUtil.isValidJordanPhone(phoneField.getText().trim())) {
            errors.append("- Phone must be a valid Jordan number. Example: 0781234567\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            errors.append("- Email is required.\n");
        } else if (!ValidationUtil.isValidEmail(emailField.getText().trim())) {
            errors.append("- Email format is not valid.\n");
        }

        if (addressField.getText().trim().isEmpty()) {
            errors.append("- Address is required.\n");
        }

        return errors.toString();
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        idField.setText(model.getValueAt(row, 0).toString());
        nameField.setText(model.getValueAt(row, 1).toString());
        phoneField.setText(model.getValueAt(row, 2).toString());
        emailField.setText(model.getValueAt(row, 3).toString());
        addressField.setText(model.getValueAt(row, 4).toString());
        typeCombo.setSelectedItem(model.getValueAt(row, 5).toString());
        notesArea.setText(model.getValueAt(row, 6) == null ? "" : model.getValueAt(row, 6).toString());
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        addressField.setText("");
        typeCombo.setSelectedIndex(0);
        notesArea.setText("");
        table.clearSelection();
    }
}
