package practice.message_project.domain.chat.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
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

	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
	private List<Chat> chats = new ArrayList<>();

	public static ChatRoom create() {
		return new ChatRoom();
	}

	public void addChat(Chat chat) {
		chats.add(chat);
	}

}
