import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;

class MedicClinic extends JFrame {
    //Colors
    private Color backgroundColor = new Color(240, 248, 255); // Alice Blue
    private Color textColor = new Color(0, 0, 128); // Navy
    private static final Color BG_DARK = new Color(20, 25, 38); // Dark Background
    private static final Color BG_CARD = new Color(30, 36, 56); // Card Background
    private static final Color ACCENT_PRIMARY = new Color(79, 110, 242); // Cornflower Blue
    private static final Color ACCENT_SUCCESS = new Color(46, 196, 183);
    private static final Color ACCENT_WARNING = new Color(243, 156, 17);
    private static final Color ACCENT_DANGER = new Color(201, 45, 78);
    private static final Color BORDER_COLOR = new Color(48, 56, 86); // Dark Slate Blue
    private static final Color TXT_MUTED =  new Color(155, 164, 181);// Muted Text Color
    private static final Color TXT_LIGHT = new Color(236, 239, 241); // Light text / placeholder color

    private JLabel patientCountLabel;
    private JLabel doctorCountLabel;
    private JLabel consultationCountLabel;

    private DefaultTableModel dashSearchModel;

     // Patients Tab components
    private DefaultTableModel patientTableModel;
    private JTable patientTable;
    private ModernTextField pIdField, pNameField, pAgeField, pContactField, pAddressField;
    private JComboBox<String> pGenderCombo;
    private JButton btnAddPatient, btnClearPatient;
    private boolean isEditingPatient = false;

    // Doctors Tab components
    private DefaultTableModel doctorTableModel;
    private JTable doctorTable;
    private ModernTextField dIdField, dNameField, dSpecializationField, dContactField, dEmailField, dRoomField;
    private JButton btnAddDoctor, btnClearDoctor;
    private boolean isEditingDoctor = false;

    // Consultations Tab components
    private DefaultTableModel consultationTableModel;
    private JTable consultationTable;
    private ModernTextField cIdField, cDateField, cTimeField, cSymptomsField, cDiagnosisField, cTreatmentField;
    private JComboBox<String> consultPatientCombo;
    private JComboBox<String> consultDoctorCombo;
    private JButton btnAddConsultation, btnClearConsultation;
    private boolean isEditingConsultation = false;

    // Custom Linked Lists to store data
    private final CustomLinkedList<Patient> patientList = new CustomLinkedList<>();
    private final CustomLinkedList<Doctor> doctorList = new CustomLinkedList<>();
    private final CustomLinkedList<Consultation> consultationList = new CustomLinkedList<>();

    //_______________________
    public MedicClinic(){
        setTitle("Medic Clinic");
        setSize(850, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);

        // Customize UIManager for consistent look and feel
        customizeUIManager();

        initializeMockData();

        //---Header Panel
        add(buildHeader(), BorderLayout.NORTH);

        //----Tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
        tabbedPane.setBackground(new Color(255, 255, 255)); // White
        tabbedPane.setForeground(textColor);

        //added tabs
        tabbedPane.addTab("Dashboard", createDashboardTab());
        tabbedPane.addTab("Patients", createPatientsTab());
        tabbedPane.addTab("Doctors", createDoctorsTab());
        tabbedPane.addTab("Consultations", createConsultationsTab());

        add(tabbedPane, BorderLayout.CENTER);
        refreshDashboardStats();
    }
     private void customizeUIManager(){
        UIManager.put("TabbedPane.selected", ACCENT_PRIMARY);
        UIManager.put("TabbbedPane.highlight", BORDER_COLOR);
        UIManager.put("TabbedPane.shadow", BORDER_COLOR);
        UIManager.put("TabbedPane.borderHighlightColor", BORDER_COLOR);
    }

    private void initializeMockData() {
        // Patients
        patientList.add(new Patient("P101", "John Doe", 32, "Male", "0771234567", "123 Main St, New York"));
        patientList.add(new Patient("P102", "Alice Smith", 28, "Female", "0777654321", "456 Oak Ave, Boston"));
        patientList.add(new Patient("P103", "Bob Johnson", 45, "Male", "0768889999", "789 Pine Rd, Chicago"));
        patientList.add(new Patient("P104", "Emma Watson", 19, "Female", "0751112222", "321 Maple Dr, Seattle"));

        // Doctors
        doctorList.add(new Doctor("D201", "Dr. Gregory House", "Nephrology / Diagnostics", "0771112222", "house@mediclinic.com", "Room 404"));
        doctorList.add(new Doctor("D202", "Dr. Meredith Grey", "General Surgery", "0773334444", "grey@mediclinic.com", "Room 102"));
        doctorList.add(new Doctor("D203", "Dr. Stephen Strange", "Neurosurgery", "0775556666", "strange@mediclinic.com", "Room 303"));

        // Consultations
        consultationList.add(new Consultation("C301", "P101", "D201", "2026-05-25", "10:30 AM", "Severe fatigue, leg swelling", "Acute Kidney Injury", "Hydration, low-salt diet, follow-up lab tests"));
        consultationList.add(new Consultation("C302", "P102", "D202", "2026-05-26", "02:15 PM", "Abdominal pain, nausea", "Appendicitis", "Schedule appendectomy surgery"));
        consultationList.add(new Consultation("C303", "P103", "D203", "2026-05-28", "09:00 AM", "Frequent headaches, double vision", "Optic nerve inflammation", "Steroids and weekly MRI scans"));
    }


    // =============
    //   HEADER
    // =============
    private JPanel buildHeader(){
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
            new EmptyBorder(15, 25, 15, 25)
        ));
        
        JLabel headerLabel = new JLabel("Medic Clinic", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon icon = new ImageIcon("hospital.png");
        if (icon.getIconWidth() == -1) {
            icon = new ImageIcon();
        }
        JLabel iconLabel = new JLabel(icon);

        if (icon.getIconWidth() > 0) {
            Image scaledIcon = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledIcon));
        }
        
        headerPanel.add(iconLabel, BorderLayout.WEST);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        //System Date
        JLabel dateLabel = new JLabel(new SimpleDateFormat("EEEE, d MMMM yyyy").format(new Date()));
        dateLabel.setFont(new Font("Segeo", Font.BOLD, 15));
        dateLabel.setForeground(TXT_MUTED);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        setVisible(true);
        return headerPanel;
    }

    // ============
    //  Dashboard
    // ============
    private JPanel createDashboardTab(){
        JPanel dashPanel = new JPanel(new BorderLayout(20, 20));  
        dashPanel.setBackground(BG_DARK);

        //Card Panels
        JPanel statPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statPanel.setBackground(BG_DARK);

        patientCountLabel = new JLabel("Total Patients: 0");
        doctorCountLabel = new JLabel("Total Doctors: 0");
        consultationCountLabel = new JLabel("Total Consultations: 0");

        statPanel.add(createMetricCard("Total Patients", patientCountLabel, "👤", ACCENT_PRIMARY));
        statPanel.add(createMetricCard("On-Duty Medis Doctors", doctorCountLabel, "👨‍⚕️", ACCENT_SUCCESS));
        statPanel.add(createMetricCard("Total Consultations", consultationCountLabel, "📋", ACCENT_WARNING));

        dashPanel.add(statPanel, BorderLayout.NORTH);

        JPanel searchSection = new JPanel(new BorderLayout(15, 15));
        searchSection.setBackground(BG_CARD);
        searchSection.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Header for search
        JLabel searchTitle = new JLabel("🔍 Global Linear Search System");
        searchTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        searchTitle.setForeground(TXT_LIGHT);

        JTextField globalSearchField = new ModernTextField("Type to search Patient, Doctor, or Consultation by IDs, names, symptoms...");
        globalSearchField.setPreferredSize(new Dimension(300, 40));

        JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
        searchBarPanel.setBackground(BG_CARD);
        searchBarPanel.add(globalSearchField, BorderLayout.CENTER);

        ModernButton btnSearch = new ModernButton("Search");
        btnSearch.setPreferredSize(new Dimension(120, 40));
        searchBarPanel.add(btnSearch, BorderLayout.EAST);

        // Results table
        String[] columns = {"Record Type", "Record ID", "Primary Identifier", "Details / Description"};
        dashSearchModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable searchTable = new JTable(dashSearchModel);
        styleTable(searchTable);
        JScrollPane scrollPane = new JScrollPane(searchTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        searchSection.add(searchTitle, BorderLayout.NORTH);
        searchSection.add(searchBarPanel, BorderLayout.CENTER);
        searchSection.add(scrollPane, BorderLayout.SOUTH);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        dashPanel.add(searchSection, BorderLayout.CENTER);

        // Setup real-time search or trigger on action
        btnSearch.addActionListener(e -> performGlobalSearch(globalSearchField.getText()));
        globalSearchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { performGlobalSearch(globalSearchField.getText()); }
            public void removeUpdate(DocumentEvent e) { performGlobalSearch(globalSearchField.getText()); }
            public void changedUpdate(DocumentEvent e) { performGlobalSearch(globalSearchField.getText()); }
        });

        // Trigger initial search empty string to fill table with everything or help details
        performGlobalSearch("");

        return dashPanel;
    }
    private JPanel createMetricCard(String title, JLabel valueLabel, String icon, Color accentColor){
        JPanel card = new RoundedPanel(16);
        card.setBackground(BG_CARD);
        card.setLayout(new BorderLayout(15, 10));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        //Icon Label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        iconLabel.setForeground(accentColor);
        card.add(iconLabel, BorderLayout.WEST);

        //Text Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setBackground(BG_CARD);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(TXT_MUTED);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(Color.WHITE);

        infoPanel.add(titleLabel);
        infoPanel.add(valueLabel);
        card.add(infoPanel, BorderLayout.CENTER);

        return card; 
    }

    private JPanel createPatientsTab(){
        JPanel Ppanel = new JPanel(new BorderLayout(20,20));
        Ppanel.setBackground(BG_DARK);
        Ppanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(BG_DARK);
        formWrapper.setPreferredSize(new Dimension(350,0));

        JPanel formPanel = new RoundedPanel(16);
        formPanel.setBackground(BG_CARD);
        formPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(8, 0, 8, 0);
        gbc.gridx = 0;

        JLabel formTitle = new JLabel("📝 Register Patient");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(TXT_LIGHT);
        gbc.gridy = 0;
        formPanel.add(formTitle, gbc);

        // Fields
        pIdField = new ModernTextField("Patient ID (e.g. P105)");
        gbc.gridy = 1;
        formPanel.add(createLabelFieldPair("Patient ID", pIdField), gbc);

        pNameField = new ModernTextField("Full Name");
        gbc.gridy = 2;
        formPanel.add(createLabelFieldPair("Name", pNameField), gbc);

        pAgeField = new ModernTextField("Age");
        gbc.gridy = 3;
        formPanel.add(createLabelFieldPair("Age", pAgeField), gbc);

        pGenderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        pGenderCombo.setBackground(BG_DARK);
        pGenderCombo.setForeground(TXT_LIGHT);
        pGenderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 4;
        formPanel.add(createLabelFieldPair("Gender", pGenderCombo), gbc);

        pContactField = new ModernTextField("Contact Number");
        gbc.gridy = 5;
        formPanel.add(createLabelFieldPair("Contact Number", pContactField), gbc);

        pAddressField = new ModernTextField("Home Address");
        gbc.gridy = 6;
        formPanel.add(createLabelFieldPair("Address", pAddressField), gbc);

        // Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(BG_CARD);
        btnAddPatient = new ModernButton("Save Patient");
        btnAddPatient.setBackground(ACCENT_SUCCESS);
        btnClearPatient = new ModernButton("Clear");
        btnClearPatient.setBackground(BORDER_COLOR);

        btnPanel.add(btnAddPatient);
        btnPanel.add(btnClearPatient);
        gbc.gridy = 7;
        gbc.insets = new java.awt.Insets(18, 0, 0, 0);
        formPanel.add(btnPanel, gbc);

        formWrapper.add(formPanel, BorderLayout.CENTER);
        Ppanel.add(formWrapper, BorderLayout.WEST);

        // RIGHT: Table View & Actions
        JPanel viewPanel = new JPanel(new BorderLayout(15, 15));
        viewPanel.setBackground(BG_DARK);

        // Top Filter Bar
        JPanel filterBar = new JPanel(new BorderLayout(10, 0));
        filterBar.setBackground(BG_DARK);

        JTextField pSearchField = new ModernTextField("Search patients by ID, Name...");
        pSearchField.setPreferredSize(new Dimension(250, 36));
        filterBar.add(pSearchField, BorderLayout.CENTER);

        JPanel sortingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sortingPanel.setBackground(BG_DARK);
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setForeground(TXT_MUTED);
        sortLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sortingPanel.add(sortLabel);

        JComboBox<String> pSortCombo = new JComboBox<>(new String[]{"None", "Patient ID", "Name"});
        pSortCombo.setBackground(BG_CARD);
        pSortCombo.setForeground(TXT_LIGHT);
        sortingPanel.add(pSortCombo);
        filterBar.add(sortingPanel, BorderLayout.EAST);

        viewPanel.add(filterBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Age", "Gender", "Contact", "Address"};
        patientTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        patientTable = new JTable(patientTableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.setRowSelectionAllowed(true);
        patientTable.setColumnSelectionAllowed(false);
        styleTable(patientTable);
        JScrollPane scrollPane = new JScrollPane(patientTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        // Under table actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setBackground(BG_DARK);
        ModernButton btnNewPatient = new ModernButton(" Add New Patient");
        btnNewPatient.setBackground(ACCENT_PRIMARY);
        ModernButton btnEditSelected = new ModernButton(" Edit Selected");
        btnEditSelected.setBackground(ACCENT_PRIMARY);
        ModernButton btnDeleteSelected = new ModernButton(" Delete Selected");
        btnDeleteSelected.setBackground(ACCENT_DANGER);

        actionPanel.add(btnNewPatient);
        actionPanel.add(btnEditSelected);
        actionPanel.add(btnDeleteSelected);
        viewPanel.add(actionPanel, BorderLayout.SOUTH);

        Ppanel.add(viewPanel, BorderLayout.CENTER);

        // Event Handlers
        btnAddPatient.addActionListener(e -> savePatient());
        btnClearPatient.addActionListener(e -> clearPatientForm());
        btnNewPatient.addActionListener(e -> {
            clearPatientForm();
            pIdField.requestFocusInWindow();
        });
        btnEditSelected.addActionListener(e -> loadPatientToEdit());
        btnDeleteSelected.addActionListener(e -> deletePatient());

        // Live filtering
        pSearchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshPatientTable(pSearchField.getText(), (String) pSortCombo.getSelectedItem()); }
            public void removeUpdate(DocumentEvent e) { refreshPatientTable(pSearchField.getText(), (String) pSortCombo.getSelectedItem()); }
            public void changedUpdate(DocumentEvent e) { refreshPatientTable(pSearchField.getText(), (String) pSortCombo.getSelectedItem()); }
        });

        pSortCombo.addActionListener(e -> refreshPatientTable(pSearchField.getText(), (String) pSortCombo.getSelectedItem()));

        // Double click table to edit
        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadPatientToEdit();
                }
            }
        });

        // Initialize table content
        refreshPatientTable("", "None");

        return Ppanel;
    }

    private void refreshPatientTable(String searchVal, String sortBy) {
        patientTableModel.setRowCount(0);

        // 1. Gather filtered patients (Linear Search)
        CustomLinkedList<Patient> filtered = new CustomLinkedList<>();
        String q = searchVal.toLowerCase().trim();
        if (q.equals("search patients by id, name...")) {
            q = "";
        }

        for (Patient p : patientList) {
            if (q.isEmpty() || p.getPatientID().toLowerCase().contains(q) || p.getName().toLowerCase().contains(q)) {
                filtered.add(p);
            }
        }

        // 2. Convert to array to perform manual sorting
        Patient[] arr = new Patient[filtered.size()];
        for (int i = 0; i < filtered.size(); i++) {
            arr[i] = filtered.get(i);
        }

        // 3. Perform manual Bubble Sort if required
        if ("Patient ID".equals(sortBy)) {
            bubbleSortPatientsByID(arr);
        } else if ("Name".equals(sortBy)) {
            bubbleSortPatientsByName(arr);
        }

        // 4. Fill Table Model
        for (Patient p : arr) {
            patientTableModel.addRow(new Object[]{
                    p.getPatientID(),
                    p.getName(),
                    p.getAge(),
                    p.getGender(),
                    p.getContactNumber(),
                    p.getAddress()
            });
        }
    }

    // Manual Bubble Sort for Patients by ID
    private void bubbleSortPatientsByID(Patient[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getPatientID().compareToIgnoreCase(arr[j+1].getPatientID()) > 0) {
                    Patient temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    // Manual Bubble Sort for Patients by Name
    private void bubbleSortPatientsByName(Patient[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getName().compareToIgnoreCase(arr[j+1].getName()) > 0) {
                    Patient temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    private void savePatient() {
        String id = pIdField.getText().trim();
        String name = pNameField.getText().trim();
        String ageStr = pAgeField.getText().trim();
        String gender = (String) pGenderCombo.getSelectedItem();
        String contact = pContactField.getText().trim();
        String address = pAddressField.getText().trim();

        if (name.isEmpty() || ageStr.isEmpty() || contact.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the patient fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
            if (age < 0 || age > 150) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid age (0-150).", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isEditingPatient) {
            if (findPatientById(id) != null) {
                JOptionPane.showMessageDialog(this, "Patient ID '" + id + "' already exists! Please use a unique ID.", "Duplicate ID Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            patientList.add(new Patient(id, name, age, gender, contact, address));
            JOptionPane.showMessageDialog(this, "Patient registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Patient existing = findPatientById(id);
            if (existing != null) {
                existing.setName(name);
                existing.setAge(age);
                existing.setGender(gender);
                existing.setContactNumber(contact);
                existing.setAddress(address);
                JOptionPane.showMessageDialog(this, "Patient details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        clearPatientForm();
        refreshPatientTable("", "None");
        refreshDashboardStats();
        refreshConsultationDropdowns();
        performGlobalSearch("");
    }


    private void loadPatientToEdit() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient from the table to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) patientTableModel.getValueAt(selectedRow, 0);
        Patient p = findPatientById(id);
        if (p != null) {
            pIdField.setText(p.getPatientID());
            pIdField.setEditable(false);
            pIdField.setForeground(TXT_MUTED);
            pNameField.setText(p.getName());
            pAgeField.setText(String.valueOf(p.getAge()));
            pGenderCombo.setSelectedItem(p.getGender());
            pContactField.setText(p.getContactNumber());
            pAddressField.setText(p.getAddress());

            isEditingPatient = true;
            btnAddPatient.setText("Update Details");
            btnAddPatient.setBackground(ACCENT_WARNING);
        }
    }

    private void deletePatient() {
        int selectedRow = patientTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient from the table to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) patientTableModel.getValueAt(selectedRow, 0);
        Patient p = findPatientById(id);

        if (p != null) {
            // Confirm delete
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete patient '" + p.getName() + "' (ID: " + id + ")?\nThis will not delete related consultations, but they will show as Unknown patient.",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (choice == JOptionPane.YES_OPTION) {
                patientList.remove(p);
                JOptionPane.showMessageDialog(this, "Patient deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);

                // If currently editing this patient, reset form
                if (isEditingPatient && pIdField.getText().trim().equals(id)) {
                    clearPatientForm();
                }

                refreshPatientTable("", "None");
                refreshDashboardStats();
                refreshConsultationDropdowns();
                performGlobalSearch("");
            }
        }
    }

    private void clearPatientForm() {
        pIdField.setText("");
        pIdField.setPlaceholder("Patient ID (e.g. P105)");
        pIdField.setEditable(true);
        pIdField.setForeground(TXT_LIGHT);
        pNameField.setText("");
        pNameField.setPlaceholder("Full Name");
        pAgeField.setText("");
        pAgeField.setPlaceholder("Age");
        pGenderCombo.setSelectedIndex(0);
        pContactField.setText("");
        pContactField.setPlaceholder("Contact Number");
        pAddressField.setText("");
        pAddressField.setPlaceholder("Home Address");

        isEditingPatient = false;
        btnAddPatient.setText("Save Patient");
        btnAddPatient.setBackground(ACCENT_SUCCESS);
        patientTable.clearSelection();
    }

    private Patient findPatientById(String id) {
        // Linear search for patient by ID
        for (Patient p : patientList) {
            if (p.getPatientID().equalsIgnoreCase(id)) {
                return p;
            }
        }
        return null;
    }


    private static class RoundedPanel extends JPanel {
        private int cornerRadius;

        public RoundedPanel(int radius) {
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
            g2.dispose();
        }
    }

    private static class ModernTextField extends JTextField {
        private String placeholder;
        private boolean isPlaceholderActive;

        public ModernTextField(String placeholder) {
            this.placeholder = placeholder;
            this.isPlaceholderActive = true;
            setText(placeholder);
            setForeground(TXT_MUTED);
            setFont(new Font("Segoe UI", Font.PLAIN, 14));
            setBackground(BG_DARK);
            setCaretColor(TXT_LIGHT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
            ));

            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (isPlaceholderActive) {
                        isPlaceholderActive = false;
                        setText("");
                        setForeground(TXT_LIGHT);
                    }
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(ACCENT_PRIMARY, 1, true),
                            BorderFactory.createEmptyBorder(6, 10, 6, 10)
                    ));
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (getText().trim().isEmpty()) {
                        isPlaceholderActive = true;
                        setText(placeholder);
                        setForeground(TXT_MUTED);
                    }
                    setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(BORDER_COLOR, 1, true),
                            BorderFactory.createEmptyBorder(6, 10, 6, 10)
                    ));
                }
            });
        }

        public void setPlaceholder(String placeholder) {
            this.placeholder = placeholder;
            if (getText().isEmpty() || getText().equals(placeholder)) {
                isPlaceholderActive = true;
                setText(placeholder);
                setForeground(TXT_MUTED);
            }
        }

        @Override
        public void setText(String text) {
            if (text == null || text.trim().isEmpty()) {
                isPlaceholderActive = true;
                super.setText(placeholder);
                setForeground(TXT_MUTED);
            } else {
                isPlaceholderActive = false;
                super.setText(text);
                setForeground(TXT_LIGHT);
            }
        }

        @Override
        public String getText() {
            return isPlaceholderActive ? "" : super.getText();
        }
    }

    private static class ModernButton extends JButton {
        private Color baseBg;

        public ModernButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(TXT_LIGHT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            baseBg = ACCENT_PRIMARY;

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(baseBg.brighter());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(baseBg);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    setBackground(baseBg.darker());
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setBackground(baseBg.brighter());
                }
            });
        }

        @Override
        public void setBackground(Color bg) {
            super.setBackground(bg);
            this.baseBg = bg;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    private void performGlobalSearch(String query) {
        dashSearchModel.setRowCount(0);
        String q = query.toLowerCase().trim();

        // If query is the placeholder, treat as empty
        if (q.equals("type to search patient, doctor, or consultation by ids, names, symptoms...")) {
            q = "";
        }

        // Search Patients (Linear Search)
        for (Patient p : patientList) {
            if (q.isEmpty() || p.getPatientID().toLowerCase().contains(q) || p.getName().toLowerCase().contains(q) ||
                    p.getGender().toLowerCase().contains(q) || p.getContactNumber().contains(q) || p.getAddress().toLowerCase().contains(q)) {
                dashSearchModel.addRow(new Object[]{
                        "Patient 👤",
                        p.getPatientID(),
                        p.getName(),
                        "Age: " + p.getAge() + " | Gender: " + p.getGender() + " | Contact: " + p.getContactNumber()
                });
            }
        }

        // Search Doctors (Linear Search)
        for (Doctor d : doctorList) {
            if (q.isEmpty() || d.getDoctorID().toLowerCase().contains(q) || d.getName().toLowerCase().contains(q) ||
                    d.getSpecialization().toLowerCase().contains(q) || d.getContactNumber().contains(q) || d.getEmail().toLowerCase().contains(q)) {
                dashSearchModel.addRow(new Object[]{
                        "Doctor 🩺",
                        d.getDoctorID(),
                        "Dr. " + d.getName(),
                        "Specialization: " + d.getSpecialization() + " | Room: " + d.getRoomNumber() + " | Email: " + d.getEmail()
                });
            }
        }

        // Search Consultations (Linear Search)
        for (Consultation c : consultationList) {
            Patient p = findPatientById(c.getPatientID());
            Doctor d = findDoctorById(c.getDoctorID());
            String pName = (p != null) ? p.getName() : "Unknown (" + c.getPatientID() + ")";
            String dName = (d != null) ? "Dr. " + d.getName() : "Unknown (" + c.getDoctorID() + ")";

            if (q.isEmpty() || c.getConsultationID().toLowerCase().contains(q) || c.getPatientID().toLowerCase().contains(q) ||
                    c.getDoctorID().toLowerCase().contains(q) || c.getSymptoms().toLowerCase().contains(q) ||
                    c.getDiagnosis().toLowerCase().contains(q) || pName.toLowerCase().contains(q) || dName.toLowerCase().contains(q)) {
                dashSearchModel.addRow(new Object[]{
                        "Consultation 📝",
                        c.getConsultationID(),
                        "P: " + pName + " ➔ D: " + dName,
                        "Date: " + c.getDate() + " | Diagnosis: " + c.getDiagnosis() + " | Symptoms: " + c.getSymptoms()
                });
            }
        }
    }

    //===============
    // DOCTORS TAB
    //===============
    private JPanel createDoctorsTab() {
        JPanel Dpanel = new JPanel(new BorderLayout(20, 20));
        Dpanel.setBackground(BG_DARK);
        Dpanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formWrapper = new JPanel();
        formWrapper.setBackground(BG_DARK);
        formWrapper.setPreferredSize(new Dimension(350, 0));

        JPanel formPanel = new RoundedPanel(16);
        formPanel.setBackground(BG_CARD);
        formPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;

        JLabel formTitle = new JLabel("🩺 Register Doctor");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(TXT_LIGHT);
        gbc.gridy = 0;
        formPanel.add(formTitle, gbc);

        dIdField = new ModernTextField("Doctor ID (e.g. D204)");
        gbc.gridy = 1;
        formPanel.add(createLabelFieldPair("Doctor ID", dIdField), gbc);

        dNameField = new ModernTextField("Full Name");
        gbc.gridy = 2;
        formPanel.add(createLabelFieldPair("Name", dNameField), gbc);

        dSpecializationField = new ModernTextField("Specialization");
        gbc.gridy = 3;
        formPanel.add(createLabelFieldPair("Specialization", dSpecializationField), gbc);

        dContactField = new ModernTextField("Contact Number");
        gbc.gridy = 4;
        formPanel.add(createLabelFieldPair("Contact Number", dContactField), gbc);

        dEmailField = new ModernTextField("E-mail Address");
        gbc.gridy = 5;
        formPanel.add(createLabelFieldPair("Email", dEmailField), gbc);

        dRoomField = new ModernTextField("Room Number");
        gbc.gridy = 6;
        formPanel.add(createLabelFieldPair("Room", dRoomField), gbc);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(BG_CARD);
        btnAddDoctor = new ModernButton("Save Doctor");
        btnAddDoctor.setBackground(ACCENT_SUCCESS);
        btnClearDoctor = new ModernButton("Clear");
        btnClearDoctor.setBackground(BORDER_COLOR);
        btnPanel.add(btnAddDoctor);
        btnPanel.add(btnClearDoctor);
        gbc.gridy = 7;
        gbc.insets = new Insets(18, 0, 0, 0);
        formPanel.add(btnPanel, gbc);

        JScrollPane doctorFormScroll = new JScrollPane(formPanel);
        doctorFormScroll.setBorder(null);
        doctorFormScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        doctorFormScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formWrapper.add(doctorFormScroll, BorderLayout.CENTER);
        Dpanel.add(formWrapper, BorderLayout.WEST);

        JPanel viewPanel = new JPanel(new BorderLayout(15, 15));
        viewPanel.setBackground(BG_DARK);

        JPanel filterBar = new JPanel(new BorderLayout(10, 0));
        filterBar.setBackground(BG_DARK);

        JTextField dSearchField = new ModernTextField("Search doctors by ID, name...");
        dSearchField.setPreferredSize(new Dimension(250, 36));
        filterBar.add(dSearchField, BorderLayout.CENTER);

        JPanel sortingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sortingPanel.setBackground(BG_DARK);
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setForeground(TXT_MUTED);
        sortLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sortingPanel.add(sortLabel);

        JComboBox<String> dSortCombo = new JComboBox<>(new String[]{"None", "Doctor ID", "Name", "Specialization"});
        dSortCombo.setBackground(BG_CARD);
        dSortCombo.setForeground(TXT_LIGHT);
        sortingPanel.add(dSortCombo);
        filterBar.add(sortingPanel, BorderLayout.EAST);

        viewPanel.add(filterBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Specialization", "Contact", "Email", "Room"};
        doctorTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        doctorTable = new JTable(doctorTableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.setRowSelectionAllowed(true);
        doctorTable.setColumnSelectionAllowed(false);
        styleTable(doctorTable);
        JScrollPane scrollPane = new JScrollPane(doctorTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setBackground(BG_DARK);
        ModernButton btnEditSelected = new ModernButton("✏️ Edit Selected");
        btnEditSelected.setBackground(ACCENT_PRIMARY);
        ModernButton btnDeleteSelected = new ModernButton("❌ Delete Selected");
        btnDeleteSelected.setBackground(ACCENT_DANGER);
        actionPanel.add(btnEditSelected);
        actionPanel.add(btnDeleteSelected);
        viewPanel.add(actionPanel, BorderLayout.SOUTH);

        Dpanel.add(viewPanel, BorderLayout.CENTER);

        btnAddDoctor.addActionListener(e -> saveDoctor());
        btnClearDoctor.addActionListener(e -> clearDoctorForm());
        btnEditSelected.addActionListener(e -> loadDoctorToEdit());
        btnDeleteSelected.addActionListener(e -> deleteDoctor());

        dSearchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshDoctorTable(dSearchField.getText(), (String) dSortCombo.getSelectedItem()); }
            public void removeUpdate(DocumentEvent e) { refreshDoctorTable(dSearchField.getText(), (String) dSortCombo.getSelectedItem()); }
            public void changedUpdate(DocumentEvent e) { refreshDoctorTable(dSearchField.getText(), (String) dSortCombo.getSelectedItem()); }
        });

        dSortCombo.addActionListener(e -> refreshDoctorTable(dSearchField.getText(), (String) dSortCombo.getSelectedItem()));

        doctorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadDoctorToEdit();
                }
            }
        });

        refreshDoctorTable("", "None");
        return Dpanel;
    }

    private JPanel createConsultationsTab() {
        JPanel Cpanel = new JPanel(new BorderLayout(20, 20));
        Cpanel.setBackground(BG_DARK);
        Cpanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(BG_DARK);
        formWrapper.setPreferredSize(new Dimension(360, 0));

        JPanel formPanel = new RoundedPanel(16);
        formPanel.setBackground(BG_CARD);
        formPanel.setPreferredSize(new Dimension(350, 420));
        formPanel.setBorder(new EmptyBorder(25, 20, 25, 20));
        formPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.gridx = 0;

        JLabel formTitle = new JLabel("📋 Record Consultation");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(TXT_LIGHT);
        gbc.gridy = 0;
        formPanel.add(formTitle, gbc);

        cIdField = new ModernTextField("Consultation ID (e.g. C304)");
        gbc.gridy = 1;
        formPanel.add(createLabelFieldPair("Consultation ID", cIdField), gbc);

        consultPatientCombo = new JComboBox<>();
        consultPatientCombo.setBackground(BG_DARK);
        consultPatientCombo.setForeground(TXT_LIGHT);
        consultPatientCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 2;
        formPanel.add(createLabelFieldPair("Patient", consultPatientCombo), gbc);

        consultDoctorCombo = new JComboBox<>();
        consultDoctorCombo.setBackground(BG_DARK);
        consultDoctorCombo.setForeground(TXT_LIGHT);
        consultDoctorCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridy = 3;
        formPanel.add(createLabelFieldPair("Doctor", consultDoctorCombo), gbc);

        cDateField = new ModernTextField("Date (YYYY-MM-DD)");
        gbc.gridy = 4;
        formPanel.add(createLabelFieldPair("Date", cDateField), gbc);

        cTimeField = new ModernTextField("Time (e.g. 10:30 AM)");
        gbc.gridy = 5;
        formPanel.add(createLabelFieldPair("Time", cTimeField), gbc);

        cSymptomsField = new ModernTextField("Symptoms / Complaint");
        gbc.gridy = 6;
        formPanel.add(createLabelFieldPair("Symptoms", cSymptomsField), gbc);

        cDiagnosisField = new ModernTextField("Diagnosis");
        gbc.gridy = 7;
        formPanel.add(createLabelFieldPair("Diagnosis", cDiagnosisField), gbc);

        cTreatmentField = new ModernTextField("Treatment Plan");
        gbc.gridy = 8;
        formPanel.add(createLabelFieldPair("Treatment", cTreatmentField), gbc);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBackground(BG_CARD);
        btnAddConsultation = new ModernButton("Save Consultation");
        btnAddConsultation.setBackground(ACCENT_SUCCESS);
        btnClearConsultation = new ModernButton("Clear");
        btnClearConsultation.setBackground(BORDER_COLOR);
        btnPanel.add(btnAddConsultation);
        btnPanel.add(btnClearConsultation);
        gbc.gridy = 9;
        gbc.insets = new Insets(18, 0, 0, 0);
        formPanel.add(btnPanel, gbc);

        JScrollPane consultationFormScroll = new JScrollPane(formPanel);
        consultationFormScroll.setBorder(null);
        consultationFormScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        consultationFormScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        formWrapper.add(consultationFormScroll, BorderLayout.CENTER);
        Cpanel.add(formWrapper, BorderLayout.WEST);

        JPanel viewPanel = new JPanel(new BorderLayout(15, 15));
        viewPanel.setBackground(BG_DARK);

        JPanel filterBar = new JPanel(new BorderLayout(10, 0));
        filterBar.setBackground(BG_DARK);

        JTextField cSearchField = new ModernTextField("Search consultations by ID, patient, doctor, diagnosis...");
        cSearchField.setPreferredSize(new Dimension(250, 36));
        filterBar.add(cSearchField, BorderLayout.CENTER);

        JPanel sortingPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        sortingPanel.setBackground(BG_DARK);
        JLabel sortLabel = new JLabel("Sort by:");
        sortLabel.setForeground(TXT_MUTED);
        sortLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sortingPanel.add(sortLabel);

        JComboBox<String> cSortCombo = new JComboBox<>(new String[]{"None", "Consultation ID", "Date"});
        cSortCombo.setBackground(BG_CARD);
        cSortCombo.setForeground(TXT_LIGHT);
        sortingPanel.add(cSortCombo);
        filterBar.add(sortingPanel, BorderLayout.EAST);

        viewPanel.add(filterBar, BorderLayout.NORTH);

        String[] columns = {"Consultation ID", "Patient", "Doctor", "Date", "Time", "Diagnosis"};
        consultationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        consultationTable = new JTable(consultationTableModel);
        consultationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        consultationTable.setRowSelectionAllowed(true);
        consultationTable.setColumnSelectionAllowed(false);
        styleTable(consultationTable);
        JScrollPane scrollPane = new JScrollPane(consultationTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        viewPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        actionPanel.setBackground(BG_DARK);
        ModernButton btnEditSelected = new ModernButton("✏️ Edit Selected");
        btnEditSelected.setBackground(ACCENT_PRIMARY);
        ModernButton btnDeleteSelected = new ModernButton("❌ Delete Selected");
        btnDeleteSelected.setBackground(ACCENT_DANGER);
        actionPanel.add(btnEditSelected);
        actionPanel.add(btnDeleteSelected);
        viewPanel.add(actionPanel, BorderLayout.SOUTH);

        Cpanel.add(viewPanel, BorderLayout.CENTER);

        btnAddConsultation.addActionListener(e -> saveConsultation());
        btnClearConsultation.addActionListener(e -> clearConsultationForm());
        btnEditSelected.addActionListener(e -> loadConsultationToEdit());
        btnDeleteSelected.addActionListener(e -> deleteConsultation());

        cSearchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { refreshConsultationTable(cSearchField.getText(), (String) cSortCombo.getSelectedItem()); }
            public void removeUpdate(DocumentEvent e) { refreshConsultationTable(cSearchField.getText(), (String) cSortCombo.getSelectedItem()); }
            public void changedUpdate(DocumentEvent e) { refreshConsultationTable(cSearchField.getText(), (String) cSortCombo.getSelectedItem()); }
        });

        cSortCombo.addActionListener(e -> refreshConsultationTable(cSearchField.getText(), (String) cSortCombo.getSelectedItem()));

        consultationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadConsultationToEdit();
                }
            }
        });

        refreshConsultationDropdowns();
        refreshConsultationTable("", "None");
        return Cpanel;
    }

    private void refreshDashboardStats() {
        if (patientCountLabel != null) {
            patientCountLabel.setText("Total Patients: " + patientList.size());
        }
        if (doctorCountLabel != null) {
            doctorCountLabel.setText("Total Doctors: " + doctorList.size());
        }
        if (consultationCountLabel != null) {
            consultationCountLabel.setText("Total Consultations: " + consultationList.size());
        }
    }

    private void refreshConsultationDropdowns() {
        if (consultPatientCombo != null) {
            DefaultComboBoxModel<String> patientModel = new DefaultComboBoxModel<>();
            patientModel.addElement("Select Patient");
            for (Patient p : patientList) {
                patientModel.addElement(p.getPatientID() + " - " + p.getName());
            }
            consultPatientCombo.setModel(patientModel);
        }

        if (consultDoctorCombo != null) {
            DefaultComboBoxModel<String> doctorModel = new DefaultComboBoxModel<>();
            doctorModel.addElement("Select Doctor");
            for (Doctor d : doctorList) {
                doctorModel.addElement(d.getDoctorID() + " - Dr. " + d.getName());
            }
            consultDoctorCombo.setModel(doctorModel);
        }
    }

    private void refreshDoctorTable(String searchVal, String sortBy) {
        doctorTableModel.setRowCount(0);
        CustomLinkedList<Doctor> filtered = new CustomLinkedList<>();
        String q = searchVal.toLowerCase().trim();
        if (q.equals("search doctors by id, name...")) {
            q = "";
        }
        for (Doctor d : doctorList) {
            if (q.isEmpty() || d.getDoctorID().toLowerCase().contains(q) || d.getName().toLowerCase().contains(q) || d.getSpecialization().toLowerCase().contains(q)) {
                filtered.add(d);
            }
        }
        Doctor[] arr = new Doctor[filtered.size()];
        for (int i = 0; i < filtered.size(); i++) {
            arr[i] = filtered.get(i);
        }
        if ("Doctor ID".equals(sortBy)) {
            bubbleSortDoctorsByID(arr);
        } else if ("Name".equals(sortBy)) {
            bubbleSortDoctorsByName(arr);
        } else if ("Specialization".equals(sortBy)) {
            bubbleSortDoctorsBySpecialization(arr);
        }
        for (Doctor d : arr) {
            doctorTableModel.addRow(new Object[]{
                    d.getDoctorID(),
                    d.getName(),
                    d.getSpecialization(),
                    d.getContactNumber(),
                    d.getEmail(),
                    d.getRoomNumber()
            });
        }
    }

    private void bubbleSortDoctorsByID(Doctor[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getDoctorID().compareToIgnoreCase(arr[j+1].getDoctorID()) > 0) {
                    Doctor temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    private void bubbleSortDoctorsByName(Doctor[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getName().compareToIgnoreCase(arr[j+1].getName()) > 0) {
                    Doctor temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    private void bubbleSortDoctorsBySpecialization(Doctor[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getSpecialization().compareToIgnoreCase(arr[j+1].getSpecialization()) > 0) {
                    Doctor temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    private void saveDoctor() {
        String id = dIdField.getText().trim();
        String name = dNameField.getText().trim();
        String specialization = dSpecializationField.getText().trim();
        String contact = dContactField.getText().trim();
        String email = dEmailField.getText().trim();
        String room = dRoomField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || specialization.isEmpty() || contact.isEmpty() || email.isEmpty() || room.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all the doctor fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isEditingDoctor) {
            if (findDoctorById(id) != null) {
                JOptionPane.showMessageDialog(this, "Doctor ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            doctorList.add(new Doctor(id, name, specialization, contact, email, room));
            JOptionPane.showMessageDialog(this, "Doctor registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Doctor existing = findDoctorById(id);
            if (existing != null) {
                existing.setName(name);
                existing.setSpecialization(specialization);
                existing.setContactNumber(contact);
                existing.setEmail(email);
                existing.setRoomNumber(room);
                JOptionPane.showMessageDialog(this, "Doctor details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        clearDoctorForm();
        refreshDoctorTable("", "None");
        refreshDashboardStats();
        refreshConsultationDropdowns();
        performGlobalSearch("");
    }

    private void loadDoctorToEdit() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a doctor from the table to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) doctorTableModel.getValueAt(selectedRow, 0);
        Doctor d = findDoctorById(id);
        if (d != null) {
            dIdField.setText(d.getDoctorID());
            dIdField.setEditable(false);
            dIdField.setForeground(TXT_MUTED);
            dNameField.setText(d.getName());
            dSpecializationField.setText(d.getSpecialization());
            dContactField.setText(d.getContactNumber());
            dEmailField.setText(d.getEmail());
            dRoomField.setText(d.getRoomNumber());
            isEditingDoctor = true;
            btnAddDoctor.setText("Update Details");
            btnAddDoctor.setBackground(ACCENT_WARNING);
        }
    }

    private void deleteDoctor() {
        int selectedRow = doctorTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a doctor from the table to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = (String) doctorTableModel.getValueAt(selectedRow, 0);
        Doctor d = findDoctorById(id);
        if (d != null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete doctor '" + d.getName() + "' (ID: " + id + ")?\nThis will not remove consultations, but they will show as Unknown doctor.",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                doctorList.remove(d);
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                if (isEditingDoctor && dIdField.getText().trim().equals(id)) {
                    clearDoctorForm();
                }
                refreshDoctorTable("", "None");
                refreshDashboardStats();
                refreshConsultationDropdowns();
                performGlobalSearch("");
            }
        }
    }

    private void clearDoctorForm() {
        dIdField.setText("");
        dIdField.setPlaceholder("Doctor ID (e.g. D204)");
        dIdField.setEditable(true);
        dIdField.setForeground(TXT_LIGHT);
        dNameField.setText("");
        dNameField.setPlaceholder("Full Name");
        dSpecializationField.setText("");
        dSpecializationField.setPlaceholder("Specialization");
        dContactField.setText("");
        dContactField.setPlaceholder("Contact Number");
        dEmailField.setText("");
        dEmailField.setPlaceholder("E-mail Address");
        dRoomField.setText("");
        dRoomField.setPlaceholder("Room Number");
        isEditingDoctor = false;
        btnAddDoctor.setText("Save Doctor");
        btnAddDoctor.setBackground(ACCENT_SUCCESS);
        doctorTable.clearSelection();
    }

    private Doctor findDoctorById(String id) {
        for (Doctor d : doctorList) {
            if (d.getDoctorID().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }

    private void refreshConsultationTable(String searchVal, String sortBy) {
        consultationTableModel.setRowCount(0);
        String q = searchVal.toLowerCase().trim();
        if (q.equals("search consultations by id, patient, doctor, diagnosis...")) {
            q = "";
        }
        Consultation[] arr = new Consultation[consultationList.size()];
        for (int i = 0; i < consultationList.size(); i++) {
            arr[i] = consultationList.get(i);
        }
        if ("Consultation ID".equals(sortBy)) {
            bubbleSortConsultationsByID(arr);
        } else if ("Date".equals(sortBy)) {
            bubbleSortConsultationsByDate(arr);
        }
        for (Consultation c : arr) {
            Patient p = findPatientById(c.getPatientID());
            Doctor d = findDoctorById(c.getDoctorID());
            String pName = (p != null) ? p.getName() : "Unknown";
            String dName = (d != null) ? d.getName() : "Unknown";
            if (q.isEmpty() || c.getConsultationID().toLowerCase().contains(q) || c.getPatientID().toLowerCase().contains(q) ||
                    c.getDoctorID().toLowerCase().contains(q) || c.getSymptoms().toLowerCase().contains(q) || c.getDiagnosis().toLowerCase().contains(q) ||
                    pName.toLowerCase().contains(q) || dName.toLowerCase().contains(q)) {
                consultationTableModel.addRow(new Object[]{
                        c.getConsultationID(),
                        c.getPatientID() + " - " + pName,
                        c.getDoctorID() + " - Dr. " + dName,
                        c.getDate(),
                        c.getTime(),
                        c.getDiagnosis()
                });
            }
        }
    }

    private void bubbleSortConsultationsByID(Consultation[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getConsultationID().compareToIgnoreCase(arr[j+1].getConsultationID()) > 0) {
                    Consultation temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    private void bubbleSortConsultationsByDate(Consultation[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].getDate().compareToIgnoreCase(arr[j+1].getDate()) > 0) {
                    Consultation temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }
    }

    private void saveConsultation() {
        String id = cIdField.getText().trim();
        String patientSelection = (String) consultPatientCombo.getSelectedItem();
        String doctorSelection = (String) consultDoctorCombo.getSelectedItem();
        String date = cDateField.getText().trim();
        String time = cTimeField.getText().trim();
        String symptoms = cSymptomsField.getText().trim();
        String diagnosis = cDiagnosisField.getText().trim();
        String treatment = cTreatmentField.getText().trim();

        if (id.isEmpty() || patientSelection == null || doctorSelection == null || patientSelection.startsWith("Select") || doctorSelection.startsWith("Select") ||
                date.isEmpty() || time.isEmpty() || symptoms.isEmpty() || diagnosis.isEmpty() || treatment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all consultation fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String patientId = patientSelection.split(" - ")[0];
        String doctorId = doctorSelection.split(" - ")[0];
        if (!isEditingConsultation) {
            if (findConsultationById(id) != null) {
                JOptionPane.showMessageDialog(this, "Consultation ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            consultationList.add(new Consultation(id, patientId, doctorId, date, time, symptoms, diagnosis, treatment));
        } else {
            Consultation existing = findConsultationById(id);
            if (existing != null) {
                existing.setPatientID(patientId);
                existing.setDoctorID(doctorId);
                existing.setDate(date);
                existing.setTime(time);
                existing.setSymptoms(symptoms);
                existing.setDiagnosis(diagnosis);
                existing.setTreatment(treatment);
            }
        }

        clearConsultationForm();
        refreshConsultationTable("", "None");
        refreshDashboardStats();
        performGlobalSearch("");
    }

    private void loadConsultationToEdit() {
        int selectedRow = consultationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a consultation from the table to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) consultationTableModel.getValueAt(selectedRow, 0);
        Consultation c = findConsultationById(id);
        if (c != null) {
            cIdField.setText(c.getConsultationID());
            cIdField.setEditable(false);
            cIdField.setForeground(TXT_MUTED);
            cDateField.setText(c.getDate());
            cTimeField.setText(c.getTime());
            cSymptomsField.setText(c.getSymptoms());
            cDiagnosisField.setText(c.getDiagnosis());
            cTreatmentField.setText(c.getTreatment());
            String patientOption = c.getPatientID() + " - " + (findPatientById(c.getPatientID()) != null ? findPatientById(c.getPatientID()).getName() : "Unknown");
            String doctorOption = c.getDoctorID() + " - " + (findDoctorById(c.getDoctorID()) != null ? "Dr. " + findDoctorById(c.getDoctorID()).getName() : "Unknown");
            consultPatientCombo.setSelectedItem(patientOption);
            consultDoctorCombo.setSelectedItem(doctorOption);
            isEditingConsultation = true;
            btnAddConsultation.setText("Update Details");
            btnAddConsultation.setBackground(ACCENT_WARNING);
        }
    }

    private void deleteConsultation() {
        int selectedRow = consultationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a consultation from the table to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) consultationTableModel.getValueAt(selectedRow, 0);
        Consultation c = findConsultationById(id);
        if (c != null) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete consultation '" + id + "'?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (choice == JOptionPane.YES_OPTION) {
                consultationList.remove(c);
                JOptionPane.showMessageDialog(this, "Consultation deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                if (isEditingConsultation && cIdField.getText().trim().equals(id)) {
                    clearConsultationForm();
                }
                refreshConsultationTable("", "None");
                refreshDashboardStats();
                performGlobalSearch("");
            }
        }
    }

    private Consultation findConsultationById(String id) {
        for (Consultation c : consultationList) {
            if (c.getConsultationID().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }

    private void clearConsultationForm() {
        cIdField.setText("");
        cIdField.setPlaceholder("Consultation ID (e.g. C304)");
        cIdField.setEditable(true);
        cIdField.setForeground(TXT_LIGHT);
        consultPatientCombo.setSelectedIndex(0);
        consultDoctorCombo.setSelectedIndex(0);
        cDateField.setText("");
        cDateField.setPlaceholder("Date (YYYY-MM-DD)");
        cTimeField.setText("");
        cTimeField.setPlaceholder("Time (e.g. 10:30 AM)");
        cSymptomsField.setText("");
        cSymptomsField.setPlaceholder("Symptoms / Complaint");
        cDiagnosisField.setText("");
        cDiagnosisField.setPlaceholder("Diagnosis");
        cTreatmentField.setText("");
        cTreatmentField.setPlaceholder("Treatment Plan");
        isEditingConsultation = false;
        btnAddConsultation.setText("Save Consultation");
        btnAddConsultation.setBackground(ACCENT_SUCCESS);
        consultationTable.clearSelection();
    }

    private JPanel createLabelFieldPair(String labelText, JComponent field) {
        JPanel wrapper = new JPanel(new BorderLayout(5, 5));
        wrapper.setBackground(BG_CARD);
        JLabel label = new JLabel(labelText);
        label.setForeground(TXT_LIGHT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);
        return wrapper;
    }

    private void styleTable(JTable table) {
        table.setSelectionBackground(ACCENT_PRIMARY);
        table.setSelectionForeground(TXT_LIGHT);
        table.setBackground(BG_CARD);
        table.setForeground(TXT_LIGHT);
        table.setFillsViewportHeight(true);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(BORDER_COLOR);
        table.getTableHeader().setForeground(TXT_LIGHT);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MedicClinic clinic = new MedicClinic();
            clinic.setVisible(true);
        });
    }
}