package librarysystem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import dataaccess.Auth;


public class MenuWindow extends JPanel implements LibWindow {
	public static final MenuWindow INSTANCE = new MenuWindow();
	
	private boolean isInitialized = false;
	private JButton booksButton, usersButton, checkoutButton, logoutButton; 
	private Auth role;
	
	public void init() {
		clearContent();
		if(role == Auth.ADMIN) {
			booksButton = new JButton("Books");
			booksButton.addActionListener(e -> LibrarySystem.INSTANCE.openListLibraryBookWindow());
	        usersButton = new JButton("Users");
	        usersButton.addActionListener(e -> LibrarySystem.INSTANCE.openListLibraryMemberWindow());
	        add(booksButton);
	        add(usersButton);
		} else if(role == Auth.LIBRARIAN) {
			checkoutButton = new JButton("Checkout");
			checkoutButton.addActionListener(e -> LibrarySystem.INSTANCE.openBookCheckoutWindow());
			add(checkoutButton);
		} else if(role == Auth.BOTH) {
			booksButton = new JButton("Books");
			booksButton.addActionListener(e -> LibrarySystem.INSTANCE.openListLibraryBookWindow());
	        usersButton = new JButton("Users");
	        usersButton.addActionListener(e -> LibrarySystem.INSTANCE.openListLibraryMemberWindow());
	        checkoutButton = new JButton("Checkout");
	        checkoutButton.addActionListener(e -> LibrarySystem.INSTANCE.openBookCheckoutWindow());
	        add(booksButton);
	        add(usersButton);
	        add(checkoutButton);
		}
		
		
		logoutButton = new JButton("Sign Out");
        logoutButton.addActionListener(new LogoutListener());
        JSeparator s = new JSeparator();
        s.setOrientation(SwingConstants.HORIZONTAL);
        add(s);
        add(logoutButton);
        isInitialized(true);
    }
	
	public void clearContent() {
		removeAll();
        revalidate();
        repaint();
	}

    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        	LibrarySystem.INSTANCE.setLoggedInUser(null);
        	LibrarySystem.INSTANCE.init();
        }
    }
	
	public boolean isInitialized() {
		return isInitialized;
	}
	public void isInitialized(boolean val) {
		isInitialized = val;
	}
	public void setAuth(Auth role) {
		this.role = role;
    	init();
	}
    private MenuWindow () {
    }	
}
