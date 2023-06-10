package librarysystem;

import business.Address;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

public class ListLibraryMemberWindow extends JPanel implements LibWindow {
	private JLabel lblFirstName;
	private JTextField txtFieldFirstName;
	private JLabel lblMemberId;
	private JTextField txtState;
	private JLabel lblLastName;
	private JTextField txtZip;
	private JLabel lblState;
	private JLabel lblStreet;
	private JTextField txtFieldLastName;
	private JLabel lblTelephone;
	private JLabel lblZip;
	private JLabel lblCity;
	private JTextField txtCity;
	private JTextField txtFieldStreet;
	private JTextField txtFieldId;
	private JTextField txtTelephone;
	private JButton btnAdd;
	private JPanel middlePanel;
	private JFrame frame;
	private JTable table;
	ControllerInterface ci = new SystemController();
	private JPanel panel_4;
	private int selectedRow = -1;
	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) {
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// try {
	// ListLibraryMemberWindow window = new ListLibraryMemberWindow();
	// window.frame.setVisible(true);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// }

	/**
	 * Create the application.
	 */
	public ListLibraryMemberWindow() {
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

		JLabel lblNewLabel = new JLabel("Table of Library Members");
		panel.add(lblNewLabel);
		Object[] columnsObjects = { "ID", "First Name", "Last Name", "TEL", "Address" };
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columnsObjects);
		Collection<LibraryMember> members = ci.alLibraryMembers();
		for (LibraryMember member : members) {
			System.out.println(member.getMemberId());
			Object[] objects = { member.getMemberId(), member.getFirstName(), member.getLastName(),
					member.getTelephone(), member.getAddress() };
			model.addRow(objects);
		}

		JPanel panel_1 = new JPanel();
		add(panel_1, BorderLayout.SOUTH);

		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.CENTER);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(154, 231, 430, 39);

		JButton btnAdd = new JButton("ADD");
		panel_3.add(btnAdd);
		btnAdd.setHorizontalAlignment(SwingConstants.RIGHT);

		JButton btnDelete = new JButton("DELETE");
		panel_3.add(btnDelete);

		JButton btnUpdate = new JButton("Update");
		panel_3.add(btnUpdate);

		middlePanel = new JPanel();
		middlePanel.setBounds(5, 5, 460, 219);
		middlePanel.setLayout(new GridLayout(0, 2, 0, 0));
		lblMemberId = new JLabel("ID:");
		middlePanel.add(lblMemberId);

		txtFieldId = new JTextField();
		middlePanel.add(txtFieldId);
		txtFieldId.setColumns(10);

		lblFirstName = new JLabel("First Name:");
		middlePanel.add(lblFirstName);

		txtFieldFirstName = new JTextField();
		middlePanel.add(txtFieldFirstName);
		txtFieldFirstName.setColumns(10);

		lblLastName = new JLabel("Last Name:");
		middlePanel.add(lblLastName);

		txtFieldLastName = new JTextField();
		middlePanel.add(txtFieldLastName);
		txtFieldLastName.setColumns(10);

		lblStreet = new JLabel("Street:");
		middlePanel.add(lblStreet);

		txtFieldStreet = new JTextField();
		middlePanel.add(txtFieldStreet);
		txtFieldStreet.setColumns(10);

		lblCity = new JLabel("City:");
		middlePanel.add(lblCity);

		txtCity = new JTextField();
		middlePanel.add(txtCity);
		txtCity.setColumns(10);

		lblState = new JLabel("State:");
		middlePanel.add(lblState);

		txtState = new JTextField();
		middlePanel.add(txtState);
		txtState.setColumns(10);

		lblZip = new JLabel("Zip:");
		middlePanel.add(lblZip);

		txtZip = new JTextField();
		middlePanel.add(txtZip);
		txtZip.setColumns(10);

		lblTelephone = new JLabel("Telephone:");
		middlePanel.add(lblTelephone);

		txtTelephone = new JTextField();
		middlePanel.add(txtTelephone);
		txtTelephone.setColumns(10);
		panel_2.setLayout(null);
		panel_2.add(panel_3);
		panel_2.add(middlePanel);

		panel_4 = new JPanel();
		panel_4.setBounds(5, 282, 580, 275);
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
		colModel.getColumn(4).setPreferredWidth(300);
		colModel.getColumn(3).setPreferredWidth(200);
		colModel.getColumn(2).setPreferredWidth(100);
		colModel.getColumn(1).setPreferredWidth(100);
		colModel.getColumn(0).setPreferredWidth(50);
		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(table);
		panel_4.add(jScrollPane);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int count = table.getSelectedRowCount();
				if (count == 1) {
					selectedRow = table.getSelectedRow();

					String memberIdString = (String) table.getValueAt(selectedRow, 0);
					model.removeRow(selectedRow);
					ci.deleteMember(memberIdString);
					selectedRow = -1;
					clearText();
				} else if (count > 1) {
					JOptionPane.showMessageDialog(frame, "Please select single row", "", JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(frame, "There is no row to delete", "", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				int count = table.getSelectedRowCount();
				if (count == 1) {
					selectedRow = table.getSelectedRow();
					System.out.println(model.getValueAt(selectedRow, 0));
					LibraryMember member = ci.getLibraryMemberById((String) model.getValueAt(selectedRow, 0));
					txtCity.setText(member.getAddress().getCity());
					txtFieldFirstName.setText(member.getFirstName());
					txtFieldId.setText(member.getMemberId());
					txtFieldLastName.setText(member.getLastName());
					txtFieldStreet.setText(member.getAddress().getStreet());
					txtState.setText(member.getAddress().getState());
					txtTelephone.setText(member.getTelephone());
					txtZip.setText(member.getAddress().getZip());
				} else {
					clearText();
				}
				super.mouseClicked(e);

			}
		});

		btnAdd.addActionListener((evt) -> {
			String idString = txtFieldId.getText();
			String firstNameString = txtFieldFirstName.getText();
			String lastNameString = txtFieldLastName.getText();
			String telephoneString = txtTelephone.getText() == null ? "N/A" : txtTelephone.getText();
			String streetString = txtFieldStreet.getText() == null ? "N/A" : txtFieldStreet.getText();
			String cityString = txtCity.getText() == null ? "N/A" : txtCity.getText();
			String stateString = txtState.getText() == null ? "N/A" : txtState.getText();
			String zipString = txtZip.getText() == null ? "N/A" : txtZip.getText();
			if (firstNameString.isEmpty() || lastNameString.isEmpty() || idString.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Invalid id or first name or last name", "",
						JOptionPane.ERROR_MESSAGE);
				System.out.println("Invalid id or first name or last name");
				return;
			}
			List<String> memberStrings = ci.allMemberIds();
			if (memberStrings.contains(idString)) {
				JOptionPane.showMessageDialog(frame, "exist member id", "", JOptionPane.ERROR_MESSAGE);
				System.out.println("exist member id");
				return;
			}
			Address newAddress = new Address(streetString, cityString, stateString, zipString);
			LibraryMember member = new LibraryMember(idString, firstNameString, lastNameString, telephoneString,
					newAddress);
			ci.saveMember(member);
			JOptionPane.showMessageDialog(frame, "Add member successfully", "", JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(System.getProperty("user.dir") + "/src/librarysystem/success.png"));
			Object[] objects = { member.getMemberId(), member.getFirstName(), member.getLastName(),
					member.getTelephone(), member.getAddress() };
			model.addRow(objects);

		});

		btnUpdate.addActionListener((evt) -> {
			String idString = txtFieldId.getText();
			String firstNameString = txtFieldFirstName.getText();
			String lastNameString = txtFieldLastName.getText();
			String telephoneString = txtTelephone.getText() == null ? "N/A" : txtTelephone.getText();
			String streetString = txtFieldStreet.getText() == null ? "N/A" : txtFieldStreet.getText();
			String cityString = txtCity.getText() == null ? "N/A" : txtCity.getText();
			String stateString = txtState.getText() == null ? "N/A" : txtState.getText();
			String zipString = txtZip.getText() == null ? "N/A" : txtZip.getText();
			if (firstNameString.isEmpty() || lastNameString.isEmpty() || idString.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "Invalid id or first name or last name", "",
						JOptionPane.ERROR_MESSAGE);
				System.out.println("Invalid id or first name or last name");
				return;
			}

			Address newAddress = new Address(streetString, cityString, stateString, zipString);
			LibraryMember member = new LibraryMember(idString, firstNameString, lastNameString, telephoneString,
					newAddress);
			ci.saveMember(member);
			JOptionPane.showMessageDialog(frame, "Update member successfully", "", JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(System.getProperty("user.dir") + "/src/librarysystem/success.png"));
			model.setValueAt(member.getMemberId(), selectedRow, 0);
			model.setValueAt(member.getFirstName(), selectedRow, 1);
			model.setValueAt(member.getLastName(), selectedRow, 2);
			model.setValueAt(member.getTelephone(), selectedRow, 3);
			model.setValueAt(member.getAddress(), selectedRow, 4);
			clearText();

		});
	}

	void clearText() {
		txtCity.setText("");
		txtFieldFirstName.setText("");
		txtFieldId.setText("");
		txtFieldLastName.setText("");
		txtFieldStreet.setText("");
		txtState.setText("");
		txtTelephone.setText("");
		txtZip.setText("");
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
