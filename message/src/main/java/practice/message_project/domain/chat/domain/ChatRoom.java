package practice.message_project.domain.chat.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.message_project.domain.Member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

	@Id
	@Column(name = "CHAT_ROOM_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 처음 메세지를 보낸 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FROM_MEMBER_ID")
	private Member fromMember;

	// fromMember가 마지막으로 메세지를 읽은 시간
	private LocalDateTime fromMemberLastRead;

	// 처음 메세지를 받은 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TO_MEMBER_ID")
	private Member toMember;

	// toMember가 마지막으로 메세지를 읽은 시간
	private LocalDateTime toMemberLastRead;

	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
	private List<Chat> chats = new ArrayList<>();

	public ChatRoom(Member fromMember, Member toMember) {
		this.fromMember = fromMember;
		this.toMember = toMember;
	}

	public static ChatRoom create(Member fromMember, Member toMember) {
		return new ChatRoom(fromMember, toMember);
	}

	public void addChat(Chat chat) {
		chats.add(chat);
	}

	public void deleteFromMember() {
		this.fromMember = null;
	}

	public void deleteToMember() {
		this.toMember = null;
	}

	public void updateFromMemberLastRead() {
		fromMemberLastRead = LocalDateTime.now();
	}

	public void updateToMemberLastRead() {
		toMemberLastRead = LocalDateTime.now();
	}
}
