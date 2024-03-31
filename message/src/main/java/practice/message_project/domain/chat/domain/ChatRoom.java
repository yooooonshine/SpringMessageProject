package practice.message_project.domain.chat.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.message_project.domain.chat.service.ChatService;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

	@Id
	@Column(name = "CHAT_ROOM_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	private String name; // 채팅방 이름

	@Builder(access = AccessLevel.PROTECTED)
	private ChatRoom(String name){
		this.name = name;
	}

	@OneToMany(mappedBy = "chatRoom")
	private List<ChatMessage> chatMessages = new ArrayList<>();

	public static ChatRoom create(String name) {

		return ChatRoom.builder()
			.name(name)
			.build();
	}

}
