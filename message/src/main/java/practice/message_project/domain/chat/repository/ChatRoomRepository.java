package practice.message_project.domain.chat.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import practice.message_project.domain.Member.domain.Member;
import practice.message_project.domain.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	Slice<ChatRoom> findAllByFromMemberOrToMember(Member fromMember, Member toMember, Pageable pageable);
}
