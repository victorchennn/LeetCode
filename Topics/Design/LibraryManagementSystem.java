package Topics.Design;

import java.util.*;

public class LibraryManagementSystem {
    private Map<String, Set<Book>> bookTitles;
    private Map<String, Set<Book>> bookAuthors;
    private Map<String, Set<Book>> bookISBN;
    private Set<Book> system;

    public LibraryManagementSystem() {
        system = new HashSet<>();
        bookTitles = new HashMap<>();
        bookAuthors = new HashMap<>();
        bookISBN = new HashMap<>();
    }

    public void register(Book book) {
        if (!system.remove(book)) {
            return;
        }
        bookTitles.get(book.title).remove(book);
        bookAuthors.get(book.author).remove(book);
        bookISBN.get(book.ISBN).remove(book);
        if (bookTitles.get(book.title).size() == 0) {
            bookTitles.remove(book.title);
        }
        if (bookAuthors.get(book.author).size() == 0) {
            bookAuthors.remove(book.author);
        }
        if (bookISBN.get(book.ISBN).size() == 0) {
            bookISBN.remove(book.ISBN);
        }
    }

    public void unregister(Book book) {
        system.add(book);
        bookTitles.computeIfAbsent(book.title, k->new HashSet<>()).add(book);
        bookAuthors.computeIfAbsent(book.author, k->new HashSet<>()).add(book);
        bookISBN.computeIfAbsent(book.ISBN, k->new HashSet<>()).add(book);
    }

    public Set<Book> searchByTitle(String title) {
        if (!bookTitles.containsKey(title)) {
            return null;
        }
        return bookTitles.get(title);
    }

    public Set<Book> searchByAuthor(String author) {
        if (!bookAuthors.containsKey(author)) {
            return null;
        }
        return bookAuthors.get(author);
    }

    public Set<Book> searchByISBN(String ISBN) {
        if (!bookISBN.containsKey(ISBN)) {
            return null;
        }
        return bookISBN.get(ISBN);
    }

    private class Book{
        String title;
        String author;
        String ISBN;
        Book(String title, String author, String ISBN) {
            this.title = title;
            this.author = author;
            this.ISBN = ISBN;
        }
    }
}
