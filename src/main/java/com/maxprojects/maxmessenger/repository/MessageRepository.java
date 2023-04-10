package com.maxprojects.maxmessenger.repository;

import com.maxprojects.maxmessenger.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
