package practice.message_project.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.domain.ChatRoom;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	List<Chat> findAllByChatRoom(ChatRoom chatRoom);
}
