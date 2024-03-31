package practice.message_project.domain.chat.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

	@Id
	@Column(name = "CHAT_ROOM_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// 채팅방 이름
	@NotBlank
	private String name;

	@OneToMany(mappedBy = "chatRoom")
	private List<ChatMessage> chatMessages = new ArrayList<>();

	private ChatRoom(String name) {
		this.name = name;
	}

	public static ChatRoom create(String name) {
		return new ChatRoom(name);
	}

}
