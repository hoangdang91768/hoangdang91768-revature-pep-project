package Service;

import Model.Account;
import DAO.AccountDAO;;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();

    public Account registerAccount(Account account){
        // username should not blank
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            return null; 
        }
    
        // check if password is at least 4 characters
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null; 
        }
    
        // check if the username is already exists
        if (accountDAO.findAccByUserName(account.getUsername()) != null) {
            return null; 
        }
    
        Account newAcc = accountDAO.createAccount(account);
        return newAcc;
    }

    public Account login(String username, String password) {
        try {
            Account account = accountDAO.findAccByUserName(username);
            if (account != null && account.getPassword().equals(password)) {
                return account; 
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public Account findAccountById(int accountId) {
        Account account = accountDAO.findAccByID(accountId); 
        if (account != null && account.getAccount_id() ==  accountId){
            return account;
        } 
        return null;
    }

    public Account findAccountByUsername(String username) {
        Account account = accountDAO.findAccByUserName(username); 
        if (account != null){
            return account;
        } 
        return null;
    }
}
