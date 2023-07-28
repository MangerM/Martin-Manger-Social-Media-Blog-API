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
        Account signedInUser;


        Javalin app = controller.startAPI();
        app.start(8080);

        accountService aService = new accountService();
        messageService mService = new messageService();
        
        Account newUser = new Account("mmanger", "Pa55w0rd");
        aService.newAccount(newUser);
        aService.newAccount(new Account("bbstyger30", "Password"));
        signedInUser = aService.login("mmanger", "Pa55w0rd");
        if(signedInUser != null){
            Message newMessage = new Message(signedInUser.getAccount_id(), "This is a message for Jim", 152333418);
            mService.createMessage(newMessage);
        }
        System.out.println(aService.getAllAccounts());
        System.out.println(mService.getAllMessages());
        Message updatedMessage = mService.updateMessage(1, "This is new message text");
        System.out.println(updatedMessage);
        System.out.println(mService.userMessages(signedInUser.getAccount_id()));
        System.out.println(mService.getAllMessages());
        Message deletedMessage = mService.deleteMessageByID(5);
        System.out.println("Deleted Message: " + deletedMessage);
    }
}
