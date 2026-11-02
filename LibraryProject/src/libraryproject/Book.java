/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

public class Book extends Publication {

    private String author;

    public Book(String id, String title, String author) {
        super(id, title);
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public long getFinePerDay() {
        return 3000;
    }

    @Override
    public String getType() {
        return "BOOK";
    }

    @Override
    public String toString() {
        return "Book[" + super.toString() + ", author=" + this.author + "]";
    }
}
