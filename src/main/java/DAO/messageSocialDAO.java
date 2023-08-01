package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class messageSocialDAO {
    //Necessary DAO actions for accessing/manipulating message table

    //Get all messages
    public List<Message> getAllMessages(){
        Connection messageConnection = ConnectionUtil.getConnection();
        List<Message> resultList = new ArrayList<>();
        //We want all existing messages in full detail so SELECT * works for us.
        try {
            String sql = "SELECT * FROM message";

            PreparedStatement preparedStatement = messageConnection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message givenMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                resultList.add(givenMessage);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //As result list is defaulted to a new blank arraylist we can safely return it here as it will either have all the records from the result set or be blank
        return resultList;
    };

    //Get specific message
    public Message getMessageByID(int id){
        Connection messageConnection = ConnectionUtil.getConnection();
        Message givenMessage = null;
        String sql = "SELECT * FROM message WHERE message_id = ?";

        try {
            PreparedStatement preparedStatement = messageConnection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                givenMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        //As givenMessage is defaulted to null we can safely return it here as it will either have the record from the result set or be null
        return givenMessage;
    }

    //Delete specific message
    public int removeMessageByID(int id){
        Connection messageConnection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";
        //Given this is a delete command we will be returning the number of lines updated/removed.  If we succeed at deleting some entry we return the number of lines touched
        //If the SQL fails then return 0
        try {
            PreparedStatement preparedStatement = messageConnection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
            return 0;
        }
        
    }

    //Create new message
    public Message addMessage(Message newMessage){
        Connection messageConnection = ConnectionUtil.getConnection();
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";

        try{
            PreparedStatement preparedStatement = messageConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        
            preparedStatement.setInt(1,newMessage.getPosted_by());
            preparedStatement.setString(2,newMessage.getMessage_text());
            preparedStatement.setLong(3, newMessage.getTime_posted_epoch());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();

            //If the message was added then create a new Message object using the returned message ID and the submitted message details.
            if(pkeyResultSet.next()){
                int generated_message_id = pkeyResultSet.getInt(1);
                return new Message(generated_message_id, newMessage.getPosted_by(), newMessage.getMessage_text(), newMessage.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Update Message by ID.  This does not need to return anything as we have already verified that the ID exists so we can safely update it, the service will 
    //fetch the new message once the update is complete.
    public void updateMessage(int messageID, String newMessageBody){
        Connection messageConnection = ConnectionUtil.getConnection();
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        try{
            PreparedStatement preparedStatement = messageConnection.prepareStatement(sql);
        
            preparedStatement.setString(1,newMessageBody);
            preparedStatement.setInt(2, messageID);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    

    //Get all messages by userID
    public List<Message> usersMessages(int userID){
        Connection messageConnection = ConnectionUtil.getConnection();
        List<Message> resultList = new ArrayList<>();
        //We want all existing messages in full detail so SELECT * works for us when limited by posted_by.
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";

            PreparedStatement preparedStatement = messageConnection.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message givenMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                resultList.add(givenMessage);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return resultList;
    }


    //Following methods are not necessary for this project but may be useful if the project gets expanded or for manual testing
    //Delete all messages
    public void removeMessages(){
        Connection messageConnection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message"; //THIS IS NOT A GOOD IDEA BUT MAY BE NECESSARY.  Not for the purpose of this project but better to include for future use. 

        try {
            Statement s = messageConnection.createStatement();
            s.executeUpdate(sql);
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        
    }
}
