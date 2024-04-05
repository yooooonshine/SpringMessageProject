package practice.message_project.domain.chat.domain;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.message_project.domain.Member.domain.Member;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

	@Id
	@Column(name = "CHAT_ROOM_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 처음 메세지를 보낸 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FROM_MEMBER_ID")
	private Member fromMember;

	// 처음 메세지를 받은 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TO_MEMBER_ID")
	private Member toMember;

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
}
