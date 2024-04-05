package practice.message_project.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.domain.ChatRoom;

public interface ChatRepository extends JpaRepository<Chat, Long> {
	Slice<Chat> findAllByChatRoom(ChatRoom chatRoom, Pageable pageable);

	Slice<Chat> findByChatRoomId(Long id, Pageable pageable);

	Optional<Chat> findTopByChatRoomOrderById(ChatRoom chatRoom);
}
