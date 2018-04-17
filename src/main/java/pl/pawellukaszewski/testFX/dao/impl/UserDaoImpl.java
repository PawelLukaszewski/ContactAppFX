package pl.pawellukaszewski.testFX.dao.impl;

import pl.pawellukaszewski.testFX.dao.UserDao;
import pl.pawellukaszewski.testFX.models.MySQLConnector;
import pl.pawellukaszewski.testFX.models.UserSession;
import pl.pawellukaszewski.testFX.models.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {


    private MySQLConnector connector = MySQLConnector.getInstance();
    private UserSession session = UserSession.getInstance();


    @Override
    public boolean login(String name, String password) {
        PreparedStatement preparedStatement = connector.getPreparedStatement("SELECT * FROM userr WHERE username=?");
        try {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) { // przesuwa wskaznik z -1 na 0;
                return false;
            }


            session.setId(resultSet.getInt("id"));
            return (resultSet.getString("password").equals(Utils.shaHash(password)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean register(String name, String password) {

        PreparedStatement preparedStatement = connector.getPreparedStatement(
                "SELECT * FROM userr WHERE username=?");
        try {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }

            PreparedStatement preparedStatementInsert = connector.getPreparedStatement(
                    "INSERT INTO userr VALUES(?,?,?)");

            preparedStatementInsert.setInt(1, 0);
            preparedStatementInsert.setString(2, name);
            preparedStatementInsert.setString(3, Utils.shaHash(password));
            preparedStatementInsert.execute();

            preparedStatement.close();
            preparedStatementInsert.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    @Override
    public boolean removeUser(int id) {
        PreparedStatement preparedStatementRemove = connector.getPreparedStatement("SELECT WHERE userr WHERE id=?");
        try {
            preparedStatementRemove.setInt(1, id);
            preparedStatementRemove.execute();
            preparedStatementRemove.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return false;
    }
}
