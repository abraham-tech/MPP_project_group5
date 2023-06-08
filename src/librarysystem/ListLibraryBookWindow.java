package librarysystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import business.Book;
import business.ControllerInterface;
import business.SystemController;

public class ListLibraryBookWindow extends JPanel {
	private JLabel lblIsbn;
	private JTextField txtIsbn;
	private JLabel lblAvailability;
	private JTextField txtAvailability;
	private JLabel lblTitle;
	private JTextField txtTitle;
	private JLabel lblAuthors;
	private JButton btnAdd;
	private JButton btnCopy;
	private JPanel middlePanel;
	private JFrame frame;
	private JTable table;
	private ControllerInterface ci = new SystemController();
	private JPanel panel_4;
	private List<String> defaultList = new ArrayList<>();
	private JList<String> mainList;
	private JScrollPane mainScroll;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JButton removeSelectedButton, addItemButton;
	private JTextField addField;

	private int selectedRow = -1;

	public ListLibraryBookWindow() {
		initialize();
	}

	private JList<String> createJList() {
		JList<String> ret = new JList<String>(listModel);
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
		Object[] columnsObjects = { "ISBN", "Title", "Maximum checkout", "Copies", "Authors" };
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnsObjects);
		Collection<Book> books = ci.allBooks();
		for (Book book : books) {
			Object[] objects = { book.getIsbn(), book.getTitle(), book.getMaxCheckoutLength(),
					book.getCopies().length, book.getAuthors().toString() };
			model.addRow(objects);
		}

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(100, 231, 430, 39);

		btnAdd = new JButton("ADD");
		panel_3.add(btnAdd);
		btnAdd.setHorizontalAlignment(SwingConstants.RIGHT);

		btnCopy = new JButton("COPY");
		panel_3.add(btnCopy);
		btnCopy.setHorizontalAlignment(SwingConstants.RIGHT);

		JButton btnDelete = new JButton("DELETE");
		panel_3.add(btnDelete);

		JButton btnUpdate = new JButton("Update");
		panel_3.add(btnUpdate);

		middlePanel = new JPanel();
		middlePanel.setBounds(0, 5, 500, 219);
		middlePanel.setLayout(new GridLayout(0, 2, 0, 0));

		lblIsbn = new JLabel("ISBN:");
		middlePanel.add(lblIsbn);

		txtIsbn = new JTextField();
		middlePanel.add(txtIsbn);
		txtIsbn.setColumns(10);

		lblTitle = new JLabel("Title:");
		middlePanel.add(lblTitle);

		txtTitle = new JTextField();
		middlePanel.add(txtTitle);
		txtTitle.setColumns(10);

		lblAvailability = new JLabel("Availability:");
		middlePanel.add(lblAvailability);

		txtAvailability = new JTextField("");
		middlePanel.add(txtAvailability);
		txtAvailability.setColumns(10);

		// middlePanel = new JPanel();
		// middlePanel.setLayout(new BorderLayout());
		mainList = createJList();
		mainList.setFixedCellWidth(70);
		mainScroll = new JScrollPane(mainList);

		initializeDefaultList();
		panel_2.add(mainScroll);

		panel_2.setLayout(null);
		panel_2.add(panel_3);
		panel_2.add(middlePanel);

		panel_4 = new JPanel();
		panel_4.setBounds(0, 282, 631, 275);
		panel_2.add(panel_4);
		panel_4.setLayout(new BorderLayout(0, 0));
		table = new JTable() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(model);
		TableColumnModel colModel = table.getColumnModel();
		colModel.getColumn(4).setPreferredWidth(200);
		colModel.getColumn(3).setPreferredWidth(50);
		colModel.getColumn(2).setPreferredWidth(50);
		colModel.getColumn(1).setPreferredWidth(100);
		colModel.getColumn(0).setPreferredWidth(75);
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(table);
		panel_4.add(jScrollPane);

		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = table.getSelectedRowCount();
				if (count == 1) {
					selectedRow = table.getSelectedRow();
					String memberavailability = (String) table.getValueAt(selectedRow, 0);
					model.removeRow(selectedRow);
					ci.deleteMember(memberavailability);
					selectedRow = -1;
					clearText();
				} else if (count > 1) {
					JOptionPane.showMessageDialog(frame, "Please select single row", "", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(frame, "There is no row to delete", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = table.getSelectedRowCount();
				if (count == 1) {
					selectedRow = table.getSelectedRow();
					String isbn = (String) table.getValueAt(selectedRow, 0);
					Book book = ci.getBookByISBN(isbn);
					book.addCopy();
					ci.saveBook(book);
					model.setValueAt(book.getCopies().length, selectedRow, 3);

					clearText();
					JOptionPane.showMessageDialog(frame, "Copy a book successfully.", "",
							JOptionPane.INFORMATION_MESSAGE);
					table.clearSelection();

				} else if (count > 1) {
					JOptionPane.showMessageDialog(frame, "Please select single a book.", "", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(frame, "There is no book to copy", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int count = table.getSelectedRowCount();
				if (count == 1) {
					selectedRow = table.getSelectedRow();
					Book book = ci.getBookByISBN((String) model.getValueAt(selectedRow, 0));
					txtTitle.setText(book.getTitle());
					txtIsbn.setText(book.getIsbn());
					txtAvailability.setText(book.getNumCopies() + "");
				} else {
					clearText();
				}
				super.mouseClicked(e);
			}
		});

		btnAdd.addActionListener((evt) -> {
			String availability = txtAvailability.getText();
			String isbn = txtIsbn.getText();
			String title = txtTitle.getText() == null ? "N/A" : txtTitle.getText();
			if (isbn.isEmpty() || availability.isEmpty() || title.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Invalid isbn or title or availability", "",
						JOptionPane.ERROR_MESSAGE);
				System.out.println("Invalid isbn or title or availability");
				return;
			}
			List<String> memberStrings = ci.allMemberIds();
			if (memberStrings.contains(availability)) {
				JOptionPane.showMessageDialog(frame, "Book exists", "", JOptionPane.ERROR_MESSAGE);
				System.out.println("Book exists");
				return;
			}
		});

		btnUpdate.addActionListener((evt) -> {
			String availability = txtAvailability.getText();
			String isbn = txtIsbn.getText();
			String title = txtTitle.getText() == null ? "N/A" : txtTitle.getText();
		});
	}

	void clearText() {
		txtTitle.setText("");
		txtIsbn.setText("");
		txtAvailability.setText("");
	}
}