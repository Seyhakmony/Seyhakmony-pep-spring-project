package com.example.service;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Serivce for handling logic for message methods
 */
@Service
public class MessageService {

    private MessageRepository messageRepository;
    private AccountRepository accountRepository;
    
    //Constructor
    @Autowired
    public MessageService(MessageRepository mR, AccountRepository aR){
        this.messageRepository = mR;
        this.accountRepository = aR;
    }

    /*
     * Retrieves all messages used for debugging
     * 
     */
    public List<Message> allMessages(){
        List<Message> allM = messageRepository.findAll();
        return allM;
    }

    /*
     * Message is valid if message_text is not blank, is under 255 characters
     *  already chcked if id exited so this method creates and saves message  to messageRepository
     * @return saved Message if valid and if null if not
     */
    public Message createMessage(Message m){
        if(m.getMessageText() == null || m.getMessageText().trim().isEmpty() || m.getMessageText().length() > 255){
            return null;
        }
        
        return messageRepository.save(m);
    }

    /*
     * Retrieves a message by its ID, used in multiple of the endpoints to check if there's a message to delete or be updated etc.
     * @return Mesage if found, or null if not found
     */
    public Message getMessageById(int mId){
        Optional<Message> temp = messageRepository.findById(mId);

        if(!temp.isPresent()){
            return null;
        }
        return temp.get();
    }

    /*
     * Deletes message by Id
     * @return 1 as I already checked if it exist bu message id so it will return 1
     */
    public int deleteMessage(int id){

        messageRepository.deleteById(id);

        return 1;
    }

    /*
     * Updates the message text of an existing message
     * @return 1 if updated successfully, and the other 2 numbers and -1 if not valid
     */
    public int updateMessage(int id, String m){
        if (m == null || m.trim().isEmpty() || m.length() > 255) {
            return -1;
        }
        Optional<Message> mC = messageRepository.findById(id);
        if(mC.isPresent()){
            Message tempUpdated = mC.get();
            tempUpdated.setMessageText(m);

            messageRepository.save(tempUpdated);
            
            return 1;
        }
        return -1;
    }

    /*
     * Gets all messages posted by a specific account as this can be done since post by references account id
     * @returns a list of Message objects posted by given account
     */
    public List<Message> messageByAccountid(int id){
        List<Message> allUserMessages = messageRepository.findBypostedBy(id);
        return allUserMessages;
    }


}
