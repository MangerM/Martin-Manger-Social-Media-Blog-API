package Service;

import DAO.accountSocialDAO;
import Model.Account;

import java.util.*;

public class accountService {
    accountSocialDAO asDAO;

    //Constructors
    public accountService(){
        this.asDAO = new accountSocialDAO();
    }

    public accountService(accountSocialDAO ASD){
        this.asDAO = ASD;
    }

    //Account actions

    //Get all accounts
    public List<Account> getAllAccounts(){
        List<Account> returnedList = asDAO.getAllAccounts();
        return returnedList;
    }

    //Login
    public Account login(String username, String password){
        Account signedIn = asDAO.loginAccount(username, password);
        if(signedIn == null){
            System.out.println("Login failed.  Incorrect username or password");
        }
        return signedIn;
    }

    //Verify Username
    public Boolean usernameExist(String username){
        Account existingAccount = asDAO.verifyUsername(username);
        if(existingAccount == null){
            return false;
        }else{
            return true;
        }
    }

    //Verify UserID
    public Boolean userIDExist(int userID){
        Account existingAccount = asDAO.verifyUserID(userID);
        if(existingAccount == null){
            return false;
        }else{
            return true;
        }
    }

    //Create a new account
    public Account newAccount(Account submittedAccount){
        //If the username exists then return null else return the newly added account
        if(usernameExist(submittedAccount.getUsername()) == false){
            Account addedAccount = asDAO.addAccount(submittedAccount);
            return addedAccount;
        }else{
            System.out.println("Can't add account " + submittedAccount.getUsername() + ", username already exists.");
            return null;
        }
    }
}
