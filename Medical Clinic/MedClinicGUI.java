import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class MedClinicGUI extends JFrame {
    // Data structures
    private CustomLinkedList<Patient> patientList = new CustomLinkedList<>();
    private CustomLinkedList<Doctor> doctorList = new CustomLinkedList<>();
    private CustomLinkedList<Consultation> consultationList = new CustomLinkedList<>();

    // Color scheme - Modern Blue Theme
    private static final Color PRIMARY_COLOR = new Color(79, 110, 242);
    private static final Color SECONDARY_COLOR = new Color(46, 196, 183);
    private static final Color DANGER_COLOR = new Color(201, 45, 78);
    private static final Color WARNING_COLOR = new Color(243, 156, 17);
    private static final Color BG_DARK = new Color(20, 25, 38);
    private static final Color BG_CARD = new Color(30, 36, 56);
    private static final Color TEXT_LIGHT = new Color(236, 239, 241);
    private static final Color TEXT_MUTED = new Color(155, 164, 181);
    private static final Color BORDER_COLOR = new Color(48, 56, 86);

    // UI Components
    private JTabbedPane tabbedPane;
    private DefaultTableModel patientTableModel, doctorTableModel, consultationTableModel;
    private JTable patientTable, doctorTable, consultationTable;
    private JLabel patientCountLabel, doctorCountLabel, consultationCountLabel;

    public MedClinicGUI() {
        setTitle("MediClinic - Hospital Management System");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(BG_DARK);
        setLookAndFeel();

        // Create main layout
        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        
        setVisible(true);
    }

    private void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_DARK);
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Title section
        JLabel titleLabel = new JLabel("🏥 MediClinic Hospital Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_COLOR);

        // Stats section
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BG_DARK);
        statsPanel.setOpaque(false);

        patientCountLabel = createStatCard("👥 Patients", "0");
        doctorCountLabel = createStatCard("👨‍⚕️ Doctors", "0");
        consultationCountLabel = createStatCard("📋 Consultations", "0");

        statsPanel.add(patientCountLabel);
        statsPanel.add(doctorCountLabel);
        statsPanel.add(consultationCountLabel);

        header.add(titleLabel, BorderLayout.WEST);
        header.add(statsPanel, BorderLayout.EAST);

        return header;
    }

    private JLabel createStatCard(String title, String count) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_CARD);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        panel.setMaximumSize(new Dimension(200, 80));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_MUTED);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        countLabel.setForeground(PRIMARY_COLOR);
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(countLabel);

        return new JLabel(panel.getComponentCount() > 0 ? title : count);
    }

    private JPanel createMainContent() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);

        tabbedPane = new JTabbedPane();
        tabbedPane.setUI(new CustomTabbedPaneUI());
        tabbedPane.setBackground(BG_DARK);
        tabbedPane.setForeground(TEXT_LIGHT);

        // Add tabs
        tabbedPane.addTab("📊 Dashboard", createDashboard());
        tabbedPane.addTab("👥 Register Patient", createPatientRegistration());
        tabbedPane.addTab("👨‍⚕️ Register Doctor", createDoctorRegistration());
        tabbedPane.addTab("📋 Record Consultation", createConsultationRecording());
        tabbedPane.addTab("🔍 Search", createSearchPanel());
        tabbedPane.addTab("📑 Manage Data", createManageDataPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createDashboard() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 20, 20));
        panel.setBackground(BG_DARK);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(createDashboardCard("Patient Management", 
            "Register and manage patient records", PRIMARY_COLOR));
        panel.add(createDashboardCard("Doctor Management", 
            "Register and manage doctor profiles", SECONDARY_COLOR));
        panel.add(createDashboardCard("Consultation Records", 
            "Record patient consultations", WARNING_COLOR));
        panel.add(createDashboardCard("Search & Reports", 
            "Search and generate reports", PRIMARY_COLOR));

        return panel;
    }

    private JPanel createDashboardCard(String title, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(color);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_MUTED);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(descLabel);

        return card;
    }

    private JPanel createPatientRegistration() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(BG_CARD);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Patient ID
        addFormField(formPanel, gbc, "Patient ID:", 0);
        JTextField pIdField = createFormField();
        addComponent(formPanel, pIdField, gbc, 1, 0);

        // Name
        addFormField(formPanel, gbc, "Full Name:", 2);
        JTextField pNameField = createFormField();
        addComponent(formPanel, pNameField, gbc, 3, 0);

        // Age
        addFormField(formPanel, gbc, "Age:", 4);
        JTextField pAgeField = createFormField();
        addComponent(formPanel, pAgeField, gbc, 5, 0);

        // Gender
        addFormField(formPanel, gbc, "Gender:", 6);
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        styleComboBox(genderCombo);
        addComponent(formPanel, genderCombo, gbc, 7, 0);

        // Contact
        addFormField(formPanel, gbc, "Contact Number:", 8);
        JTextField pContactField = createFormField();
        addComponent(formPanel, pContactField, gbc, 9, 0);

        // Address
        addFormField(formPanel, gbc, "Address:", 10);
        JTextField pAddressField = createFormField();
        addComponent(formPanel, pAddressField, gbc, 11, 0);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BG_CARD);

        JButton addBtn = createButton("Add Patient", PRIMARY_COLOR);
        JButton updateBtn = createButton("Update Patient", SECONDARY_COLOR);
        JButton clearBtn = createButton("Clear", TEXT_MUTED);

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        patientTableModel = new DefaultTableModel(
            new String[]{"Patient ID", "Name", "Age", "Gender", "Contact", "Address"}, 0
        );
        patientTable = createStyledTable(patientTableModel);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.getViewport().setBackground(BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(BG_DARK);
        
        JButton deleteBtn = createButton("Delete Selected", DANGER_COLOR);
        deleteBtn.addActionListener(e -> deletePatient());
        actionPanel.add(deleteBtn);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        // Add button actions
        addBtn.addActionListener(e -> addPatient(pIdField, pNameField, pAgeField, genderCombo, pContactField, pAddressField));
        clearBtn.addActionListener(e -> clearPatientForm(pIdField, pNameField, pAgeField, genderCombo, pContactField, pAddressField));

        return mainPanel;
    }

    private JPanel createDoctorRegistration() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(BG_CARD);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Doctor ID
        addFormField(formPanel, gbc, "Doctor ID:", 0);
        JTextField dIdField = createFormField();
        addComponent(formPanel, dIdField, gbc, 1, 0);

        // Name
        addFormField(formPanel, gbc, "Full Name:", 2);
        JTextField dNameField = createFormField();
        addComponent(formPanel, dNameField, gbc, 3, 0);

        // Specialization
        addFormField(formPanel, gbc, "Specialization:", 4);
        JTextField dSpecField = createFormField();
        addComponent(formPanel, dSpecField, gbc, 5, 0);

        // Contact
        addFormField(formPanel, gbc, "Contact Number:", 6);
        JTextField dContactField = createFormField();
        addComponent(formPanel, dContactField, gbc, 7, 0);

        // Email
        addFormField(formPanel, gbc, "Email:", 8);
        JTextField dEmailField = createFormField();
        addComponent(formPanel, dEmailField, gbc, 9, 0);

        // Room Number
        addFormField(formPanel, gbc, "Room Number:", 10);
        JTextField dRoomField = createFormField();
        addComponent(formPanel, dRoomField, gbc, 11, 0);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BG_CARD);

        JButton addBtn = createButton("Add Doctor", PRIMARY_COLOR);
        JButton updateBtn = createButton("Update Doctor", SECONDARY_COLOR);
        JButton clearBtn = createButton("Clear", TEXT_MUTED);

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        doctorTableModel = new DefaultTableModel(
            new String[]{"Doctor ID", "Name", "Specialization", "Contact", "Email", "Room"}, 0
        );
        doctorTable = createStyledTable(doctorTableModel);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.getViewport().setBackground(BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setBackground(BG_DARK);
        
        JButton deleteBtn = createButton("Delete Selected", DANGER_COLOR);
        deleteBtn.addActionListener(e -> deleteDoctor());
        actionPanel.add(deleteBtn);

        mainPanel.add(actionPanel, BorderLayout.SOUTH);

        // Add button actions
        addBtn.addActionListener(e -> addDoctor(dIdField, dNameField, dSpecField, dContactField, dEmailField, dRoomField));
        clearBtn.addActionListener(e -> clearDoctorForm(dIdField, dNameField, dSpecField, dContactField, dEmailField, dRoomField));

        return mainPanel;
    }

    private JPanel createConsultationRecording() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(BG_CARD);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Consultation ID
        addFormField(formPanel, gbc, "Consultation ID:", 0);
        JTextField cIdField = createFormField();
        addComponent(formPanel, cIdField, gbc, 1, 0);

        // Patient Selection
        addFormField(formPanel, gbc, "Patient:", 2);
        JComboBox<String> patientCombo = new JComboBox<>();
        styleComboBox(patientCombo);
        addComponent(formPanel, patientCombo, gbc, 3, 0);

        // Doctor Selection
        addFormField(formPanel, gbc, "Doctor:", 4);
        JComboBox<String> doctorCombo = new JComboBox<>();
        styleComboBox(doctorCombo);
        addComponent(formPanel, doctorCombo, gbc, 5, 0);

        // Date
        addFormField(formPanel, gbc, "Date:", 6);
        JTextField cDateField = createFormField();
        addComponent(formPanel, cDateField, gbc, 7, 0);

        // Time
        addFormField(formPanel, gbc, "Time:", 8);
        JTextField cTimeField = createFormField();
        addComponent(formPanel, cTimeField, gbc, 9, 0);

        // Symptoms
        addFormField(formPanel, gbc, "Symptoms:", 10);
        JTextField cSymptomsField = createFormField();
        addComponent(formPanel, cSymptomsField, gbc, 11, 0);

        // Diagnosis
        addFormField(formPanel, gbc, "Diagnosis:", 12);
        JTextField cDiagnosisField = createFormField();
        addComponent(formPanel, cDiagnosisField, gbc, 13, 0);

        // Treatment
        addFormField(formPanel, gbc, "Treatment:", 14);
        JTextField cTreatmentField = createFormField();
        addComponent(formPanel, cTreatmentField, gbc, 15, 0);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BG_CARD);

        JButton addBtn = createButton("Record Consultation", PRIMARY_COLOR);
        JButton clearBtn = createButton("Clear", TEXT_MUTED);

        buttonPanel.add(addBtn);
        buttonPanel.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        mainPanel.add(formPanel, BorderLayout.NORTH);

        // Table Panel
        consultationTableModel = new DefaultTableModel(
            new String[]{"Consultation ID", "Patient", "Doctor", "Date", "Time", "Symptoms", "Diagnosis", "Treatment"}, 0
        );
        consultationTable = createStyledTable(consultationTableModel);
        JScrollPane scrollPane = new JScrollPane(consultationTable);
        scrollPane.getViewport().setBackground(BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Add button actions
        addBtn.addActionListener(e -> addConsultation(cIdField, patientCombo, doctorCombo, cDateField, cTimeField, cSymptomsField, cDiagnosisField, cTreatmentField));
        clearBtn.addActionListener(e -> clearConsultationForm(cIdField, patientCombo, doctorCombo, cDateField, cTimeField, cSymptomsField, cDiagnosisField, cTreatmentField));

        return mainPanel;
    }

    private JPanel createSearchPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Search controls
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BG_CARD);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JLabel typeLabel = new JLabel("Search Type:");
        typeLabel.setForeground(TEXT_LIGHT);

        JComboBox<String> searchTypeCombo = new JComboBox<>(new String[]{
            "Patient by ID", "Patient by Name", "Doctor by ID", "Doctor by Name", "Consultation by ID"
        });
        styleComboBox(searchTypeCombo);

        JLabel keywordLabel = new JLabel("Keyword:");
        keywordLabel.setForeground(TEXT_LIGHT);

        JTextField searchField = createFormField();
        
        JButton searchBtn = createButton("Search", PRIMARY_COLOR);
        JButton clearBtn = createButton("Clear", TEXT_MUTED);

        searchPanel.add(typeLabel);
        searchPanel.add(searchTypeCombo);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(keywordLabel);
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchBtn);
        searchPanel.add(clearBtn);

        mainPanel.add(searchPanel, BorderLayout.NORTH);

        // Results table
        DefaultTableModel resultTableModel = new DefaultTableModel(
            new String[]{"ID", "Name", "Details", "Type"}, 0
        );
        JTable resultTable = createStyledTable(resultTableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.getViewport().setBackground(BG_CARD);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> performSearch(searchTypeCombo, searchField, resultTableModel));
        clearBtn.addActionListener(e -> {
            searchField.setText("");
            resultTableModel.setRowCount(0);
        });

        return mainPanel;
    }

    private JPanel createManageDataPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Patients panel
        JPanel patientsPanel = createManagePanel("Patients", patientTableModel, patientTable, () -> deletePatient());
        mainPanel.add(patientsPanel);

        // Doctors panel
        JPanel doctorsPanel = createManagePanel("Doctors", doctorTableModel, doctorTable, () -> deleteDoctor());
        mainPanel.add(doctorsPanel);

        // Consultations panel
        JPanel consultationsPanel = createManagePanel("Consultations", consultationTableModel, consultationTable, () -> deleteConsultation());
        mainPanel.add(consultationsPanel);

        return mainPanel;
    }

    private JPanel createManagePanel(String title, DefaultTableModel model, JTable table, Runnable deleteAction) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(PRIMARY_COLOR);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BG_CARD);
        scrollPane.setVisible(model != null);

        JButton deleteBtn = createButton("Delete Selected", DANGER_COLOR);
        deleteBtn.addActionListener(e -> deleteAction.run());

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(deleteBtn, BorderLayout.SOUTH);

        return panel;
    }

    // Helper methods
    private void addFormField(JPanel panel, GridBagConstraints gbc, String label, int gridy) {
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(TEXT_LIGHT);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(fieldLabel, gbc);
    }

    private void addComponent(JPanel panel, Component component, GridBagConstraints gbc, int gridy, int gridx) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        panel.add(component, gbc);
    }

    private JTextField createFormField() {
        JTextField field = new JTextField(20);
        field.setBackground(BG_DARK);
        field.setForeground(TEXT_LIGHT);
        field.setCaretColor(TEXT_LIGHT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR),
            new EmptyBorder(8, 8, 8, 8)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return field;
    }

    private void styleComboBox(JComboBox<?> combo) {
        combo.setBackground(BG_DARK);
        combo.setForeground(TEXT_LIGHT);
        combo.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(TEXT_LIGHT);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFocusPainted(false);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(darkenColor(color));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private Color darkenColor(Color color) {
        return new Color(
            Math.max(0, color.getRed() - 30),
            Math.max(0, color.getGreen() - 30),
            Math.max(0, color.getBlue() - 30)
        );
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_LIGHT);
        table.setGridColor(BORDER_COLOR);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.getTableHeader().setBackground(PRIMARY_COLOR);
        table.getTableHeader().setForeground(TEXT_LIGHT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(TEXT_LIGHT);
        return table;
    }

    // Data operations
    private void addPatient(JTextField idField, JTextField nameField, JTextField ageField, 
                           JComboBox<String> genderCombo, JTextField contactField, JTextField addressField) {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int age = Integer.parseInt(ageField.getText().trim());
            String gender = (String) genderCombo.getSelectedItem();
            String contact = contactField.getText().trim();
            String address = addressField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Patient patient = new Patient(id, name, age, gender, contact, address);
            patientList.add(patient);
            patientTableModel.addRow(new Object[]{id, name, age, gender, contact, address});
            updatePatientCount();
            clearPatientForm(idField, nameField, ageField, genderCombo, contactField, addressField);
            JOptionPane.showMessageDialog(this, "Patient added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding patient: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addDoctor(JTextField idField, JTextField nameField, JTextField specField,
                          JTextField contactField, JTextField emailField, JTextField roomField) {
        try {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String spec = specField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();
            String room = roomField.getText().trim();

            if (id.isEmpty() || name.isEmpty() || spec.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Doctor doctor = new Doctor(id, name, spec, contact, email, room);
            doctorList.add(doctor);
            doctorTableModel.addRow(new Object[]{id, name, spec, contact, email, room});
            updateDoctorCount();
            clearDoctorForm(idField, nameField, specField, contactField, emailField, roomField);
            JOptionPane.showMessageDialog(this, "Doctor added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding doctor: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addConsultation(JTextField idField, JComboBox<String> patientCombo, JComboBox<String> doctorCombo,
                                JTextField dateField, JTextField timeField, JTextField symptomsField,
                                JTextField diagnosisField, JTextField treatmentField) {
        try {
            String id = idField.getText().trim();
            String patientId = (String) patientCombo.getSelectedItem();
            String doctorId = (String) doctorCombo.getSelectedItem();
            String date = dateField.getText().trim();
            String time = timeField.getText().trim();
            String symptoms = symptomsField.getText().trim();
            String diagnosis = diagnosisField.getText().trim();
            String treatment = treatmentField.getText().trim();

            if (id.isEmpty() || date.isEmpty() || time.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Consultation consultation = new Consultation(id, patientId, doctorId, date, time, symptoms, diagnosis, treatment);
            consultationList.add(consultation);
            consultationTableModel.addRow(new Object[]{id, patientId, doctorId, date, time, symptoms, diagnosis, treatment});
            updateConsultationCount();
            clearConsultationForm(idField, patientCombo, doctorCombo, dateField, timeField, symptomsField, diagnosisField, treatmentField);
            JOptionPane.showMessageDialog(this, "Consultation recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error recording consultation: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow >= 0) {
            String patientId = (String) patientTableModel.getValueAt(selectedRow, 0);
            patientTableModel.removeRow(selectedRow);
            updatePatientCount();
            JOptionPane.showMessageDialog(this, "Patient deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a patient to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow >= 0) {
            String doctorId = (String) doctorTableModel.getValueAt(selectedRow, 0);
            doctorTableModel.removeRow(selectedRow);
            updateDoctorCount();
            JOptionPane.showMessageDialog(this, "Doctor deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a doctor to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteConsultation() {
        int selectedRow = consultationTable.getSelectedRow();
        if (selectedRow >= 0) {
            consultationTableModel.removeRow(selectedRow);
            updateConsultationCount();
            JOptionPane.showMessageDialog(this, "Consultation deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a consultation to delete!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void performSearch(JComboBox<String> typeCombo, JTextField searchField, DefaultTableModel resultModel) {
        String searchType = (String) typeCombo.getSelectedItem();
        String keyword = searchField.getText().trim();

        resultModel.setRowCount(0);

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a search keyword!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        switch (searchType) {
            case "Patient by ID":
            case "Patient by Name":
                searchPatients(keyword, searchType.contains("ID"), resultModel);
                break;
            case "Doctor by ID":
            case "Doctor by Name":
                searchDoctors(keyword, searchType.contains("ID"), resultModel);
                break;
            case "Consultation by ID":
                searchConsultations(keyword, resultModel);
                break;
        }
    }

    private void searchPatients(String keyword, boolean searchById, DefaultTableModel resultModel) {
        for (int i = 0; i < patientTableModel.getRowCount(); i++) {
            String id = (String) patientTableModel.getValueAt(i, 0);
            String name = (String) patientTableModel.getValueAt(i, 1);
            
            if ((searchById && id.equalsIgnoreCase(keyword)) || 
                (!searchById && name.toLowerCase().contains(keyword.toLowerCase()))) {
                resultModel.addRow(new Object[]{id, name, "Patient", "Patient"});
            }
        }
    }

    private void searchDoctors(String keyword, boolean searchById, DefaultTableModel resultModel) {
        for (int i = 0; i < doctorTableModel.getRowCount(); i++) {
            String id = (String) doctorTableModel.getValueAt(i, 0);
            String name = (String) doctorTableModel.getValueAt(i, 1);
            
            if ((searchById && id.equalsIgnoreCase(keyword)) || 
                (!searchById && name.toLowerCase().contains(keyword.toLowerCase()))) {
                resultModel.addRow(new Object[]{id, name, "Doctor", "Doctor"});
            }
        }
    }

    private void searchConsultations(String keyword, DefaultTableModel resultModel) {
        for (int i = 0; i < consultationTableModel.getRowCount(); i++) {
            String id = (String) consultationTableModel.getValueAt(i, 0);
            if (id.equalsIgnoreCase(keyword)) {
                resultModel.addRow(new Object[]{id, "Consultation", "", "Consultation"});
            }
        }
    }

    private void clearPatientForm(JTextField idField, JTextField nameField, JTextField ageField,
                                 JComboBox<String> genderCombo, JTextField contactField, JTextField addressField) {
        idField.setText("");
        nameField.setText("");
        ageField.setText("");
        genderCombo.setSelectedIndex(0);
        contactField.setText("");
        addressField.setText("");
    }

    private void clearDoctorForm(JTextField idField, JTextField nameField, JTextField specField,
                                JTextField contactField, JTextField emailField, JTextField roomField) {
        idField.setText("");
        nameField.setText("");
        specField.setText("");
        contactField.setText("");
        emailField.setText("");
        roomField.setText("");
    }

    private void clearConsultationForm(JTextField idField, JComboBox<String> patientCombo, JComboBox<String> doctorCombo,
                                      JTextField dateField, JTextField timeField, JTextField symptomsField,
                                      JTextField diagnosisField, JTextField treatmentField) {
        idField.setText("");
        dateField.setText("");
        timeField.setText("");
        symptomsField.setText("");
        diagnosisField.setText("");
        treatmentField.setText("");
    }

    private void updatePatientCount() {
        patientCountLabel.setText(String.valueOf(patientTableModel.getRowCount()));
    }

    private void updateDoctorCount() {
        doctorCountLabel.setText(String.valueOf(doctorTableModel.getRowCount()));
    }

    private void updateConsultationCount() {
        consultationCountLabel.setText(String.valueOf(consultationTableModel.getRowCount()));
    }

    // Custom TabbedPane UI
    private static class CustomTabbedPaneUI extends javax.swing.plaf.basic.BasicTabbedPaneUI {
        @Override
        protected void paintTabBackground(java.awt.Graphics g, int tabPlacement, int tabIndex,
                                         int x, int y, int w, int h, boolean isSelected) {
            if (isSelected) {
                g.setColor(PRIMARY_COLOR);
            } else {
                g.setColor(BG_CARD);
            }
            g.fillRect(x, y, w, h);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MedClinicGUI());
    }
}
