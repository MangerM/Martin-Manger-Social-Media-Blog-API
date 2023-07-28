package Service;

import java.util.*;

import DAO.messageSocialDAO;
import Model.Message;

public class messageService {
    messageSocialDAO msDAO;

    //Constructors
    public messageService(){
        msDAO = new messageSocialDAO();
    }

    public messageService(messageSocialDAO DAO){
        this.msDAO = DAO;
    }


    //Actions

    //Get All Messages
    public List<Message> getAllMessages(){
        List<Message> allMessages = msDAO.getAllMessages();
        return allMessages;
    }

    //Get message by ID
    public Message getMessageByID(int messageID){
        Message returnedMessage = msDAO.getMessageByID(messageID);
        if(returnedMessage == null){
            System.out.println("Message ID " + messageID + " does not exist");
        }
        return returnedMessage;
    }

    //Delete Specific Message
    public Message deleteMessageByID(int messageID){
        Message returnedMessage = msDAO.getMessageByID(messageID);
        int linesRemoved = msDAO.removeMessageByID(messageID);
        if(linesRemoved > 0){
            return returnedMessage;
        }else{
            return null;
        }
    }

    //Create new message
    public Message createMessage(Message submittedMessage){
        Message returnedMessage = msDAO.addMessage(submittedMessage);
        return returnedMessage;
    }

    //Update message by ID
    public Message updateMessage(int messageID, String msgBody){
        if(getMessageByID(messageID) != null){
            msDAO.updateMessage(messageID, msgBody);
            return getMessageByID(messageID);
        }else{
            return null;
        }
    }

    //Get all messages by user
    public List<Message> userMessages(int userID){
        List<Message> resultList = msDAO.usersMessages(userID);
        return resultList;
    }
}
