package practice.message_project.domain.chat.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import practice.message_project.domain.Member.domain.Member;
import practice.message_project.domain.chat.domain.ChatRoom;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	Slice<ChatRoom> findAllByFromMemberOrToMember(Member fromMember, Member toMember, Pageable pageable);

	// 두 멤버가 참여하는 방이 있는 지 확인
	@Query("SELECT cr FROM ChatRoom cr WHERE " +
		"(cr.fromMember = :memberA AND cr.toMember = :memberB) OR " +
		"(cr.fromMember = :memberB AND cr.toMember = :memberA)")
	Optional<ChatRoom> findChatRoomByMembers(Member memberA, Member memberB);

}
