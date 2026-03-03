import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PanelForm extends JPanel {

    private final JTextField txtProjectName = new JTextField(22);
    private final JTextField txtTeamLeader  = new JTextField(22);
    private final JTextField txtStartDate   = new JTextField(22);

    private final JComboBox<String> cmbTeamSize = new JComboBox<>(
            new String[] { "1-3", "4-6", "7-10", "10+" }
    );

    private final JComboBox<String> cmbProjectType = new JComboBox<>(
            new String[] { "Web", "Mobile", "Desktop", "API" }
    );

    private final JButton btnSave  = new JButton("Save");
    private final JButton btnClear = new JButton("Clear");

    private static final DateTimeFormatter RECORD_TIME_FMT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Service (2nd class)
    private final FileService fileService = new FileService("projects.txt");

    public PanelForm() {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildFormBody(), BorderLayout.CENTER);
        add(buildFooterButtons(), BorderLayout.SOUTH);

        btnClear.addActionListener(e -> clearForm());
        btnSave.addActionListener(e -> saveForm());

        cmbTeamSize.setSelectedIndex(0);
        cmbProjectType.setSelectedIndex(0);
    }

    private JComponent buildHeader() {
        JLabel title = new JLabel("Software Project Registration Form", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return title;
    }

    private JComponent buildFormBody() {
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(buildRow("Project Name", txtProjectName));
        body.add(buildRow("Team Leader", txtTeamLeader));
        body.add(buildRow("Team Size", cmbTeamSize));
        body.add(buildRow("Project Type", cmbProjectType));
        body.add(buildRow("Start Date (DD/MM/YYYY)", txtStartDate));

        return body;
    }

    private JPanel buildRow(String labelText, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        JLabel lbl = new JLabel(labelText + " :");
        lbl.setPreferredSize(new Dimension(170, 26));

        row.add(lbl, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        row.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
        return row;
    }

    private JComponent buildFooterButtons() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        footer.add(btnClear);
        footer.add(btnSave);
        return footer;
    }

    private void clearForm() {
        txtProjectName.setText("");
        txtTeamLeader.setText("");
        txtStartDate.setText("");
        cmbTeamSize.setSelectedIndex(0);
        cmbProjectType.setSelectedIndex(0);
        resetFieldColors();
    }

    private void saveForm() {
        resetFieldColors();

        String projectName = txtProjectName.getText().trim();
        String teamLeader  = txtTeamLeader.getText().trim();
        String startDate   = txtStartDate.getText().trim();

        if (!requireNonEmpty(txtProjectName, projectName)) {
            showWarn("Project Name is required.");
            return;
        }
        if (!requireNonEmpty(txtTeamLeader, teamLeader)) {
            showWarn("Team Leader is required.");
            return;
        }
        if (!requireNonEmpty(txtStartDate, startDate)) {
            showWarn("Start Date is required.");
            return;
        }
        if (!isValidDateFormat(startDate)) {
            highlight(txtStartDate);
            showWarn("Start Date must be in DD/MM/YYYY format (e.g., 05/11/2026).");
            return;
        }

        String teamSize    = String.valueOf(cmbTeamSize.getSelectedItem());
        String projectType = String.valueOf(cmbProjectType.getSelectedItem());
        String recordTime  = LocalDateTime.now().format(RECORD_TIME_FMT);

        String entryText = fileService.buildEntryText(
                projectName, teamLeader, teamSize, projectType, startDate, recordTime
        );

        try {
            fileService.appendEntry(entryText);
            JOptionPane.showMessageDialog(this, "Project saved successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to save project.\n" + ex.getMessage(),
                    "File I/O Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean requireNonEmpty(JTextField field, String value) {
        if (value == null || value.isEmpty()) {
            highlight(field);
            return false;
        }
        return true;
    }

    // Regex-based DD/MM/YYYY format check
    private boolean isValidDateFormat(String ddmmyyyy) {
        return ddmmyyyy.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$");
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.WARNING_MESSAGE);
    }

    private void highlight(JTextField field) {
        field.setBackground(new Color(255, 235, 235));
    }

    private void resetFieldColors() {
        txtProjectName.setBackground(Color.WHITE);
        txtTeamLeader.setBackground(Color.WHITE);
        txtStartDate.setBackground(Color.WHITE);
    }
}