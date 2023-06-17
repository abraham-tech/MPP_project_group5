package librarysystem;

import business.Book;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class AddBookWindow extends JPanel implements LibWindow {
    private JTextField bookTitleField;
    private JTextField txtState;
    private JTextField txtZip;
    private JTextField maxCheckoutLength;
    private JTextField txtCity;
    private JTextField noOfCopiesField;
    private JTextField bookIsbnField;
    private JTextField txtTelephone;

    ControllerInterface ci = new SystemController();

    private boolean isInitialized = false;

    public AddBookWindow() {
        init();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblNewLabel = new JLabel("Add New Book Search");
        panel.add(lblNewLabel);

        JPanel panel_1 = new JPanel();
        add(panel_1, BorderLayout.SOUTH);

        JPanel panel_2 = new JPanel();
        add(panel_2, BorderLayout.CENTER);

        JPanel panel_3 = new JPanel();
        panel_3.setBounds(154, 231, 430, 39);

        JButton searchMemberButton = new JButton("SEARCH");
        panel_3.add(searchMemberButton);
        searchMemberButton.setHorizontalAlignment(SwingConstants.RIGHT);

        JButton printRecordButton = new JButton("PRINT RECORD");
        panel_3.add(printRecordButton);

        JButton clearFieldsButton = new JButton("CLEAR FIELDS");
        panel_3.add(clearFieldsButton);

        JPanel middlePanel = new JPanel();
        middlePanel.setBounds(5, 5, 460, 219);
        middlePanel.setLayout(new GridLayout(0, 2, 0, 0));

        bookIsbnField = new JTextField();
        middlePanel.add(bookIsbnField);
        bookIsbnField.setColumns(10);
        middlePanel.add(new JLabel("ISBN: "));

        bookTitleField = new JTextField();
        middlePanel.add(bookTitleField);
        bookTitleField.setColumns(10);
        middlePanel.add(new JLabel("Title:"));

        maxCheckoutLength = new JTextField();
        middlePanel.add(maxCheckoutLength);
        maxCheckoutLength.setColumns(10);
        middlePanel.add(new JLabel("Maximum Checkout Length (days):"));

        noOfCopiesField = new JTextField();
        middlePanel.add(noOfCopiesField);
        noOfCopiesField.setColumns(10);
        middlePanel.add(new JLabel("No. Of Copies:"));

        middlePanel.add(new JLabel("State:"));
        txtCity = new JTextField();
        middlePanel.add(txtCity);
        txtCity.setColumns(10);

        txtState = new JTextField();
        middlePanel.add(txtState);
        txtState.setColumns(10);

        JLabel lblZip = new JLabel("Zip:");
        middlePanel.add(lblZip);

        txtZip = new JTextField();
        middlePanel.add(txtZip);
        txtZip.setColumns(10);

        JLabel lblTelephone = new JLabel("Telephone:");
        middlePanel.add(lblTelephone);

        txtTelephone = new JTextField();
        middlePanel.add(txtTelephone);
        txtTelephone.setColumns(10);

        panel_2.setLayout(null);
        panel_2.add(panel_3);
        panel_2.add(middlePanel);

        searchMemberButton.addActionListener((evt) -> {

            String memberId = bookIsbnField.getText();
            if (memberId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please Enter Member ID to search", "", ERROR_MESSAGE);
                return;
            }

            LibraryMember member = ci.findMemberById(memberId);

            if (member == null) {
                JOptionPane.showMessageDialog(this, "Member with ID " + memberId + " not found", "", ERROR_MESSAGE);
                return;
            }

            bookIsbnField.setEnabled(false);

            bookTitleField.setText(member.getFirstName());
            maxCheckoutLength.setText(member.getLastName());
            txtTelephone.setText(member.getTelephone());
            noOfCopiesField.setText(member.getAddress().getStreet());
            txtZip.setText(member.getAddress().getZip());
            txtCity.setText(member.getAddress().getCity());
            txtState.setText(member.getAddress().getState());
        });

        printRecordButton.addActionListener(e -> {

            String memberId = bookIsbnField.getText();
            if (memberId.isEmpty() || bookIsbnField.isEnabled()) {
                JOptionPane.showMessageDialog(this, "Please Enter Member ID to search", "",
                        ERROR_MESSAGE);
                return;
            }

            LibraryMember member = ci.findMemberById(memberId);

            if (member == null) {
                JOptionPane.showMessageDialog(this, "Member with ID " + memberId + " not found", "",
                        ERROR_MESSAGE);
                return;
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            System.out.printf("%-10s\t%-10s\t%-8s\t%-6s\t%-20s\t%s\n", "CHECKOUT", "DUE", "ISBN", "COPIES", "TITLE", "MEMBER");
            member.getCheckoutRecords().forEach(record -> {
                record.getEntries().forEach(entry -> {
                    Book b = entry.getCopy().getBook();
                    LibraryMember m = record.getMember();
                    String checkoutDate = entry.getCheckoutDate().format(formatter);
                    String dueDate = entry.getDueDate().format(formatter);
                    System.out.printf("%-10s\t%-10s\t%-8s\t%-6s\t%-20s\t%s\n", checkoutDate, dueDate, b.getIsbn(), b.getNumCopies(), b.getTitle(), m.getFullName());
                });
            });
        });

        clearFieldsButton.addActionListener((evt) -> clearText());
    }

    void clearText() {
        txtCity.setText("");
        bookIsbnField.setEnabled(true);
        bookTitleField.setText("");
        bookIsbnField.setText("");
        maxCheckoutLength.setText("");
        noOfCopiesField.setText("");
        txtState.setText("");
        txtTelephone.setText("");
        txtZip.setText("");
    }

    @Override
    public void init() {
        initialize();
        this.isInitialized(true);
    }

    @Override
    public boolean isInitialized() {
        return this.isInitialized;
    }

    @Override
    public void isInitialized(boolean val) {
        this.isInitialized = val;
    }
}
