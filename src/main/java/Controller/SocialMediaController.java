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

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

    accountService aService = new accountService();
    messageService mService = new messageService();
    ObjectMapper objectMapper = new ObjectMapper();

    public Javalin startAPI() {
        
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
    
    //Register user endpoint.  If register user fails then set result and status to blank and 400, else return the JSON of the account
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