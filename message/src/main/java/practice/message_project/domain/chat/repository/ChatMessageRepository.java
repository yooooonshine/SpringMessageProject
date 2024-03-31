package practice.message_project.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import practice.message_project.domain.chat.domain.ChatMessage;
import practice.message_project.domain.chat.domain.ChatRoom;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
	List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
