package practice.message_project.domain.chat.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import practice.message_project.domain.chat.domain.Chat;

@Getter
public class ChatResponse {

	private final Long roomId; // 방 번호
	private final Long senderId; // 채팅을 보낸 사람
	private final String senderNickName;
	private final String message; // 메시지
	private final LocalDateTime sendDate;

	private ChatResponse(Long roomId, Long senderId, String senderNickName, String message, LocalDateTime sendDate) {
		this.roomId = roomId;
		this.senderId = senderId;
		this.senderNickName = senderNickName;
		this.message = message;
		this.sendDate = sendDate;
	}

	public static ChatResponse create(Chat chat) {
		Long roomId = chat.getId();
		;
		Long senderId = chat.getSender().getId();
		String senderNickName = chat.getSender().getNickName();
		String message = chat.getMessage();
		LocalDateTime sendDate = chat.getSendDate();

		return new ChatResponse(roomId, senderId, senderNickName, message, sendDate);
	}

	public static ChatResponse create(Long roomId, Long senderId, String senderNickName, String message,
		LocalDateTime sendDate) {
		return new ChatResponse(roomId, senderId, senderNickName, message, sendDate);
	}
}