package librarysystem;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Address;
import business.ControllerInterface;
import business.LibraryMember;
import business.SystemController;

public class AddNewMemberWindow extends JFrame implements LibWindow{

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
	private JButton btnClear;
	private JPanel middlePanel;
	private JPanel topPanel;
	private JPanel mainPanel;
	ControllerInterface ci = new SystemController();

	public final static AddNewMemberWindow INSTANCE =new AddNewMemberWindow();
	private boolean isInitialized = false;
	
	/**
	 * Create the application.
	 */
	public AddNewMemberWindow() {}

	/**
	 * Initialize the contents of the frame.
	 */
	
	@Override
	public void init() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		defineTopPanel();
		defineMiddlePanel();
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(middlePanel, BorderLayout.CENTER);
		getContentPane().add(mainPanel);
		isInitialized = true;
	}
	
	
	public void defineMiddlePanel() {
		middlePanel = new JPanel();
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
		
		btnAdd = new JButton("ADD");
		btnAdd.addActionListener((evt) -> {
			String idString = txtFieldId.getText();
			String firstNameString = txtFieldFirstName.getText();
			String lastNameString = txtFieldLastName.getText();
			String telephoneString = txtTelephone.getText() == null ? "N/A" : txtTelephone.getText();
			String streetString = txtFieldStreet.getText() == null ? "N/A" : txtTelephone.getText();
			String cityString = txtCity.getText() == null ? "N/A" : txtCity.getText();
			String stateString = txtState.getText() == null ? "N/A" : txtState.getText();
			String zipString = txtZip.getText() == null ? "N/A" : txtZip.getText();
			if (firstNameString.isEmpty() || lastNameString.isEmpty() || idString.isEmpty()) {
				JOptionPane.showMessageDialog(this,"Invalid id or first name or last name", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			List<String> memberStrings = ci.allMemberIds();
			if (memberStrings.contains(idString)) {
				JOptionPane.showMessageDialog(this,"exist member id", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Address newAddress = new Address(streetString, cityString, stateString, zipString);
			ci.saveMember(new LibraryMember(idString, firstNameString, lastNameString, telephoneString, newAddress));
		});
		middlePanel.add(btnAdd);
		
		btnClear = new JButton("BACK TO MAIN MENU");
		btnClear.addActionListener((evt) -> {
			LibrarySystem.hideAllWindows();
			LibrarySystem.INSTANCE.setVisible(true);
			});
		middlePanel.add(btnClear);
	}
	
	public void defineTopPanel() {
		topPanel = new JPanel();
		JLabel AllIDsLabel = new JLabel("ADD NEW MEMBER");
		Util.adjustLabelFont(AllIDsLabel, Util.DARK_BLUE, true);
		topPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		topPanel.add(AllIDsLabel);
	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return isInitialized;
	}

	@Override
	public void isInitialized(boolean val) {
		isInitialized = val;
		
	}
}
