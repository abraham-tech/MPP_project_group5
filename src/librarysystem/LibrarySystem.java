package librarysystem;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import business.ControllerInterface;
import business.SystemController;
import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class LibrarySystem extends JFrame implements LibWindow {
	ControllerInterface ci = new SystemController();
	public final static LibrarySystem INSTANCE = new LibrarySystem();
	JPanel mainPanel;
	JPanel menuPanel;
	JPanel contentPanel;
	JMenuBar menuBar;
	JMenu options;
	JMenuItem login, logout;
	JMenuItem addBook, addMember, checkOutBook, allMemberIds;
	JSeparator separator;
	String pathToImage;
	JTextField username;
	JTextField password;
	JSplitPane splitPane;
	JButton booksButton;
	JButton usersButton;
	JButton logoutButton;
	JButton checkoutButton;

	private User loggedInUser;

	public User getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(User loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	private boolean isInitialized = false;

	private static LibWindow[] allWindows = {
			LibrarySystem.INSTANCE,
			LoginWindow.INSTANCE,
	};

	public static void hideAllWindows() {
		for (LibWindow frame : allWindows) {
			frame.setVisible(false);
		}
	}

	private LibrarySystem() {
	}

	public void init() {
		formatContentPane();
		setPathToImage();
		insertSplashImage();

		// createMenus();
		// pack();
		initLoginForm();
		setSize(810, 500);
		isInitialized = true;
	}

	private void formatContentPane() {
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(15, 1));
		menuPanel.setBackground(Color.LIGHT_GRAY);
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(1, 1));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, contentPanel);
		splitPane.setDividerLocation(150);
		getContentPane().add(splitPane);
	}

	private void setPathToImage() {
		String currDirectory = System.getProperty("user.dir");
		// for mac use this
		pathToImage = currDirectory + "/src/librarysystem/library.jpg";
		// for windows use this
		// pathToImage = currDirectory+"\\src\\librarysystem\\ßlibrary.jpg";
	}

	private void insertSplashImage() {
		ImageIcon image = new ImageIcon(pathToImage);
		contentPanel.add(new JLabel(image));
	}

	/*
	 * private void addMenuItems() {
	 * options = new JMenu("Options");
	 * menuBar.add(options);
	 * login = new JMenuItem("Login");
	 * login.addActionListener(new LoginListener());
	 * allBookIds = new JMenuItem("All Book Ids");
	 * allBookIds.addActionListener(new AllBookIdsListener());
	 * allMemberIds = new JMenuItem("All Member Ids");
	 * allMemberIds.addActionListener(new AllMemberIdsListener());
	 * options.add(login);
	 * options.add(allBookIds);
	 * options.add(allMemberIds);
	 * }
	 */

	public void initLoginForm() {
		clearMenu();
		JLabel loginLabel = new JLabel("Sign In:");
		Util.adjustLabelFont(loginLabel, Color.BLUE.darker(), true);
		menuPanel.add(loginLabel);
		JSeparator s = new JSeparator();
		s.setOrientation(SwingConstants.HORIZONTAL);
		menuPanel.add(s);
		username = new JTextField(45);
		username.setMaximumSize(username.getPreferredSize());
		JLabel uLabel = new JLabel("Username:");
		uLabel.setFont(Util.makeSmallFont(uLabel.getFont()));
		menuPanel.add(uLabel);
		menuPanel.add(username);
		password = new JTextField(45);
		password.setMaximumSize(password.getPreferredSize());
		JLabel pLabel = new JLabel("Password:");
		pLabel.setFont(Util.makeSmallFont(pLabel.getFont()));
		menuPanel.add(pLabel);
		menuPanel.add(password);
		JButton loginButton = new JButton("Sign In");
		addLoginButtonListener(loginButton);
		menuPanel.add(loginButton);
		menuPanel.setAlignmentY(Component.LEFT_ALIGNMENT);
	}

	private void clearMenu() {
		menuPanel.removeAll();
		menuPanel.revalidate();
		menuPanel.repaint();
	}

	public void initAdminMenu() {
		clearMenu();
		booksButton = new JButton("Books");
		usersButton = new JButton("Users");

		logoutButton = new JButton("Sign Out");
		menuPanel.add(booksButton);
		menuPanel.add(usersButton);
		JSeparator s = new JSeparator();
		s.setOrientation(SwingConstants.HORIZONTAL);
		menuPanel.add(s);
		menuPanel.add(logoutButton);
	}

	public void initLibrarianMenu() {
		clearMenu();
		JButton checkoutButton = new JButton("Checkout");
		menuPanel.add(checkoutButton);
		logoutButton = new JButton("Sign Out");
		JSeparator s = new JSeparator();
		s.setOrientation(SwingConstants.HORIZONTAL);
		menuPanel.add(s);
		menuPanel.add(logoutButton);
	}

	public void initBothMenu() {
		clearMenu();
		booksButton = new JButton("Books");
		usersButton = new JButton("Users");
		checkoutButton = new JButton("Checkout");
		menuPanel.add(booksButton);
		menuPanel.add(usersButton);
		menuPanel.add(checkoutButton);
		JButton logoutButton = new JButton("Sign Out");
		logoutButton.addActionListener(new LogoutListener());
		JSeparator s = new JSeparator();
		s.setOrientation(SwingConstants.HORIZONTAL);
		menuPanel.add(s);
		menuPanel.add(logoutButton);
	}

	private void addLoginButtonListener(JButton butn) {
		butn.addActionListener(evt -> {
			String userId = username.getText();
			String userPass = password.getText();
			if (userId == null || userId.isBlank() || userPass == null || userPass.isBlank()) {
				JOptionPane.showMessageDialog(this, "Invalid username or password", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			DataAccess da = new DataAccessFacade();
			Map<String, User> users = da.readUserMap();
			if (!users.containsKey(userId)) {
				JOptionPane.showMessageDialog(this, "Invalid username", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			User user = users.get(userId);
			if (!userPass.equals(user.getPassword())) {
				JOptionPane.showMessageDialog(this, "Invalid password", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			LibrarySystem.INSTANCE.setLoggedInUser(user);
			if (user.getAuthorization() == Auth.ADMIN) {
				initAdminMenu();
			} else if (user.getAuthorization() == Auth.LIBRARIAN) {
				initLibrarianMenu();
			} else if (user.getAuthorization() == Auth.BOTH) {
				initBothMenu();
			}
			if (usersButton != null) {
				usersButton.addActionListener((evt1) -> {
					if (!(contentPanel instanceof ListLibraryMemberWindow)) {
						contentPanel = new ListLibraryMemberWindow();
						splitPane.setRightComponent(contentPanel);
					}
				});
			}
			if (booksButton != null) {
				booksButton.addActionListener((evt1) -> {
					if (!(contentPanel instanceof ListLibraryBookWindow)) {
						contentPanel = new ListLibraryBookWindow();
						splitPane.setRightComponent(contentPanel);
					}
					// contentPanel = new JPanel();
					// insertSplashImage();
					// splitPane.setRightComponent(contentPanel);
				});
			}

			logoutButton.addActionListener(new LogoutListener());
		});
	}

	public void refreshMenuByUserRole() {
		if (loggedInUser != null) {
			separator.setVisible(true);
			logout.setVisible(true);
			login.setVisible(false);
			checkOutBook.setVisible(false);
			addMember.setVisible(false);
			addBook.setVisible(false);
			allMemberIds.setVisible(false);
			dataaccess.Auth auth = loggedInUser.getAuthorization();
			switch (auth) {
				case LIBRARIAN: {
					checkOutBook.setVisible(true);
					break;
				}
				case ADMIN:
					addMember.setVisible(true);
					addBook.setVisible(true);
					allMemberIds.setVisible(true);
					break;
				case BOTH:
					addMember.setVisible(true);
					addBook.setVisible(true);
					allMemberIds.setVisible(true);
					checkOutBook.setVisible(true);
					break;
				default:
					break;
			}

		} else {
			login.setVisible(true);
			separator.setVisible(false);
			checkOutBook.setVisible(false);
			addMember.setVisible(false);
			addBook.setVisible(false);
			logout.setVisible(false);
			allMemberIds.setVisible(false);
		}
	}

	class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			LoginWindow.INSTANCE.init();
			Util.centerFrameOnDesktop(LoginWindow.INSTANCE);
			LoginWindow.INSTANCE.setVisible(true);

		}

	}

	class LogoutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			loggedInUser = null;
			initLoginForm();
			contentPanel = new JPanel();
			insertSplashImage();
			splitPane.setRightComponent(contentPanel);
		}

	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void isInitialized(boolean val) {
		// TODO Auto-generated method stub

	}

}
