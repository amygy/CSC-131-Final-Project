import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private AuthService authService = new AuthService();
    private LibraryService libraryService = new LibraryService();

    private User currentUser = null;   // normal user
    private Staff currentStaff = null; // staff user

    public MainFrame() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Initial screens
        cardPanel.add(new WelcomePanel(), "WELCOME");
        cardPanel.add(new LoginPanel(), "LOGIN");
        cardPanel.add(new RegisterUserPanel(), "REGISTER_USER");
        cardPanel.add(new RegisterStaffPanel(), "REGISTER_STAFF");

        add(cardPanel);
        showScreen("WELCOME");
    }

    private void showScreen(String name) {
        cardLayout.show(cardPanel, name);
    }

    // ==================== Panels ====================

    private class WelcomePanel extends JPanel {
        public WelcomePanel() {
            setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("Library Management System", SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
            add(title, BorderLayout.NORTH);

            JPanel buttons = new JPanel(new GridLayout(4, 1, 10, 10));
            JButton createUserBtn = new JButton("Create User Account");
            JButton createStaffBtn = new JButton("Create Staff Account");
            JButton loginBtn = new JButton("Login");
            JButton exitBtn = new JButton("Exit");

            buttons.add(createUserBtn);
            buttons.add(createStaffBtn);
            buttons.add(loginBtn);
            buttons.add(exitBtn);

            add(buttons, BorderLayout.CENTER);

            createUserBtn.addActionListener(e -> showScreen("REGISTER_USER"));
            createStaffBtn.addActionListener(e -> showScreen("REGISTER_STAFF"));
            loginBtn.addActionListener(e -> showScreen("LOGIN"));
            exitBtn.addActionListener(e -> System.exit(0));
        }
    }

    private class LoginPanel extends JPanel {
        public LoginPanel() {
            setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("Login", SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            add(title, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField emailField = new JTextField();
            JPasswordField passwordField = new JPasswordField();

            form.add(new JLabel("Email:"));
            form.add(emailField);
            form.add(new JLabel("Password:"));
            form.add(passwordField);

            JButton loginBtn = new JButton("Login");
            JButton backBtn = new JButton("Back");
            form.add(loginBtn);
            form.add(backBtn);

            add(form, BorderLayout.CENTER);

            loginBtn.addActionListener(e -> {
                String email = emailField.getText().trim();
                String pwd = new String(passwordField.getPassword());
                User u = authService.login(email, pwd);
                if (u == null) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid email or password",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                } else if (u instanceof Staff) {
                    currentStaff = (Staff) u;
                    currentUser = null;
                    cardPanel.add(new StaffMenuPanel(), "STAFF_MENU");
                    showScreen("STAFF_MENU");
                } else { // normal user
                    currentUser = u;
                    currentStaff = null;
                    cardPanel.add(new UserMenuPanel(), "USER_MENU");
                    showScreen("USER_MENU");
                }
            });

            backBtn.addActionListener(e -> showScreen("WELCOME"));
        }
    }

    private class RegisterUserPanel extends JPanel {
        public RegisterUserPanel() {
            setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("Create User Account", SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            add(title, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JPasswordField passField = new JPasswordField();
            JPasswordField confirmField = new JPasswordField();

            form.add(new JLabel("Full Name:"));
            form.add(nameField);
            form.add(new JLabel("Email:"));
            form.add(emailField);
            form.add(new JLabel("Password:"));
            form.add(passField);
            form.add(new JLabel("Confirm Password:"));
            form.add(confirmField);

            JButton createBtn = new JButton("Create Account");
            JButton backBtn = new JButton("Back");
            form.add(createBtn);
            form.add(backBtn);

            add(form, BorderLayout.CENTER);

            createBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String pwd = new String(passField.getPassword());
                String confirm = new String(confirmField.getPassword());

                if (name.isEmpty() || email.isEmpty() || pwd.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "All fields are required.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!pwd.equals(confirm)) {
                    JOptionPane.showMessageDialog(this,
                            "Passwords do not match.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                User u = authService.registerUser(name, email, pwd);
                if (u == null) {
                    JOptionPane.showMessageDialog(this,
                            "Email already in use.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "User account created. You can now log in.");
                    showScreen("LOGIN");
                }
            });

            backBtn.addActionListener(e -> showScreen("WELCOME"));
        }
    }

    private class RegisterStaffPanel extends JPanel {
        public RegisterStaffPanel() {
            setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("Create Staff Account", SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            add(title, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
            JTextField nameField = new JTextField();
            JTextField emailField = new JTextField();
            JPasswordField passField = new JPasswordField();
            JPasswordField confirmField = new JPasswordField();
            JTextField workIdField = new JTextField();

            form.add(new JLabel("Full Name:"));
            form.add(nameField);
            form.add(new JLabel("Email:"));
            form.add(emailField);
            form.add(new JLabel("Password:"));
            form.add(passField);
            form.add(new JLabel("Confirm Password:"));
            form.add(confirmField);
            form.add(new JLabel("Work ID (5 digits):"));
            form.add(workIdField);

            JButton createBtn = new JButton("Create Staff Account");
            JButton backBtn = new JButton("Back");
            form.add(createBtn);
            form.add(backBtn);

            add(form, BorderLayout.CENTER);

            createBtn.addActionListener(e -> {
                String name = nameField.getText().trim();
                String email = emailField.getText().trim();
                String pwd = new String(passField.getPassword());
                String confirm = new String(confirmField.getPassword());
                String workId = workIdField.getText().trim();

                if (name.isEmpty() || email.isEmpty() || pwd.isEmpty() || workId.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "All fields are required.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!pwd.equals(confirm)) {
                    JOptionPane.showMessageDialog(this,
                            "Passwords do not match.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Staff s = authService.registerStaff(name, email, pwd, workId);
                if (s == null) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid work ID (must be 5 digits) or email already in use.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Staff account created. You can now log in.");
                    showScreen("LOGIN");
                }
            });

            backBtn.addActionListener(e -> showScreen("WELCOME"));
        }
    }

    private class UserMenuPanel extends JPanel {
        public UserMenuPanel() {
            setLayout(new BorderLayout(10, 10));
            JLabel title = new JLabel("User Menu - " + currentUser.getName(), SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            add(title, BorderLayout.NORTH);

            JPanel buttons = new JPanel(new GridLayout(2, 1, 10, 10));
            JButton searchBtn = new JButton("Search / Request / Checkout Books");
            JButton logoutBtn = new JButton("Logout");

            buttons.add(searchBtn);
            buttons.add(logoutBtn);

            add(buttons, BorderLayout.CENTER);

            searchBtn.addActionListener(e -> {
                cardPanel.add(new SearchBooksPanel(true), "SEARCH_USER");
                showScreen("SEARCH_USER");
            });

            logoutBtn.addActionListener(e -> {
                currentUser = null;
                showScreen("WELCOME");
            });
        }
    }

    private class StaffMenuPanel extends JPanel {
        public StaffMenuPanel() {
            setLayout(new BorderLayout(10, 10));
            JLabel title = new JLabel("Staff Menu - " + currentStaff.getName(), SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            add(title, BorderLayout.NORTH);

            JPanel buttons = new JPanel(new GridLayout(3, 1, 10, 10));
            JButton searchBtn = new JButton("Search Books");
            JButton manageBtn = new JButton("Add / Delete Books");
            JButton logoutBtn = new JButton("Logout");

            buttons.add(searchBtn);
            buttons.add(manageBtn);
            buttons.add(logoutBtn);

            add(buttons, BorderLayout.CENTER);

            searchBtn.addActionListener(e -> {
                cardPanel.add(new SearchBooksPanel(false), "SEARCH_STAFF");
                showScreen("SEARCH_STAFF");
            });

            manageBtn.addActionListener(e -> {
                cardPanel.add(new ManageBooksPanel(), "MANAGE_BOOKS");
                showScreen("MANAGE_BOOKS");
            });

            logoutBtn.addActionListener(e -> {
                currentStaff = null;
                showScreen("WELCOME");
            });
        }
    }

    private class SearchBooksPanel extends JPanel {

        private DefaultTableModel tableModel;
        private boolean isUserView;

        public SearchBooksPanel(boolean isUserView) {
            this.isUserView = isUserView;

            setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("Search Books", SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            add(title, BorderLayout.NORTH);

            JPanel top = new JPanel(new FlowLayout());
            JTextField searchField = new JTextField(30);
            JButton searchBtn = new JButton("Search");
            JButton backBtn = new JButton("Back");

            top.add(new JLabel("Search by title, author, or ISBN:"));
            top.add(searchField);
            top.add(searchBtn);
            top.add(backBtn);

            add(top, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(
                    new Object[]{"ISBN", "Title", "Author", "Available"}, 0
            );
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout());
            JButton checkoutBtn = new JButton("Check Out");
            JButton requestBtn = new JButton("Request Book");

            if (!isUserView) {
                checkoutBtn.setEnabled(false);
                requestBtn.setEnabled(false);
            }

            bottom.add(checkoutBtn);
            bottom.add(requestBtn);
            add(bottom, BorderLayout.SOUTH);

            // initial load
            loadBooks(libraryService.getAllBooks());

            searchBtn.addActionListener(e -> {
                String query = searchField.getText().trim();
                List<Book> results = libraryService.searchBooks(query);
                loadBooks(results);
            });

            backBtn.addActionListener(e -> {
                if (isUserView) {
                    showScreen("USER_MENU");
                } else {
                    showScreen("STAFF_MENU");
                }
            });

            checkoutBtn.addActionListener(e -> {
                if (!isUserView || currentUser == null) {
                    JOptionPane.showMessageDialog(this,
                            "Only users can check out books.");
                    return;
                }
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Select a book first.");
                    return;
                }
                String isbn = (String) tableModel.getValueAt(row, 0);
                Book selected = findBookByIsbn(isbn);
                if (selected == null) {
                    JOptionPane.showMessageDialog(this,
                            "Book not found.");
                    return;
                }
                boolean ok = libraryService.checkoutBook(currentUser, selected);
                if (!ok) {
                    JOptionPane.showMessageDialog(this,
                            "No copies available. You can request this book instead.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Book checked out successfully.");
                    String q = searchField.getText().trim();
                    loadBooks(libraryService.searchBooks(q));
                }
            });

            requestBtn.addActionListener(e -> {
                if (!isUserView || currentUser == null) {
                    JOptionPane.showMessageDialog(this,
                            "Only users can request books.");
                    return;
                }
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Select a book first.");
                    return;
                }
                String isbn = (String) tableModel.getValueAt(row, 0);
                Book selected = findBookByIsbn(isbn);
                if (selected == null) {
                    JOptionPane.showMessageDialog(this,
                            "Book not found.");
                    return;
                }
                libraryService.requestBook(currentUser, selected);
                JOptionPane.showMessageDialog(this,
                        "Book request recorded (simple implementation).");
            });
        }

        private void loadBooks(List<Book> books) {
            tableModel.setRowCount(0);
            for (Book b : books) {
                tableModel.addRow(new Object[]{
                        b.getIsbn(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getAvailableCopies()
                });
            }
        }

        private Book findBookByIsbn(String isbn) {
            for (Book b : libraryService.getAllBooks()) {
                if (b.getIsbn().equalsIgnoreCase(isbn)) {
                    return b;
                }
            }
            return null;
        }
    }

    private class ManageBooksPanel extends JPanel {

        private DefaultTableModel tableModel;

        public ManageBooksPanel() {
            setLayout(new BorderLayout(10, 10));

            JLabel title = new JLabel("Manage Books (Staff Only)", SwingConstants.CENTER);
            title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
            add(title, BorderLayout.NORTH);

            tableModel = new DefaultTableModel(
                    new Object[]{"ISBN", "Title", "Author", "Available"}, 0
            );
            JTable table = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new FlowLayout());
            JButton addBtn = new JButton("Add Book");
            JButton deleteBtn = new JButton("Delete Selected");
            JButton backBtn = new JButton("Back");

            bottom.add(addBtn);
            bottom.add(deleteBtn);
            bottom.add(backBtn);
            add(bottom, BorderLayout.SOUTH);

            loadBooks(libraryService.getAllBooks());

            addBtn.addActionListener(e -> {
                JTextField isbnField = new JTextField();
                JTextField titleField = new JTextField();
                JTextField authorField = new JTextField();
                JTextField copiesField = new JTextField();

                JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
                panel.add(new JLabel("ISBN:"));
                panel.add(isbnField);
                panel.add(new JLabel("Title:"));
                panel.add(titleField);
                panel.add(new JLabel("Author:"));
                panel.add(authorField);
                panel.add(new JLabel("Total Copies:"));
                panel.add(copiesField);

                int result = JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Add Book",
                        JOptionPane.OK_CANCEL_OPTION
                );

                if (result == JOptionPane.OK_OPTION) {
                    String isbn = isbnField.getText().trim();
                    String t = titleField.getText().trim();
                    String a = authorField.getText().trim();
                    String cStr = copiesField.getText().trim();
                    if (isbn.isEmpty() || t.isEmpty() || a.isEmpty() || cStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this,
                                "All fields are required.");
                        return;
                    }
                    int copies;
                    try {
                        copies = Integer.parseInt(cStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this,
                                "Copies must be a number.");
                        return;
                    }
                    Book b = libraryService.addBook(isbn, t, a, copies);
                    if (b == null) {
                        JOptionPane.showMessageDialog(this,
                                "Could not add book (check ISBN / data).");
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Book added successfully.");
                        loadBooks(libraryService.getAllBooks());
                    }
                }
            });

            deleteBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Select a book to delete.");
                    return;
                }
                String isbn = (String) tableModel.getValueAt(row, 0);
                boolean ok = libraryService.deleteBook(isbn);
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Book deleted.");
                    loadBooks(libraryService.getAllBooks());
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Could not delete book.");
                }
            });

            backBtn.addActionListener(e -> showScreen("STAFF_MENU"));
        }

        private void loadBooks(List<Book> books) {
            tableModel.setRowCount(0);
            for (Book b : books) {
                tableModel.addRow(new Object[]{
                        b.getIsbn(),
                        b.getTitle(),
                        b.getAuthor(),
                        b.getAvailableCopies()
                });
            }
        }
    }
}
