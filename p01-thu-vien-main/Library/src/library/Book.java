package library;

public class Book extends Publication {
    private String author;

    public Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public long getFinePerDay() {
        return 5000;
    }

    @Override
    public String getType() {
        return "BOOK";
    }

    @Override
    public String toString() {
        return "Book[" + super.toString() + ", author=" + author + "]";
    }
}