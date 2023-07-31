package Controller;
import Model.Account;
import Model.Message;
import Service.accountService;
import Service.messageService;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;


public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    //We need both services (account and message) and an object mapper for all tests.
    accountService aService = new accountService();
    messageService mService = new messageService();
    ObjectMapper objectMapper = new ObjectMapper();

    public Javalin startAPI() {
        

        //Included endpoints are needed for the project, there are more available DAO operations (get all users for example) that exist but are not used for the project.
        //They have been included in the DAO and Service but not marked with an endpoint because they were useful for manual testing and if the project were to be expanded
        //in future then the background code already exists.
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register",this::registerUser);
        app.post("/login",this::userLogin);
        app.post("/messages",this::newMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{ID}", this::getMessageByID);
        app.delete("/messages/{ID}", this::deleteMessageByID);
        app.patch("/messages/{ID}", this::updateMessage);
        app.get("/accounts/{ID}/messages", this::getUserMessages);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }
    
    //Register user endpoint.  
    //- The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. 
    //- If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. 
    //- The response status should be 200 OK, which is the default. The new account should be persisted to the database.
    //- If the registration is not successful, the response status should be 400. (Client error)
    private void registerUser(Context context){

        Account newAccount = new Account();
        newAccount = context.bodyAsClass(newAccount.getClass());
        Account returnedAccount = aService.newAccount(newAccount);
        if(returnedAccount == null){
            context.status(400);
            context.result("");
        }else{
            try {
                context.result(objectMapper.writeValueAsString(returnedAccount));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    //Login user endpoint
    //- The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. 
    //- If successful, the response body should contain a JSON of the account in the response body, including its account_id. 
    //- The response status should be 200 OK, which is the default.
    //- If the login is not successful, the response status should be 401. (Unauthorized)
    private void userLogin(Context context){
        Account loginAccount = new Account();
        loginAccount = context.bodyAsClass(loginAccount.getClass());
        Account returnedAccount = aService.login(loginAccount.getUsername(), loginAccount.getPassword());
        if(returnedAccount == null){
            context.status(401);
            context.result("");
        }else{
            try {
                context.result(objectMapper.writeValueAsString(returnedAccount));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    //New Message Endpoint 
    //- The creation of the message will be successful if and only if the message_text is not blank, is under 255 characters, and posted_by refers to a real, 
    //- existing user. If successful, the response body should contain a JSON of the message, including its message_id. The response status should be 200, 
    //- which is the default. The new message should be persisted to the database.
    //- If the creation of the message is not successful, the response status should be 400. (Client error)
    private void newMessage(Context context){
        Message submittedMessage = new Message();
        Message returnedMessage = new Message();
        submittedMessage = context.bodyAsClass(submittedMessage.getClass());
        if(aService.userIDExist(submittedMessage.getPosted_by()) && submittedMessage.getMessage_text().length() < 255 && submittedMessage.getMessage_text() != ""){
            returnedMessage = mService.createMessage(submittedMessage);
            try {
                context.result(objectMapper.writeValueAsString(returnedMessage));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            context.status(400);
            context.result("");
        }
    }

    //Get all messages endpoint
    //- The response body should contain a JSON representation of a list containing all messages retrieved from the database. 
    //- It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    private void getAllMessages(Context context){
        List<Message> allMessageList = new ArrayList<Message>();
        allMessageList = mService.getAllMessages();
        if(allMessageList != null){
            try {
                context.result(objectMapper.writeValueAsString(allMessageList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else {
            context.result("");
        }
    }

    //Get Specific Message Endpoint
    //- The response body should contain a JSON representation of the message identified by the message_id. 
    //- It is expected for the response body to simply be empty if there is no such message. The response status should always be 200, which is the default.
    private void getMessageByID(Context context){
        int messageID = objectMapper.convertValue(context.pathParam("ID"), int.class);
        Message returnedMessage = mService.getMessageByID(messageID);
        if (returnedMessage != null){
            try {
                context.result(objectMapper.writeValueAsString(returnedMessage));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            context.result("");
        }
    }

    //Delete specific message
    //- The deletion of an existing message should remove an existing message from the database. If the message existed, the response body should contain 
    //- the now-deleted message. The response status should be 200, which is the default.
    //- If the message did not exist, the response status should be 200, but the response body should be empty. 
    //- This is because the DELETE verb is intended to be idempotent, ie, multiple calls to the DELETE endpoint should respond with the same type of response.
    private void deleteMessageByID(Context context){
        int messageID = objectMapper.convertValue(context.pathParam("ID"), int.class);
        Message returnedMessage = mService.deleteMessageByID(messageID);
        if (returnedMessage != null){
            try {
                context.result(objectMapper.writeValueAsString(returnedMessage));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            context.result("");
        }
    }

    //Update specific message
    //- The update of a message should be successful if and only if the message id already exists and the new message_text is not blank and is not over 255 characters. 
    //- If the update is successful, the response body should contain the full updated message (including message_id, posted_by, message_text, and time_posted_epoch), 
    //- and the response status should be 200, which is the default. The message existing on the database should have the updated message_text.
    //- If the update of the message is not successful for any reason, the response status should be 400. (Client error)
    private void updateMessage(Context context){
        int messageID = objectMapper.convertValue(context.pathParam("ID"), int.class);
        String newContent = "";
        Message newMessage = null;
        Message returnedMessage = null;
        try {
            newMessage = objectMapper.readValue(context.body(), Message.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        newContent = newMessage.getMessage_text();
        if(newContent != "" && newContent.length() < 255){
            returnedMessage = mService.updateMessage(messageID, newContent);
        }
        
        if (returnedMessage != null){
            try {
                context.result(objectMapper.writeValueAsString(returnedMessage));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }else{
            context.result("");
            context.status(400);
        }
    }

    //Get all messages by userID
    //- The response body should contain a JSON representation of a list containing all messages posted by a particular user, which is retrieved from the database. 
    //- It is expected for the list to simply be empty if there are no messages. The response status should always be 200, which is the default.
    private void getUserMessages(Context context){
        int messageID = objectMapper.convertValue(context.pathParam("ID"), int.class);
        List<Message> resultList = new ArrayList<Message>();
        resultList = mService.userMessages(messageID);
        if(resultList != null){
            try {
                context.result(objectMapper.writeValueAsString(resultList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
}