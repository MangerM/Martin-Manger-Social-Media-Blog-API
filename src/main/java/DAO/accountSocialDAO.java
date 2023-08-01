package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class accountSocialDAO {
    //Necessary DAO actions for accessing/manipulating account table

    //Get Existing Username
    public Account verifyUsername(String username){
        Connection accountConnection = ConnectionUtil.getConnection();
        //We need to see if the username already exists.  Try to get the record from the DB and return an account with the correct info.
        try {
            String sql = "SELECT * FROM account WHERE username = ?";

            
            PreparedStatement preparedStatement = accountConnection.prepareStatement(sql);
            preparedStatement.setString(1,username);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account returnedAccount = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return returnedAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //If we reach this point the SQL failed, either through an exception or not returning a result set so we return null.
        return null;
    }

    //Verify userID
    public Account verifyUserID(int userID){
        Connection accountConnection = ConnectionUtil.getConnection();
        //We need to see if the userID already exists
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";

            
            PreparedStatement preparedStatement = accountConnection.prepareStatement(sql);
            preparedStatement.setInt(1,userID);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account returnedAccount = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return returnedAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //If we reach this point the SQL failed, either through an exception or not returning a result set so we return null.
        return null;
    }

    //Login
    public Account loginAccount(String username, String password){
        Connection accountConnection = ConnectionUtil.getConnection();
        //We need to find an existing user with the username and password combination.
        //Similar to verify userID but using username and password instead
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

            
            PreparedStatement preparedStatement = accountConnection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account returnedAccount = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return returnedAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //If we reach this point the SQL failed, either through an exception or not returning a result set so we return null.
        return null;
    }

    

    //Add a new account
    public Account addAccount(Account newAccount){
        Connection accountConnection = ConnectionUtil.getConnection();
        //Adding a new account entry we only need a username and password
        try{
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)" ;
            PreparedStatement preparedStatement = accountConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            preparedStatement.setString(1,newAccount.getUsername());
            preparedStatement.setString(2,newAccount.getPassword());
    
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
    
            if(pkeyResultSet.next()){
                int generated_user_id = (int) pkeyResultSet.getLong(1);
                return new Account(generated_user_id, newAccount.getUsername(), newAccount.getPassword());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    //The following methods are not necessary for this project but are useful for manual testing or potential future use if the project expands

    //Remove all or a particular account (not necessary for this project but included for future use)
    public void removeAllAccounts(){
        Connection accountConnection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM account";  //THIS IS NOT A GOOD IDEA BUT MAY BE NECESSARY.  Not for the purpose of this project but better to include for future use. 
            Statement s = accountConnection.createStatement();
            s.executeUpdate(sql);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void removeAccount(String username, String password){
        Connection accountConnection = ConnectionUtil.getConnection();
        try{
            String sql = "DELETE FROM account WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = accountConnection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Change an existing password, not necessary for this project but included for future use
    public void changePass(String username, String password, String newPass){
        Connection accountConnection = ConnectionUtil.getConnection();
        try{
            String sql = "UPDATE account SET password = ? WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = accountConnection.prepareStatement(sql);
            preparedStatement.setString(1,newPass);
            preparedStatement.setString(2,username);
            preparedStatement.setString(3,password);

            preparedStatement.executeUpdate();
           
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Get all accounts (not necessary for this project but included for future use and testing)
    public List<Account> getAllAccounts(){
        Connection accountConnection = ConnectionUtil.getConnection();
        List<Account> resultList = new ArrayList<>();
        //We want all existing accounts in full detail so SELECT * works for us.
        try {
            String sql = "SELECT * FROM account";

            PreparedStatement preparedStatement = accountConnection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account givenAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                resultList.add(givenAccount);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return resultList;
    }
}
