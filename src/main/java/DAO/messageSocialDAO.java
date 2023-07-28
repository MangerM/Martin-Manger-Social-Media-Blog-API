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
        return givenMessage;
    }

    //Delete specific message
    public int removeMessageByID(int id){
        Connection messageConnection = ConnectionUtil.getConnection();
        String sql = "DELETE FROM message WHERE message_id = ?";

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

            if(pkeyResultSet.next()){
                int generated_message_id = pkeyResultSet.getInt(1);
                return new Message(generated_message_id, newMessage.getPosted_by(), newMessage.getMessage_text(), newMessage.getTime_posted_epoch());
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    //Update Message by ID
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
}
