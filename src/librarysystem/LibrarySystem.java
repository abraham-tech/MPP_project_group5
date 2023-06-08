package librarysystem;

import business.ControllerInterface;
import business.SystemController;
import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class LibrarySystem extends JFrame implements LibWindow {
	ControllerInterface ci = new SystemController();
	public final static LibrarySystem INSTANCE = new LibrarySystem();
	JPanel mainPanel;
	JPanel menuPanel;
	JPanel contentPanel;
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
     
    private LibrarySystem() {}
    
    public void init() {
    	formatContentPane();
    	setPathToImage();
    	insertSplashImage();
		
    	initLoginForm();
		setSize(810,500);
    }
    
    private void formatContentPane() {
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(15, 1));
		menuPanel.setBackground(Color.LIGHT_GRAY);
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(1, 1));
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, contentPanel);
	    splitPane.setDividerLocation(100);
		getContentPane().add(splitPane);
	}
    
    private void setPathToImage() {
    	String currDirectory = System.getProperty("user.dir");
    	// for mac use this
    	pathToImage = currDirectory+"/src/librarysystem/library.jpg";
    	// for windows use this 
    	// pathToImage = currDirectory+"\\src\\librarysystem\\ßlibrary.jpg";
    }

	private void insertSplashImage() {
		ImageIcon image = new ImageIcon(pathToImage);
		contentPanel.add(new JLabel(image));
	}


	public void initLoginForm() {
		clearMenu();
		JLabel loginLabel = new JLabel("Sign In:");
		splitPane.setDividerLocation(100);
		getContentPane().add(splitPane);
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
		password = new JPasswordField(45);
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
    
    private void clearContentPanel() {
    	contentPanel.removeAll();
    	contentPanel.revalidate();
    	contentPanel.repaint();
    }
    
    public void initAdminMenu() {
    	clearMenu();
		booksButton = new JButton("Books");
		usersButton = new JButton("Users");		
		menuPanel.add(booksButton);
		menuPanel.add(usersButton);
        addLogoutButton();
    }

	public void initLibrarianMenu() {
		clearMenu();
		JButton checkoutButton = new JButton("Checkout");
		checkoutButton = new JButton("Checkout");
		checkoutButton.addActionListener(e -> {
			if (!(contentPanel instanceof BookCheckoutWindow)) {
				contentPanel = new BookCheckoutWindow();
				splitPane.setRightComponent(contentPanel);
			}
		});
		menuPanel.add(checkoutButton);
        addLogoutButton();
    }

	public void initBothMenu() {
		clearMenu();
		booksButton = new JButton("Books");
		usersButton = new JButton("Users");
		checkoutButton = new JButton("Checkout");
		checkoutButton.addActionListener(e -> {
			if (!(contentPanel instanceof BookCheckoutWindow)) {
				contentPanel = new BookCheckoutWindow();
				splitPane.setRightComponent(contentPanel);
			}
		});
		menuPanel.add(booksButton);
		menuPanel.add(usersButton);
		menuPanel.add(checkoutButton);

        addLogoutButton();
    }
    
    private void addLogoutButton() {
    	logoutButton = new JButton("Sign Out");
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
		});
	}  
    

	class LogoutListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			loggedInUser = null;
			clearMenu();
			clearContentPanel();
			insertSplashImage();
			initLoginForm();	
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
