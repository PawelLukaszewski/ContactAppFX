package pl.pawellukaszewski.testFX.dao.impl;

import pl.pawellukaszewski.testFX.dao.ContactDao;
import pl.pawellukaszewski.testFX.models.MySQLConnector;
import pl.pawellukaszewski.testFX.models.UserSession;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {


    private MySQLConnector connector = MySQLConnector.getInstance();
private UserSession userSession =UserSession.getInstance();

    @Override
    public List<String> getAllContactsName(String username) {
        List<String> nameList = new ArrayList<>();

        PreparedStatement preparedStatement = connector.getPreparedStatement(
                "SELECT name FROM contact INNER JOIN userr ON userr.id = contact.userid WHERE userr.username = ?");
        try {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                nameList.add(resultSet.getString("name"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nameList;
    }


    @Override
    public String getNumber(String contact) {

        PreparedStatement preparedStatement = connector.getPreparedStatement(

                "SELECT phonenumber FROM contact WHERE name = ?");
        try {
            preparedStatement.setString(1, contact);
            ResultSet resultSet = preparedStatement.executeQuery();

           while (resultSet.next()){
               return resultSet.getString("phonenumber");
           }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean addContact(String name, String number) {

        PreparedStatement preparedStatement = connector.getPreparedStatement(
                "SELECT * FROM contact WHERE name=?");
        try {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }

        PreparedStatement preparedStatementInsert = connector.getPreparedStatement(
                "INSERT INTO contact VALUES(?,?,?,?)"
        );


            preparedStatementInsert.setInt(1, 0);
            preparedStatementInsert.setString(2, name);
            preparedStatementInsert.setString(3, number);
            preparedStatementInsert.setInt(4, userSession.getId());
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
    public void removeContact(String name) {

        PreparedStatement preparedStatement = connector.getPreparedStatement(
                "DELETE FROM contact WHERE name = ?"
        );
        try {
            preparedStatement.setString(1, name);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean editContact(String newName, String number,String oldName) {
        PreparedStatement preparedStatement = connector.getPreparedStatement(
                "UPDATE contact SET phonenumber = ?, name=? WHERE name = ?"
        );

        try {
            preparedStatement.setString(1, number);
            preparedStatement.setString(2, newName);
            preparedStatement.setString(3, oldName);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
