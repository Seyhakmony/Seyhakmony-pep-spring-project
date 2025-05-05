package com.example.service;


import java.util.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

/*
 * this is a service class for handling business logic for account
 */
@Service
public class AccountService {
    private AccountRepository accountRepository;

    private MessageService messageService;

    /*
     * Constructor
     */
    @Autowired
    public AccountService(AccountRepository accR, MessageService messageService){
        this.accountRepository = accR;
        this.messageService = messageService;
        
    }
    /*
     * Returns list of all registered accounts, helped with debbugging
     * @return list of account objects
     */
    public List<Account> allAccounts(){
        List<Account> allAcc = accountRepository.findAll();
        return allAcc;
    }

    /*
     * register a new user if inputs are valid as if the username is not blank, the password is at least 4 characters long
     * Since I already checked before if username already existed I saved to accountRepository
     * @return account saves accountRepository null if invalid
     */
    public Account register(Account user){
        if(user.getUsername().trim().isEmpty() || user.getPassword() == null || user.getPassword().length() < 4){
            return null;
        }

        return accountRepository.save(user);
    }

    /*
     * Used to check if a userrname already exist. This is mainly used for the implementation that goes with the registration logic
     * @return false meaning there's a user that does exist so you are arent able to use it, and true if you can register that user account
     */
    public boolean checkAccUser(Account account){
        if(accountRepository.findByusername(account.getUsername()) != null){
            return false;
        }
        return true;
    }

    /*
     * Check by id which is used for when I try to get all messages from user given account id, so I just make sure its here in account
     * @return true if theres an id, false if otherwise
     */
    public boolean checkAccById(int accountId) {
        if (accountRepository.existsById(accountId)) {
            return true;
        }
        return false;
    }
    
    /*
     * Login lgoic as username and password has to not be empty and has to match to with a username and password together to in which
     * @returns Account object if valid and null if othervise for any reason
     */
    public Account login(Account account){
        if(account.getUsername() == null || account.getPassword() == null){
            return null;
        }

        Account acc = accountRepository.findByusername(account.getUsername());
        if(acc != null && acc.getPassword().equals(account.getPassword())){
            return acc;
        }

        return null;
    }




}
