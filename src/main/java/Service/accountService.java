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

    //Get all accounts  (not necessary for this project but useful for manual testing)
    public List<Account> getAllAccounts(){
        List<Account> returnedList = asDAO.getAllAccounts();
        return returnedList;
    }

    //Login
    //- The login will be successful if and only if the username and password provided in the request body JSON match a real account existing on the database. 
    //- If successful, the response body should contain a JSON of the account in the response body, including its account_id. 
    //- The response status should be 200 OK, which is the default.
    //- If the login is not successful, the response status should be 401. (Unauthorized)
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
    //- The registration will be successful if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist. 
    //- If all these conditions are met, the response body should contain a JSON of the Account, including its account_id. 
    //- The response status should be 200 OK, which is the default. The new account should be persisted to the database.
    //- If the registration is not successful, the response status should be 400. (Client error)
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
