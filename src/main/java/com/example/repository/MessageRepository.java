package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import com.example.entity.Message;

/*
 * data accessing interface for Message entity
 * it also extends JpaRepository like accountRepository to provide built-in operations
 * The main here provided below is a method to find all messages by posted by which references the accountid
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBypostedBy(int postedBy);

}
