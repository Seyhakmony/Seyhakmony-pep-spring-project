package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Account;

/*
 * Data access interface for the account entity and it also extends the JpaRepository to provide basic operations for account table
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>{
    Account findByusername(String username);

}
