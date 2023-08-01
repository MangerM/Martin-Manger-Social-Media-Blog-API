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
        //If the returned message is null then it found no messages.  We can still pass a null back up the chain but we print out an error message for clarity.
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
        //First we get a message by ID.  If this returns null we know the message does not exist (the getMessageByID method does this) so we continue if not null
        if(returnedMessage != null){
            int linesRemoved = msDAO.removeMessageByID(messageID);
            //We only return the message if lines were actually deleted.  the message may exist but the delete may not remove lines so we need to return null if that case happens.
            //In future we might want to handle this situation differently but as this is either success or fail return null.
            if(linesRemoved > 0){
                return returnedMessage;
            }else{
                return null;
            }
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
        //We assume at this point that the posted_by exists, this would be checked at the endpoint level in the controller
        //If the message is blank return null and print the alert.
        if(submittedMessage.getMessage_text().isBlank()){
            System.out.println("Blank message");
            return null;

        }else if(submittedMessage.getMessage_text().length()>=255){
            //If the message is too long return null and print the alert.
            System.out.println("Message Length too long");
            return null;

        }else{
            //Otherwise add the message and return the added message.
            Message returnedMessage = msDAO.addMessage(submittedMessage);
            return returnedMessage;
        }
    }

    //Update message by ID
    //- The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. 
    //- If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), 
    //- and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
    //- If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    public Message updateMessage(int messageID, String msgBody){
        //Verify the message exists using the get message by id, if not null then update else return null.
        if(getMessageByID(messageID) != null){
            //If the message is blank return null and print the alert.
            if(msgBody.isBlank()){
                System.out.println("Blank message");
                return null;
            }else if(msgBody.length() >= 255){
                //If the message is too long return null and print the alert.
                System.out.println("Message Length too long");
                return null;
            }else{
                //message checks out, update the message
                msDAO.updateMessage(messageID, msgBody);
                return getMessageByID(messageID);
            }
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
