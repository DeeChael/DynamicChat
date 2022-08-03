package net.deechael.dynamichat.sql;

import java.io.File;
import java.sql.*;

public class Sqlite {

    private final String url;

    private Connection connection;

    private boolean closed = false;

    public Sqlite(File file) {
        this.url = "jdbc:sqlite:" + file.getPath();
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String command) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            ResultSet resultSet = statement.executeQuery();
            //statement.close();
            return resultSet;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeUpdate(String command) {
        try {
            PreparedStatement statement = this.connection.prepareStatement(command);
            statement.executeUpdate();
            statement.close();
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
