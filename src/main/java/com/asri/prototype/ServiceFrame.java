package com.asri.prototype;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ServiceFrame extends JFrame {
    private final ServiceDAO serviceDAO = new ServiceDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    private JTextField idField;
    private JTextField customerIdField;
    private JTextField serviceNameField;
    private JTextField priceField;
    private JComboBox<String> statusCombo;
    private JComboBox<String> priorityCombo;
    private JFormattedTextField startDateField;
    private JFormattedTextField endDateField;
    private JTextArea descriptionArea;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;

    public ServiceFrame() {
        UIHelper.setupFrame(this, "Manage Services", 1150, 720);

        JPanel main = new JPanel(new BorderLayout(20, 20));
        main.setBackground(UIHelper.BG);
        main.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        add(main);

        JLabel title = UIHelper.title("Service / Order Management", 30);
        main.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIHelper.CARD);
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        idField = UIHelper.textField();
        idField.setEditable(false);
        customerIdField = UIHelper.textField();
        serviceNameField = UIHelper.textField();
        priceField = UIHelper.textField();
        statusCombo = UIHelper.combo("New", "In Progress", "Completed", "Canceled");
        priorityCombo = UIHelper.combo("Low", "Medium", "High");
        startDateField = UIHelper.dateField();
        endDateField = UIHelper.dateField();
        descriptionArea = UIHelper.textArea();

        GridBagConstraints gbc = new GridBagConstraints();
        UIHelper.addField(form, gbc, 0, 0, "Service ID", idField);
        UIHelper.addField(form, gbc, 0, 1, "Customer ID", customerIdField);
        UIHelper.addField(form, gbc, 1, 0, "Service Name", serviceNameField);
        UIHelper.addField(form, gbc, 1, 1, "Price", priceField);
        UIHelper.addField(form, gbc, 2, 0, "Status", statusCombo);
        UIHelper.addField(form, gbc, 2, 1, "Priority", priorityCombo);
        UIHelper.addField(form, gbc, 3, 0, "Start Date", startDateField);
        UIHelper.addField(form, gbc, 3, 1, "End Date", endDateField);

        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(UIHelper.label("Description"), gbc);

        gbc.gridy = 9;
        form.add(new JScrollPane(descriptionArea), gbc);

        JPanel buttons = new JPanel(new GridLayout(2, 3, 10, 10));
        buttons.setBackground(UIHelper.CARD);

        JButton addBtn = UIHelper.button("Add");
        JButton updateBtn = UIHelper.button("Update");
        JButton deleteBtn = UIHelper.dangerButton("Delete");
        JButton clearBtn = UIHelper.grayButton("Clear");
        JButton refreshBtn = UIHelper.grayButton("Refresh");
        JButton backBtn = UIHelper.grayButton("Back");

        addBtn.addActionListener(e -> addService());
        updateBtn.addActionListener(e -> updateService());
        deleteBtn.addActionListener(e -> deleteService());
        clearBtn.addActionListener(e -> clearForm());
        refreshBtn.addActionListener(e -> loadServices());
        backBtn.addActionListener(e -> dispose());

        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);
        buttons.add(refreshBtn);
        buttons.add(backBtn);

        gbc.gridy = 10;
        form.add(buttons, gbc);

        main.add(form, BorderLayout.WEST);

        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(UIHelper.BG);

        JPanel searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBackground(UIHelper.BG);
        searchField = UIHelper.textField();
        JButton searchBtn = UIHelper.button("Search");
        searchBtn.addActionListener(e -> searchServices());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchBtn, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{
                "ID", "Customer ID", "Customer", "Service", "Price", "Status",
                "Priority", "Start Date", "End Date", "Description"
        }, 0) {
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

        loadServices();
    }

    private void addService() {
        String errors = validateService(false);
        if (!errors.isEmpty()) {
            UIHelper.showError(this, errors);
            return;
        }

        try {
            serviceDAO.add(readServiceFromForm(false));
            UIHelper.showInfo(this, "Service order added successfully.");
            clearForm();
            loadServices();
        } catch (Exception ex) {
            UIHelper.showError(this, "Add failed:\n" + ex.getMessage());
        }
    }

    private void updateService() {
        String errors = validateService(true);
        if (!errors.isEmpty()) {
            UIHelper.showError(this, errors);
            return;
        }

        try {
            serviceDAO.update(readServiceFromForm(true));
            UIHelper.showInfo(this, "Service order updated successfully.");
            clearForm();
            loadServices();
        } catch (Exception ex) {
            UIHelper.showError(this, "Update failed:\n" + ex.getMessage());
        }
    }

    private void deleteService() {
        if (idField.getText().trim().isEmpty()) {
            UIHelper.showError(this, "Select a service order from the table first.");
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this service order?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (result != JOptionPane.YES_OPTION) return;

        try {
            serviceDAO.delete(Integer.parseInt(idField.getText().trim()));
            UIHelper.showInfo(this, "Service order deleted successfully.");
            clearForm();
            loadServices();
        } catch (Exception ex) {
            UIHelper.showError(this, "Delete failed:\n" + ex.getMessage());
        }
    }

    private void loadServices() {
        try {
            fillTable(serviceDAO.getAll());
        } catch (Exception ex) {
            UIHelper.showError(this, "Load services failed:\n" + ex.getMessage());
        }
    }

    private void searchServices() {
        try {
            String keyword = searchField.getText().trim();
            if (keyword.isEmpty()) {
                loadServices();
            } else {
                fillTable(serviceDAO.search(keyword));
            }
        } catch (Exception ex) {
            UIHelper.showError(this, "Search failed:\n" + ex.getMessage());
        }
    }

    private void fillTable(List<ServiceOrder> orders) {
        model.setRowCount(0);

        for (ServiceOrder o : orders) {
            model.addRow(new Object[]{
                    o.getId(),
                    o.getCustomerId(),
                    o.getCustomerName(),
                    o.getServiceName(),
                    o.getPrice(),
                    o.getStatus(),
                    o.getPriority(),
                    o.getStartDate(),
                    o.getEndDate(),
                    o.getDescription()
            });
        }
    }

    private ServiceOrder readServiceFromForm(boolean includeId) {
        ServiceOrder order = new ServiceOrder();
        if (includeId) order.setId(Integer.parseInt(idField.getText().trim()));
        order.setCustomerId(Integer.parseInt(customerIdField.getText().trim()));
        order.setServiceName(serviceNameField.getText().trim());
        order.setPrice(Double.parseDouble(priceField.getText().trim()));
        order.setStatus(statusCombo.getSelectedItem().toString());
        order.setPriority(priorityCombo.getSelectedItem().toString());
        order.setStartDate(startDateField.getText().trim());
        order.setEndDate(endDateField.getText().trim());
        order.setDescription(descriptionArea.getText().trim());
        return order;
    }

    private String validateService(boolean requireId) {
        StringBuilder errors = new StringBuilder();

        if (requireId && idField.getText().trim().isEmpty()) {
            errors.append("- Select a service order from the table first.\n");
        }

        String customerIdText = customerIdField.getText().trim();

        if (customerIdText.isEmpty()) {
            errors.append("- Customer ID is required.\n");
        } else {
            try {
                int customerId = Integer.parseInt(customerIdText);
                if (!customerDAO.exists(customerId)) {
                    errors.append("- Customer ID does not exist. Add the customer first.\n");
                }
            } catch (Exception ex) {
                errors.append("- Customer ID must be a number.\n");
            }
        }

        if (serviceNameField.getText().trim().isEmpty()) {
            errors.append("- Service Name is required.\n");
        } else if (serviceNameField.getText().trim().length() < 3) {
            errors.append("- Service Name must be at least 3 characters.\n");
        }

        if (priceField.getText().trim().isEmpty()) {
            errors.append("- Price is required.\n");
        } else if (!ValidationUtil.isPositiveNumber(priceField.getText().trim())) {
            errors.append("- Price must be a positive number.\n");
        }

        String startDate = startDateField.getText().trim();
        String endDate = endDateField.getText().trim();

        if (startDate.contains("_")) {
            errors.append("- Start Date is required. Format: dd/mm/yyyy\n");
        } else if (!ValidationUtil.isValidDate(startDate)) {
            errors.append("- Start Date is not valid. Example: 23/05/2026\n");
        }

        if (endDate.contains("_")) {
            errors.append("- End Date is required. Format: dd/mm/yyyy\n");
        } else if (!ValidationUtil.isValidDate(endDate)) {
            errors.append("- End Date is not valid. Example: 25/05/2026\n");
        }

        if (!startDate.contains("_") && !endDate.contains("_")
                && ValidationUtil.isValidDate(startDate)
                && ValidationUtil.isValidDate(endDate)
                && !ValidationUtil.isEndDateAfterOrEqualStart(startDate, endDate)) {
            errors.append("- End Date must be after or equal Start Date.\n");
        }

        return errors.toString();
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        idField.setText(model.getValueAt(row, 0).toString());
        customerIdField.setText(model.getValueAt(row, 1).toString());
        serviceNameField.setText(model.getValueAt(row, 3).toString());
        priceField.setText(model.getValueAt(row, 4).toString());
        statusCombo.setSelectedItem(model.getValueAt(row, 5).toString());
        priorityCombo.setSelectedItem(model.getValueAt(row, 6).toString());
        startDateField.setText(model.getValueAt(row, 7).toString());
        endDateField.setText(model.getValueAt(row, 8).toString());
        descriptionArea.setText(model.getValueAt(row, 9) == null ? "" : model.getValueAt(row, 9).toString());
    }

    private void clearForm() {
        idField.setText("");
        customerIdField.setText("");
        serviceNameField.setText("");
        priceField.setText("");
        statusCombo.setSelectedIndex(0);
        priorityCombo.setSelectedIndex(0);
        startDateField.setValue(null);
        endDateField.setValue(null);
        descriptionArea.setText("");
        table.clearSelection();
    }
}
