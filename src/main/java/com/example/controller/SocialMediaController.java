package com.example.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
/*
 * Hanndles the Http request for accounts and messages
 * Creates endpoints for register, login, getting all & create messages, messages/{message_id}, delete & update at messages/{message_id}, and accounts/{account_id}/messages
 */
@RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    /*
     * constuctor to initialize
     */
    @Autowired
    public SocialMediaController(AccountService accountS, MessageService messageS){
        this.accountService = accountS;
        this.messageService = messageS;
    }

    /*
     * new post on the endpoint POST localhost:8080/messages
     * Request body will contain a JSON representation of a message
     * Implementaton logic to check if its a valid message_text is handled in messageService
     * First checked if theirs an existing user and I just used the accountid it since postby references accountid to make sure that there's an account that exist, so a new message can be created.
     * If successful then response will be 200, if not the 400 as for client error
     */
    @PostMapping("messages")
    public ResponseEntity<Message> createM(@RequestBody Message m){
        
        if(accountService.checkAccById(m.getPostedBy())){
            Message createdM = messageService.createMessage(m);

            if(createdM != null){

                return ResponseEntity.ok().body(createdM);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    /*
     * This allos to submit a GET request on the endpoint GET localhost:8080/messages
     * returns Json representation f all messages retrieved in the database.
     * Default will always be status of 200, but if message is null then it will just return an empty string
     */
    @GetMapping("messages")
    public ResponseEntity<List<Message>> allMessages(){
        List<Message> tempM = messageService.allMessages();

        if(tempM != null){
            return ResponseEntity.ok().body(tempM);
        }
        return ResponseEntity.ok().build();
    }

    
    /*
     * Allows to submit a GET request on the endpoint GET localhost:8080/messages/{message_id}
     * Status will always be 200, and even if there's no existing message given id thenit will just return empty body
     */
    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> messageById(@PathVariable int message_id){
        Message m = messageService.getMessageById(message_id);

        if(m != null){
            return ResponseEntity.ok().body(m);
        }

        return ResponseEntity.ok().build();
    }

    /*
     * able to submit a DELETE request on the endpoint DELETE localhost:8080/messages/{message_id}
     * If the message exist then it will delete the message and the response body will contain the number of rows updated
     * should be 1 if successful as message_id is a primary key
     * Default status response will be 200 and if not found then response body will be empty
     */
    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<Integer> deletebyId(@PathVariable int message_id){
        Message m = messageService.getMessageById(message_id);

        if(m != null){
            int num = messageService.deleteMessage(message_id);
            
            return ResponseEntity.ok().body(num);
            
        }   

        return ResponseEntity.ok().build();
    }

    /*
     * Allows a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}
     * Updates message on if message id exist already and replaces it with the new message_text while returning the rows updated(1)
     * Status response will be 200 by default, if not successful for any reason then status will be 400
     */
    @PatchMapping("messages/{message_id}")
    public ResponseEntity<Integer> updatedMessagebyId(@PathVariable int message_id, @RequestBody Message mUpdated){
        Message mTemp = messageService.getMessageById(message_id);
        if(mTemp != null){
            int num = messageService.updateMessage(message_id, mUpdated.getMessageText());
            if(num == 1){
                return ResponseEntity.ok().body(num);
            }
        }

        return ResponseEntity.badRequest().build();
    }

    /*
     * able to create a new Account on the endpoint POST localhost:8080/register
     * Implementation logic to what a username is handled in AccountService (if and only if the username is not blank, the password is at least 4 characters long, and an Account with that username does not already exist).
     * Response is 200 by default but if theres a duplicate user, meaning a user is exist already then status will be 409 conflict
     * If successful then it will return 200, and if theirs some other reason it will return status of 400
     */
    @PostMapping("register")
    public ResponseEntity<Account> register(@RequestBody Account account){
        boolean check = accountService.checkAccUser(account);
        if(!check){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if(check){
            accountService.register(account);
            return ResponseEntity.ok().body(account);
        }

        return ResponseEntity.badRequest().build();
    }


    /*
     * verify my login on the endpoint POST localhost:8080/login.
     * Login logic will be that of, will only be successful if username and password provided match to existing account on the database
     * Response is 200 if successful and if not then the status will be 401 unauthorized error.
     */
    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account checkLogin = accountService.login(account);

        if(checkLogin != null){
            return ResponseEntity.ok().body(checkLogin);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }

    /*
     * Allows user to submit an GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.
     * Response body will be a JSON representation of all message
     * I first check by accountid as postby references account_id in which then use postby to get all the messages from the Message table
     * Response is allways 200 but if messages is empty it will return empty string. 
     */
    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByUser(@PathVariable int account_id){

        boolean checkAccount = accountService.checkAccById(account_id);
        if(checkAccount){
            List<Message> m = messageService.messageByAccountid(account_id);

            if(m != null){
                return ResponseEntity.ok().body(m);
            }
    
        }

        return ResponseEntity.ok().build();
    }

}
