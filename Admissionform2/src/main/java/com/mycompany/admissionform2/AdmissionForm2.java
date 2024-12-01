package com.mycompany.admissionform2;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.toedter.calendar.JDateChooser;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdmissionForm2 {

    private static ArrayList<String[]> responses = new ArrayList<>();
    private static DefaultTableModel model;

    public static void main(String[] args) {
        JFrame frame = new JFrame("University Admission Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Admission Form", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        frame.add(titleLabel, BorderLayout.NORTH);

        // Create a split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Create the left panel (Admission Form)
        JPanel formPanel = createFormPanel();
        splitPane.setLeftComponent(formPanel);

        // Create the right panel (Responses Table)
        JPanel tablePanel = createTablePanel();
        splitPane.setRightComponent(tablePanel);

        // Set divider location and make sure the split pane is visible
        splitPane.setDividerLocation(400);
        frame.add(splitPane, BorderLayout.CENTER);

        frame.setVisible(true);

        // Load previously saved responses
        responses = Student.loadData();
        for (String[] response : responses) {
            model.addRow(response);
        }
    }

    private static JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 2));
        formPanel.setBackground(new Color(202, 241, 247));

        // Form fields (same as before)
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();

        JLabel fatherNameLabel = new JLabel("Father Name:");
        JTextField fatherNameField = new JTextField();

        JLabel motherNameLabel = new JLabel("Mother Name:");
        JTextField motherNameField = new JTextField();

        JLabel dobLabel = new JLabel("Date of Birth:");
        JDateChooser dobChooser = new JDateChooser();
        dobChooser.setDateFormatString("dd/MM/yyyy");
        dobChooser.setBorder(new LineBorder(Color.GRAY, 1));

        JLabel genderLabel = new JLabel("Gender:");
        JRadioButton maleRadio = new JRadioButton("Male");
        JRadioButton femaleRadio = new JRadioButton("Female");
        JRadioButton otherRadio = new JRadioButton("Other");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadio);
        genderGroup.add(femaleRadio);
        genderGroup.add(otherRadio);

        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        JPanel bloodGroupPanel = new JPanel(new GridLayout(2, 4));
        String[] bloodGroups = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ButtonGroup bloodGroupGroup = new ButtonGroup();
        for (String group : bloodGroups) {
            JRadioButton button = new JRadioButton(group);
            bloodGroupGroup.add(button);
            bloodGroupPanel.add(button);
        }
        bloodGroupPanel.setBackground(new Color(181, 203, 206));

        JLabel presentAddressLabel = new JLabel("Present Address:");
        JTextArea presentAddressField = new JTextArea(3, 20);
        presentAddressField.setBorder(new LineBorder(Color.GRAY, 1));

        JLabel permanentAddressLabel = new JLabel("Permanent Address:");
        JTextArea permanentAddressField = new JTextArea(3, 20);
        permanentAddressField.setBorder(new LineBorder(Color.GRAY, 1));

        JRadioButton sameAsPresentRadio = new JRadioButton("Same as Present Address");
        sameAsPresentRadio.addActionListener(e -> {
            if (sameAsPresentRadio.isSelected()) {
                permanentAddressField.setText(presentAddressField.getText());
            } else {
                permanentAddressField.setText("");
            }
        });

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField();

        JLabel hscYearLabel = new JLabel("HSC Passing Year:");
        JComboBox<String> hscYearBox = new JComboBox<>();
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        for (int i = 0; i < 5; i++) {
            hscYearBox.addItem(String.valueOf(currentYear - i));
        }

        JLabel hscGradeLabel = new JLabel("HSC Grade:");
        JTextField hscGradeField = new JTextField();

        JLabel sscYearLabel = new JLabel("SSC Passing Year:");
        JComboBox<String> sscYearBox = new JComboBox<>();
        sscYearBox.addItem("Select a year");

        JLabel sscGradeLabel = new JLabel("SSC Grade:");
        JTextField sscGradeField = new JTextField();

        hscYearBox.addActionListener(e -> {
            sscYearBox.removeAllItems();
            sscYearBox.addItem("Select a year");
            String selectedHscYear = (String) hscYearBox.getSelectedItem();
            if (!"Select a year".equals(selectedHscYear)) {
                int hscYear = Integer.parseInt(selectedHscYear);
                for (int i = 2; i <= 7; i++) {
                    sscYearBox.addItem(String.valueOf(hscYear - i));
                }
            }
        });

        // Course Preference
        JLabel coursesLabel = new JLabel("Course Preference:");
        JComboBox<String> courseBox = new JComboBox<>();
        courseBox.addItem("Select a Course");
        String[] courses = {"CSE", "SWE", "English", "EEE", "LLB", "Business", "Architecture", "Economy"};
        for (String course : courses) {
            courseBox.addItem(course);
        }

        JButton submitButton = new JButton("Submit");
        JButton resetButton = new JButton("Reset");

        // Adding components to form panel
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(fatherNameLabel);
        formPanel.add(fatherNameField);
        formPanel.add(motherNameLabel);
        formPanel.add(motherNameField);
        formPanel.add(dobLabel);
        formPanel.add(dobChooser);
        formPanel.add(genderLabel);
        formPanel.add(new JPanel() {
            {
                add(maleRadio);
                add(femaleRadio);
                add(otherRadio);
            }
        });
        formPanel.add(bloodGroupLabel);
        formPanel.add(bloodGroupPanel);
        formPanel.add(presentAddressLabel);
        formPanel.add(presentAddressField);
        formPanel.add(new JLabel()); // Empty label to provide space
        formPanel.add(sameAsPresentRadio);
        formPanel.add(permanentAddressLabel);
        formPanel.add(permanentAddressField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(hscYearLabel);
        formPanel.add(hscYearBox);
        formPanel.add(hscGradeLabel);
        formPanel.add(hscGradeField);
        formPanel.add(sscYearLabel);
        formPanel.add(sscYearBox);
        formPanel.add(sscGradeLabel);
        formPanel.add(sscGradeField);
        formPanel.add(coursesLabel);
        formPanel.add(courseBox);
        formPanel.add(submitButton);
        formPanel.add(resetButton);

        // Submit button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String fatherName = fatherNameField.getText();
                String motherName = motherNameField.getText();
                String dob = ((JTextField) dobChooser.getDateEditor().getUiComponent()).getText();
                String gender = maleRadio.isSelected() ? "Male" : femaleRadio.isSelected() ? "Female" : "Other";
                String bloodGroup = null;
                for (Component c : bloodGroupPanel.getComponents()) {
                    if (c instanceof JRadioButton && ((JRadioButton) c).isSelected()) {
                        bloodGroup = ((JRadioButton) c).getText();
                        break;
                    }
                }
                String presentAddress = presentAddressField.getText();
                String permanentAddress = permanentAddressField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String hscYear = (String) hscYearBox.getSelectedItem();
                String hscGrade = hscGradeField.getText();
                String sscYear = (String) sscYearBox.getSelectedItem();
                String sscGrade = sscGradeField.getText();
                String course = (String) courseBox.getSelectedItem();

                // Validate empty fields and format errors
                StringBuilder errorMessage = new StringBuilder();

                // Check for empty fields
                if (name.isEmpty() || fatherName.isEmpty() || motherName.isEmpty() || dob.isEmpty() || gender.isEmpty()
                        || bloodGroup == null || presentAddress.isEmpty() || permanentAddress.isEmpty() || email.isEmpty()
                        || phone.isEmpty() || hscYear.isEmpty() || sscYear.isEmpty() || hscGrade.isEmpty() || sscGrade.isEmpty() || course.equals("Select a Course")) {
                    errorMessage.append("Please fill all fields.\n");
                }

// Validate individual fields only if they are not empty
                if (!name.isEmpty() && !name.matches("[a-zA-Z\\s]+")) {
                    errorMessage.append("Name should only contain letters.\n");
                }

                if (!fatherName.isEmpty() && !fatherName.matches("[a-zA-Z\\s]+")) {
                    errorMessage.append("Father's Name should only contain letters.\n");
                }

                if (!motherName.isEmpty() && !motherName.matches("[a-zA-Z\\s]+")) {
                    errorMessage.append("Mother's Name should only contain letters.\n");
                }

                if (!email.isEmpty() && !isValidEmail(email)) {
                    errorMessage.append("Invalid email format.\n");
                }

                if (!phone.isEmpty() && !phone.matches("[0-9]+")) {
                    errorMessage.append("Phone should contain only numbers.\n");
                }

                try {
                    if (!hscGrade.isEmpty()) {
                        Float.parseFloat(hscGrade);
                    }
                } catch (NumberFormatException ex) {
                    errorMessage.append("HSC Grade must be a float value.\n");
                }

                try {
                    if (!sscGrade.isEmpty()) {
                        Float.parseFloat(sscGrade);
                    }
                } catch (NumberFormatException ex) {
                    errorMessage.append("SSC Grade must be a float value.\n");
                }

                if (errorMessage.length() > 0) {
                    Component frame = null;
                    JOptionPane.showMessageDialog(frame, errorMessage.toString(), "Form Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String[] rowData = {name, fatherName, motherName, dob, gender, bloodGroup, presentAddress, permanentAddress, email, phone, hscYear, hscGrade, sscYear, sscGrade, course};
                    model.addRow(rowData);
                    resetForm(formPanel);
                }
            }
        });

        // Reset button action
        resetButton.addActionListener(e -> resetForm(formPanel));

        return formPanel;
    }

    private static JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        // Table model
        model = new DefaultTableModel(new String[]{"Name", "Father's Name", "Mother's Name", "Date of Birth", "Gender", "Blood Group", "Present Address", "Permanent Address", "Email", "Phone", "HSC Year", "HSC Grade", "SSC Year", "SSC Grade", "Course"}, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons for Save, Update, Delete
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        saveButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String[] rowData = getRowDataFromTable(table, selectedRow);
                Student.saveData(rowData);
            } else {
                JOptionPane.showMessageDialog(tablePanel, "Select a row to save", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Allow user to update selected row data
                // Implementation would open a dialog with the selected data pre-filled
                // After user edits and clicks save, update the table and save data
                // Implementation can be similar to the save action
            } else {
                JOptionPane.showMessageDialog(tablePanel, "Select a row to update", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    // Delete the selected row
                    model.removeRow(selectedRow);
                    Student.deleteData(selectedRow);  // Deleting from serialized data
                } else {
                    JOptionPane.showMessageDialog(tablePanel, "Select a row to delete", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private static String[] getRowDataFromTable(JTable table, int row) {
        String[] rowData = new String[model.getColumnCount()];
        for (int i = 0; i < model.getColumnCount(); i++) {
            rowData[i] = table.getValueAt(row, i).toString();
        }
        return rowData;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static void resetForm(JPanel formPanel) {
        for (Component component : formPanel.getComponents()) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JTextArea) {
                ((JTextArea) component).setText("");
            } else if (component instanceof JRadioButton) {
                ((JRadioButton) component).setSelected(false);
            } else if (component instanceof JComboBox) {
                ((JComboBox<?>) component).setSelectedIndex(0);
            }
        }
    }
}
