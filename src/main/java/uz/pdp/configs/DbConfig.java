package uz.pdp.configs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DbConfig {
    private static final DbConfig instance = new DbConfig();
    private Connection connection;

    public Connection conn() {
        try {
            if (Objects.isNull(this.connection) || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection(PConfig.getInstance().getFromAppProperties("db.connection"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void close() {
        try {
            if (Objects.nonNull(this.connection) && !this.connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static DbConfig getInstance() {
        return instance;
    }
}

