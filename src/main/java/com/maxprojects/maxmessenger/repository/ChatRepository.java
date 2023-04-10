package com.maxprojects.maxmessenger.repository;

import com.maxprojects.maxmessenger.Entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
