public class Book {
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;

    public Book(String isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }

    public boolean hasAvailableCopies() {
        return availableCopies > 0;
    }

    public void checkoutOne() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    @Override
    public String toString() {
        return title + " by " + author + " (ISBN " + isbn + ")";
    }
}
