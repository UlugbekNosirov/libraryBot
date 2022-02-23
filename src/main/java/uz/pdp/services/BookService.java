package uz.pdp.services;

import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import uz.pdp.buttons.InlineBoard;
import uz.pdp.models.Book;
import uz.pdp.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class BookService {
    private static final BookRepository bookRepository = BookRepository.getInstance();
    public static ArrayList<Book> books = new ArrayList();

    public static void save(Document document, String sentBy) {
        Book book = new Book(document.getFileId(), document.getFileName(), LocalDateTime.now().toString(), sentBy);
        bookRepository.insert(book);
    }

    public static String getMyBooksText(String userID) {
        ArrayList<Book> myBooks = bookRepository.getMyBooks(userID);
        return getDataAboutBooks(myBooks);
    }

    public static void search(String search) {
        books = bookRepository.select(search);
    }

    public static void searchInProcess() {
        books = bookRepository.select();
    }

    public static String getText() {
        ArrayList<Book> result = getBooks();
        return getDataAboutBooks(result);
    }

    private static String getDataAboutBooks(ArrayList<Book> result) {
        StringBuilder stringBuilder = new StringBuilder("");
        int i = 1;
        for (Book book : result) {
            stringBuilder.append(String.format("%s. %s     [\uD83D\uDC4D %s   \uD83D\uDC4E %s   ⭳ %s] \n", i++, book.getName(), book.getLike(), book.getDislike(), book.getDownload()));
        }
        return stringBuilder.toString();
    }

    public static InlineKeyboardMarkup getInlineKeyboard() {
        return InlineBoard.getInstance().makeInlineKeyboard(getBooks());
    }

    private static ArrayList<Book> getBooks() {
        int start = (AuthUserService.getSessionUser().getPage() - 1) * 10;
        int end = Math.min(start + 10, books.size());
        ArrayList<Book> result = new ArrayList();

        for (int i = start; i < end; ++i) {
            result.add((Book) books.get(i));
        }

        return result;
    }
}
