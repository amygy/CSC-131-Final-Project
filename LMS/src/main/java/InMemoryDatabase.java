import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryDatabase {

    private static final InMemoryDatabase INSTANCE = new InMemoryDatabase();

    private List<User> users = new ArrayList<>();
    private List<Book> books = new ArrayList<>();

    private InMemoryDatabase() {
        // Seed one staff and one normal user for testing
        users.add(new Staff(
                UUID.randomUUID().toString(),
                "Default Librarian",
                "staff@library.com",
                "staff123",
                "12345"
        ));

        users.add(new User(
                UUID.randomUUID().toString(),
                "Test User",
                "user@library.com",
                "user123"
        ));

        // Seed some books
        books.add(new Book("1111", "Clean Code", "Robert C. Martin", 3));
        books.add(new Book("2222", "Design Patterns", "Gamma et al.", 2));
        books.add(new Book("3333", "The Hobbit", "J.R.R. Tolkien", 1));
    }

    public static InMemoryDatabase getInstance() {
        return INSTANCE;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }
}
