package uz.pdp.repository;

import lombok.NoArgsConstructor;
import uz.pdp.configs.DbConfig;
import uz.pdp.configs.PConfig;
import uz.pdp.models.AuthUser;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


@NoArgsConstructor
public class AuthUserRepository {
    private static final AuthUserRepository instance = new AuthUserRepository();
    private final DbConfig dbConfig = DbConfig.getInstance();

    public void insert(AuthUser user) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.users.insert"));
        ) {
            preparedStatement.setString(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getLanguageCode());
            preparedStatement.setString(6, user.getJoinedAt());
            preparedStatement.setString(7, user.getStatus());
            preparedStatement.setString(8, user.getRole());
            preparedStatement.setString(9, user.getChatID());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(String userID, String langugeCode) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.update.user"));
        ) {
            preparedStatement.setString(1, langugeCode);
            preparedStatement.setString(2, userID);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean alreadyRegistred(String userID) {
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.select.user"));
        ) {
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public AuthUser select(String userID) {
        ArrayList<AuthUser> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.select.user"))) {
            preparedStatement.setString(1, userID);
            ResultSet resultSet = preparedStatement.executeQuery();
            users = resultSetToAuthUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users.get(0);
    }

    public ArrayList<AuthUser> getAll() {
        ArrayList<AuthUser> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = dbConfig.conn().prepareStatement(PConfig.getInstance().getFromQueryProperties("query.selectAll.user"))) {
            ResultSet resultSet = preparedStatement.executeQuery();
            users = resultSetToAuthUser(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private ArrayList<AuthUser> resultSetToAuthUser(ResultSet resultSet) {
        ArrayList<AuthUser> authUsers = new ArrayList<>();
        try {
            while (resultSet.next()) {
                AuthUser authUser = new AuthUser();
                authUser.setId(resultSet.getString("user_id"));
                authUser.setUsername(resultSet.getString("user_name"));
                authUser.setFirstName(resultSet.getString("first_name"));
                authUser.setLastName(resultSet.getString("last_name"));
                authUser.setLanguageCode(resultSet.getString("language_code"));
                authUser.setJoinedAt(resultSet.getString("joined_at"));
                authUser.setStatus(resultSet.getString("status"));
                authUser.setRole(resultSet.getString("role"));
                authUsers.add(authUser);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authUsers;
    }

    public String getRole(String userID) {
        AuthUser authUser = select(userID);
        return authUser.getRole();
    }

    public static AuthUserRepository getInstance() {
        return instance;
    }
}
