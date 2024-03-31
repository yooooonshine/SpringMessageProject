package practice.message_project.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import practice.message_project.domain.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
