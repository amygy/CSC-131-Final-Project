import java.util.ArrayList;
import java.util.List;

public class LibraryService {

    private final InMemoryDatabase db = InMemoryDatabase.getInstance();

    public List<Book> getAllBooks() {
        return db.getBooks();
    }

    // Simple search: title or author or ISBN contains query (case-insensitive)
    public List<Book> searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            return new ArrayList<>(db.getBooks());
        }
        String q = query.toLowerCase();
        List<Book> result = new ArrayList<>();
        for (Book b : db.getBooks()) {
            if (b.getTitle().toLowerCase().contains(q)
                    || b.getAuthor().toLowerCase().contains(q)
                    || b.getIsbn().toLowerCase().contains(q)) {
                result.add(b);
            }
        }
        return result;
    }

    public boolean checkoutBook(User user, Book book) {
        if (user == null || book == null) return false;
        if (!book.hasAvailableCopies()) {
            return false;
        }
        book.checkoutOne();
        // We’re keeping it simple: just decrease available copies.
        return true;
    }

    public void requestBook(User user, Book book) {
        // Minimal “request” logic – just log to console.
        // You could later add a real list of requests.
        System.out.println("Book requested: " + book.getTitle()
                + " by " + user.getName());
    }

    // Staff only
    public Book addBook(String isbn, String title, String author, int totalCopies) {
        if (isbn == null || isbn.isEmpty() || totalCopies <= 0) return null;
        // prevent duplicates by ISBN
        for (Book b : db.getBooks()) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                return null;
            }
        }
        Book book = new Book(isbn, title, author, totalCopies);
        db.addBook(book);
        return book;
    }

    public boolean deleteBook(String isbn) {
        Book toRemove = null;
        for (Book b : db.getBooks()) {
            if (b.getIsbn().equalsIgnoreCase(isbn)) {
                toRemove = b;
                break;
            }
        }
        if (toRemove != null) {
            db.removeBook(toRemove);
            return true;
        }
        return false;
    }
}
