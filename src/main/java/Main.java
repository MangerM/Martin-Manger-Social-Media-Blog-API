import Controller.SocialMediaController;
import Model.Account;
import Model.Message;
import Service.accountService;
import Service.messageService;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application. This class will not
 * affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        SocialMediaController controller = new SocialMediaController();
        
        Javalin app = controller.startAPI();
        app.start(8080);

        //Manual Testing Code
        /*
        
        accountService aService = new accountService();
        messageService mService = new messageService();
        Account signedInUser;
        Account newUser = new Account("user20", "123type");

        //Account Testing
        //New Account
        aService.newAccount(newUser);  
        aService.newAccount(new Account("dude2", "dudebro"));

        //Login Testing
        signedInUser = aService.login("user20", "123type");


        //Message Testing
        //Create Message
        if(signedInUser != null){
            Message newMessage = new Message(signedInUser.getAccount_id(), "This is a message for Jim", 152333418);
            mService.createMessage(newMessage);
        }

        //Get all Messages
        System.out.println(mService.getAllMessages());

        //Get Message by ID
        System.out.println(mService.getMessageByID(1));

        //Delete Message
        Message deletedMessage = mService.deleteMessageByID(1);
        System.out.println("Deleted Message: " + deletedMessage);

        //Update Message
        Message updatedMessage = mService.updateMessage(1, "This is new message text");
        System.out.println(updatedMessage);
        
        //Get all Messages by User
        System.out.println(mService.userMessages(signedInUser.getAccount_id()));
        */
    }
}
