package uz.pdp.repository;

import uz.pdp.configs.DbConfig;
import uz.pdp.configs.PConfig;
import uz.pdp.models.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class BookRepository {
    private static final BookRepository instance = new BookRepository();
    private final DbConfig dbConfig = DbConfig.getInstance();

    public void insert(Book book) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.insert"));
        ) {
            preparedStatement.setString(1, book.getFileID());
            preparedStatement.setString(2, book.getName());
            preparedStatement.setString(3, book.getSentAt());
            preparedStatement.setString(4, book.getSentBy());
            preparedStatement.setString(5, book.getStatus());
            preparedStatement.setInt(6, book.getLike());
            preparedStatement.setInt(7, book.getDislike());
            preparedStatement.setLong(8, book.getDownload());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> select(String search) {
        ArrayList<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.select"));
        ) {
            preparedStatement.setString(1, search);
            ResultSet resultSet = preparedStatement.executeQuery();
            books = resultSetToArrayList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public String getFileIDInProcess(Long id) {
        String fileID = "";
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.select.byID.inProcess"));
        ) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                fileID = resultSet.getString("file_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fileID;
    }

    public String getFileIDAccepted(Long id) {
        return select(id).getFileID();
    }

    public Book select(Long id) {
        ArrayList<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.select.byID"));
        ) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            books = resultSetToArrayList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books.get(0);
    }

    public ArrayList<Book> select() {
        ArrayList<Book> books = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.select.inProcess"));
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            books = resultSetToArrayList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public void accept(Long id) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.accept"))) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reject(Long id) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.reject"))) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void like(Long id) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.like"))) {
            preparedStatement.setInt(1, select(id).getLike() + 1);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dislike(Long id) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.dislike"))) {
            preparedStatement.setInt(1, select(id).getDislike() + 1);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void download(Long id) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.download"))) {
            preparedStatement.setLong(1, select(id).getDownload() + 1);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> getMyBooks(String userID) {
        ArrayList<Book> myBooks = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.books.select.by.sentBy"))) {
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            myBooks = resultSetToArrayList(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return myBooks;
    }

    private ArrayList<Book> resultSetToArrayList(ResultSet resultSet) {
        ArrayList<Book> books = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Book book = new Book(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString("status"), resultSet.
                        getInt("like"), resultSet.getInt("dislike"), resultSet.getLong("download"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public static BookRepository getInstance() {
        return instance;
    }
}
