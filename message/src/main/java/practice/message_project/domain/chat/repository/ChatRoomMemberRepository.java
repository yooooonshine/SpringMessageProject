package practice.message_project.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import practice.message_project.domain.Member.domain.Member;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.domain.ChatRoomMember;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
	ChatRoomMember findByChatRoomAndMember(ChatRoom chatRoom, Member member);

	List<ChatRoomMember> findAllByMember(Member member);
}
