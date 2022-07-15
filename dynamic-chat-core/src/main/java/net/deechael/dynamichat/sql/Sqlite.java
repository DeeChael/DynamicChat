package net.deechael.dynamichat.sql;

import java.io.File;
import java.sql.*;

public class Sqlite {

    private final String url;

    private Connection connection;
    private Statement statement;

    private boolean closed = false;

    public Sqlite(File file) {
        this.url = "jdbc:sqlite:" + file.getPath();
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
            this.statement = connection.createStatement();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String command) {
        try {
            return this.statement.executeQuery(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeUpdate(String command) {
        try {
            this.statement.executeUpdate(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PreparedStatement preparedStatement(String command) {
        try {
            return connection.prepareStatement(command);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (!closed) {
            try {
                this.connection.close();
                this.statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            closed = true;
        }
    }

    public boolean isClosed() {
        return closed;
    }


}
