package librarysystem;

import business.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

public class AddNewBookWindow extends JPanel implements LibWindow {
    private JTextField bookTitleField;
    private JTextField txtState;
    private JTextField txtZip;
    private JTextField maxCheckoutLengthField;
    private JTextField txtCity;
    private JTextField noOfCopiesField;
    private JTextField bookIsbnField;
    private JList<String> jAllAuthors;
    private JList<String> jSelectedAuthors;
    private JButton copyButton;

    private JTextField txtIsbn;
    private JTextField txtAvailability;
    private JTextField txtTitle;
    private JTable table;
    private final ControllerInterface ci = new SystemController();
    private final List<String> defaultList = new ArrayList<>();
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private JTextField searchField;
    DefaultTableModel model;

    private int selectedRow = -1;

    public AddNewBookWindow() {
        init();
    }

    private JList<String> createJList() {
        JList<String> ret = new JList<>(listModel);
        ret.setVisibleRowCount(4);
        return ret;
    }

    private void initializeDefaultList() {
        defaultList.add("Red");
        defaultList.add("Blue");
        defaultList.add("Yellow");
    }

    private void initialize() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        add(panel, BorderLayout.NORTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JLabel lblNewLabel = new JLabel("Book shelf");
        panel.add(lblNewLabel);
        Object[] columnsObjects = {"ISBN", "Title", "Maximum Checkout", "Available copies", "Total copies",
                "Authors"};
        model = new DefaultTableModel();
        model.setColumnIdentifiers(columnsObjects);
        updateJtable();

        JPanel panel_1 = new JPanel();
        add(panel_1, BorderLayout.SOUTH);

        JPanel panel_2 = new JPanel();
        add(panel_2, BorderLayout.CENTER);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(new GridLayout(0, 6, 0, 0));
        panel_3.setBounds(5, 231, 700, 39);

        JButton addNewBookButton = new JButton("ADD NEW BOOK");
        panel_3.add(addNewBookButton);

        JPanel middlePanel = new JPanel();
        middlePanel.setBounds(5, 5, 460, 219);
        middlePanel.setLayout(new GridLayout(0, 2, 0, 0));

        middlePanel.add(new JLabel("ISBN: "));
        bookIsbnField = new JTextField();
        middlePanel.add(bookIsbnField);
        bookIsbnField.setColumns(10);

        middlePanel.add(new JLabel("Title:"));
        bookTitleField = new JTextField();
        middlePanel.add(bookTitleField);
        bookTitleField.setColumns(10);

        middlePanel.add(new JLabel("Maximum Checkout Length (days):"));
        maxCheckoutLengthField = new JTextField();
        middlePanel.add(maxCheckoutLengthField);
        maxCheckoutLengthField.setColumns(10);

        middlePanel.add(new JLabel("No. Of Copies:"));
        noOfCopiesField = new JTextField();
        middlePanel.add(noOfCopiesField);
        noOfCopiesField.setColumns(10);

        List<Author> allAuthors = ci.allBooks().stream()
                .map(Book::getAuthors)
                .flatMap(List::stream)
                .distinct()
                .toList();

        List<String> authorsFullName = allAuthors.stream()
                .map(Person::getFullName)
                .distinct()
                .toList();

        String[] authors = authorsFullName.toArray(new String[0]);

        middlePanel.add(new JLabel("Authors:"));
        jAllAuthors = new JList<>(authors);
        jAllAuthors.setFixedCellHeight(-1);
        jAllAuthors.setFixedCellWidth(100);
        jAllAuthors.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        jAllAuthors.setVisibleRowCount(4);
        middlePanel.add(new JScrollPane(jAllAuthors));

//        middlePanel.add(new JLabel(""));
        copyButton = new JButton(">>>");

        copyButton.addActionListener(e -> {
            int[] selectedIndices = jAllAuthors.getSelectedIndices();
            String[] selectedAuthors = new String[selectedIndices.length];
            for (int i = 0; i < selectedIndices.length; i++) {
                selectedAuthors[i] = authors[selectedIndices[i]];
                jAllAuthors.setSelectedIndex(i);
            }
            jSelectedAuthors.setListData(selectedAuthors);
        });

        middlePanel.add(copyButton);
        jSelectedAuthors = new JList<>();
        jSelectedAuthors.setFixedCellHeight(-1);
        jSelectedAuthors.setFixedCellWidth(100);
        jAllAuthors.setVisibleRowCount(4);
        jAllAuthors.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        middlePanel.add(new JScrollPane(jSelectedAuthors));

        JList<String> mainList = createJList();
        mainList.setFixedCellWidth(70);
        JScrollPane mainScroll = new JScrollPane(mainList);

        initializeDefaultList();
        panel_2.add(mainScroll);

        panel_2.setLayout(null);
        panel_2.add(panel_3);
        panel_2.add(middlePanel);

        JPanel panel_4 = new JPanel();
        panel_4.setBounds(5, 282, 580, 275);
        panel_2.add(panel_4);
        panel_4.setLayout(new BorderLayout(0, 0));
        table = new JTable() {
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(model);
        TableColumnModel colModel = table.getColumnModel();
//        colModel.getColumn(5).setPreferredWidth(200);
//        colModel.getColumn(4).setPreferredWidth(50);
//        colModel.getColumn(3).setPreferredWidth(50);
//        colModel.getColumn(2).setPreferredWidth(50);
//        colModel.getColumn(1).setPreferredWidth(100);
//        colModel.getColumn(0).setPreferredWidth(75);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(table);
        panel_4.add(jScrollPane);

        addNewBookButton.addActionListener(e -> {
            String bookIsbn = bookIsbnField.getText();
            String bookTitle = bookTitleField.getText();
            String maxCheckOutLength = maxCheckoutLengthField.getText();
            String noOfCopies = noOfCopiesField.getText();
            int selectedAuthorsSize = jSelectedAuthors.getModel().getSize();

            if (bookIsbn.isEmpty() || bookTitle.isEmpty() || maxCheckOutLength.isEmpty() || noOfCopies.isEmpty() || selectedAuthorsSize == 0) {
                JOptionPane.showMessageDialog(this, "Please provide all required fields", "", ERROR_MESSAGE);
                return;
            }

            Book book = ci.getBookByIsbn(bookIsbn);
            if (book != null) {
                JOptionPane.showMessageDialog(this, "Book with ISBN " + bookIsbn + " already exists", "", ERROR_MESSAGE);
                return;
            }

            List<String> selectedAuthors = new ArrayList<>();
            for (int i = 0; i < selectedAuthorsSize; i++) {
                selectedAuthors.add(jSelectedAuthors.getModel().getElementAt(i));
            }

            List<Author> bookAuthors = allAuthors.stream()
                    .filter(author -> selectedAuthors.contains(author.getFullName()))
                    .toList();

            book = new Book(bookIsbn, bookTitle, Integer.parseInt(maxCheckOutLength), bookAuthors);

            //add book copies
            book.addCopies(Integer.parseInt(noOfCopies));
            ci.saveBook(book);

            updateJtable();
            clearText();

            JOptionPane.showMessageDialog(this, "Book successfully added", "", INFORMATION_MESSAGE,
                    new ImageIcon(System.getProperty("user.dir") + "/src/librarysystem/success.png"));
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int count = table.getSelectedRowCount();
                if (count == 1) {
                    selectedRow = table.getSelectedRow();
                    Book book = ci.getBookByIsbn((String) model.getValueAt(selectedRow, 0));
                    bookTitleField.setText(book.getTitle());
                    bookIsbnField.setText(book.getIsbn());
                    noOfCopiesField.setText(String.valueOf(book.getNumCopies()));
                    maxCheckoutLengthField.setText(String.valueOf(book.getMaxCheckoutLength()));

                    List<String> bookAuthors = book.getAuthors().stream().map(Author::getFullName).toList();
                    jSelectedAuthors.setListData(bookAuthors.toArray(new String[0]));
                } else {
                    clearText();
                }
                super.mouseClicked(e);
            }
        });
    }

    void clearText() {
        bookIsbnField.setText("");
        bookTitleField.setText("");
        maxCheckoutLengthField.setText("");
        noOfCopiesField.setText("");
        jAllAuthors.clearSelection();
        jSelectedAuthors.setListData(new String[0]);
    }

    void updateJtable() {
        model.setRowCount(0);
        Collection<Book> books = ci.allBooks();
        for (Book book : books) {
            model.addRow(new Object[]{
                    book.getIsbn(),
                    book.getTitle(),
                    book.getMaxCheckoutLength(),
                    book.getAvailableBooksLength(),
                    book.getCopies().length,
                    book.getAuthors().stream().map(Author::getFullName).collect(Collectors.joining(", "))
            });
        }
    }

    void updateJtableByIsbn(String isbn) {
        model.setRowCount(0);
        Collection<Book> books = ci.allBooks();
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                model.addRow(new Object[]{
                        book.getIsbn(), book.getTitle(),
                        book.getMaxCheckoutLength(),
                        book.getAvailableBooksLength(),
                        book.getCopies().length,
                        book.getAuthors().stream().map(Author::getFullName).collect(Collectors.joining(", "))
                });
                break;
            }
        }
    }

    @Override
    public void init() {
        initialize();
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void isInitialized(boolean val) {

    }
}