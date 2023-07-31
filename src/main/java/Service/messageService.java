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
    //- The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
    //- It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    public List<Message> getAllMessages(){
        List<Message> allMessages = msDAO.getAllMessages();
        return allMessages;
    }

    //Get message by ID
    //- The response body should contain a JSON representation of the message identified by the message_id. 
    //- It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.
    public Message getMessageByID(int messageID){
        Message returnedMessage = msDAO.getMessageByID(messageID);
        if(returnedMessage == null){
            System.out.println("Message ID " + messageID + " does not exist");
        }
        return returnedMessage;
    }

    //Delete Specific Message
    //- The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain 
    //- the now-deleted message. The response status should be 200, which is the default.
    //- If the message did not exist, the response status should be 200, but the response body should be empty. 
    //- This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
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
    //- The creation of the message will be successful if and only if the message_text is not blank, is under 255 characters, and posted_by refers to a real, 
    //- existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, 
    //- which is the default. The new message should be persisted to the database.
    //- If the creation of the message is not successful, the response status should be 400. (Client error)
    public Message createMessage(Message submittedMessage){
        Message returnedMessage = msDAO.addMessage(submittedMessage);
        return returnedMessage;
    }

    //Update message by ID
    //- The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. 
    //- If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), 
    //- and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
    //- If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    public Message updateMessage(int messageID, String msgBody){
        if(getMessageByID(messageID) != null){
            msDAO.updateMessage(messageID, msgBody);
            return getMessageByID(messageID);
        }else{
            return null;
        }
    }

    //Get all messages by user
    //- The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. 
    //- It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    public List<Message> userMessages(int userID){
        List<Message> resultList = msDAO.usersMessages(userID);
        return resultList;
    }
}
