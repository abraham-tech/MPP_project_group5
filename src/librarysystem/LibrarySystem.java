package librarysystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
	public final static LibrarySystem INSTANCE =new LibrarySystem();
	JPanel mainPanel;
	JPanel menuPanel;
	JPanel contentPanel;
	JMenuBar menuBar;
    JMenu options;
    JMenuItem login, logout;
    JMenuItem addBook, addMember, checkOutBook;
    JSeparator separator;
    String pathToImage;
    JTextField username;
    JTextField password;
    
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
		AllMemberIdsWindow.INSTANCE,	
		AllBookIdsWindow.INSTANCE
	};
    	
	public static void hideAllWindows() {		
		for(LibWindow frame: allWindows) {
			frame.setVisible(false);			
		}
	}
     
    private LibrarySystem() {}
    
    public void init() {
    	formatContentPane();
    	setPathToImage();
    	insertSplashImage();
		
		//createMenus();
		//pack();
    	initLoginForm();
		setSize(810,500);
		isInitialized = true;
    }
    
    private void formatContentPane() {
		menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(15, 1) );
		menuPanel.setBackground(Color.LIGHT_GRAY);
		contentPanel = new JPanel();
		contentPanel.setLayout(new GridLayout(1,1));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, contentPanel);
	    splitPane.setDividerLocation(150);
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
    private void createMenus() {
    	menuBar = new JMenuBar();
		menuBar.setBorder(BorderFactory.createRaisedBevelBorder());
		addMenuItems();
		setJMenuBar(menuBar);		
    }
    /*
    private void addMenuItems() {
       options = new JMenu("Options");  
 	   menuBar.add(options);
 	   login = new JMenuItem("Login");
 	   login.addActionListener(new LoginListener());
 	   allBookIds = new JMenuItem("All Book Ids");
 	   allBookIds.addActionListener(new AllBookIdsListener());
 	   allMemberIds = new JMenuItem("All Member Ids");
 	   allMemberIds.addActionListener(new AllMemberIdsListener());
 	   options.add(login);
 	   options.add(allBookIds);
 	   options.add(allMemberIds);
    }
    */
    
    
    
    private void addMenuItems() {
       options = new JMenu("Options");  
       menuBar.add(options);
       login = new JMenuItem("Login"); 
  	   login.addActionListener(new LoginListener());
	   options.add(login);
	   addBook = new JMenuItem("Add Book"); 
	   addMember = new JMenuItem("Add member"); 
	   checkOutBook = new JMenuItem("Checkout book");
	   options.add(addBook);
	   options.add(addMember);
	   options.add(checkOutBook);	   
	   logout = new JMenuItem("Logout"); 
  	   logout.addActionListener(new LogoutListener());
  	   separator = new JSeparator(SwingConstants.HORIZONTAL);
  	   options.add(separator);
	   options.add(logout);	   
	  
	   refreshMenuByUserRole();	   
     }
    
    public void initLoginForm() {
    	clearMenu();
    	JLabel loginLabel = new JLabel("Sign In:");
		Util.adjustLabelFont(loginLabel, Color.BLUE.darker(), true);
		menuPanel.add(loginLabel);
		JSeparator s = new JSeparator();
        s.setOrientation(SwingConstants.HORIZONTAL);
        menuPanel.add(s);
		username = new JTextField(45);
		username.setMaximumSize( username.getPreferredSize() );
		JLabel uLabel = new JLabel("Username:");
		uLabel.setFont(Util.makeSmallFont(uLabel.getFont()));
		menuPanel.add(uLabel);
		menuPanel.add(username);
		password = new JTextField(45);
		password.setMaximumSize( password.getPreferredSize() );
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
		JButton booksButton = new JButton("Books");
		JButton usersButton = new JButton("Users");
		JButton logoutButton = new JButton("Sign Out");
		logoutButton.addActionListener(new LogoutListener());
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
		JButton logoutButton = new JButton("Sign Out");
		logoutButton.addActionListener(new LogoutListener());
		JSeparator s = new JSeparator();
        s.setOrientation(SwingConstants.HORIZONTAL);
        menuPanel.add(s);
		menuPanel.add(logoutButton);
    }
    
    public void initBothMenu() {
    	clearMenu();
    	JButton booksButton = new JButton("Books");
		JButton usersButton = new JButton("Users");
		JButton checkoutButton = new JButton("Checkout");
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
			if(userId == null || userId.isBlank() || userPass == null || userPass.isBlank()) {
				JOptionPane.showMessageDialog(this,"Invalid username or password", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			DataAccess da = new DataAccessFacade();
			Map<String, User> users = da.readUserMap();
			if(!users.containsKey(userId)) {
				JOptionPane.showMessageDialog(this,"Invalid username", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			User user = users.get(userId);
			if(!userPass.equals(user.getPassword())) {
				JOptionPane.showMessageDialog(this,"Invalid password", "", JOptionPane.ERROR_MESSAGE);
				return;
			}
			LibrarySystem.INSTANCE.setLoggedInUser(user);
			if(user.getAuthorization() == Auth.ADMIN) {
				initAdminMenu();
			} else if(user.getAuthorization() == Auth.LIBRARIAN) {
				initLibrarianMenu();
			}
		});
	}
    
    public void refreshMenuByUserRole() {
    	if(loggedInUser != null) {	
    		separator.setVisible(true);
    		logout.setVisible(true);
	 		login.setVisible(false);
	 		checkOutBook.setVisible(false);
		    addMember.setVisible(false);
		    addBook.setVisible(false);
		    dataaccess.Auth auth = loggedInUser.getAuthorization();
	 		switch (auth) {
	 			case LIBRARIAN: {
	 				checkOutBook.setVisible(true);
	 		        break;
	 			}
	 		    case ADMIN:
	 		        addMember.setVisible(true);
	 		        addBook.setVisible(true);
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
		}
    	
    }
    class AllBookIdsListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			AllBookIdsWindow.INSTANCE.init();
			
			List<String> ids = ci.allBookIds();
			Collections.sort(ids);
			StringBuilder sb = new StringBuilder();
			for(String s: ids) {
				sb.append(s + "\n");
			}
			System.out.println(sb.toString());
			AllBookIdsWindow.INSTANCE.setData(sb.toString());
			AllBookIdsWindow.INSTANCE.pack();
			//AllBookIdsWindow.INSTANCE.setSize(660,500);
			Util.centerFrameOnDesktop(AllBookIdsWindow.INSTANCE);
			AllBookIdsWindow.INSTANCE.setVisible(true);
			
		}
    	
    }
    
    class AllMemberIdsListener implements ActionListener {

    	@Override
		public void actionPerformed(ActionEvent e) {
			LibrarySystem.hideAllWindows();
			AllMemberIdsWindow.INSTANCE.init();
			AllMemberIdsWindow.INSTANCE.pack();
			AllMemberIdsWindow.INSTANCE.setVisible(true);
			
			
			LibrarySystem.hideAllWindows();
			AllBookIdsWindow.INSTANCE.init();
			
			List<String> ids = ci.allMemberIds();
			Collections.sort(ids);
			StringBuilder sb = new StringBuilder();
			for(String s: ids) {
				sb.append(s + "\n");
			}
			System.out.println(sb.toString());
			AllMemberIdsWindow.INSTANCE.setData(sb.toString());
			AllMemberIdsWindow.INSTANCE.pack();
			//AllMemberIdsWindow.INSTANCE.setSize(660,500);
			Util.centerFrameOnDesktop(AllMemberIdsWindow.INSTANCE);
			AllMemberIdsWindow.INSTANCE.setVisible(true);
			
			
		}
    	
    }

	@Override
	public boolean isInitialized() {
		return isInitialized;
	}


	@Override
	public void isInitialized(boolean val) {
		isInitialized =val;
		
	}
    
}
