package practice.message_project.domain.chat.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;
import practice.message_project.domain.chat.domain.Chat;

@Getter
public class ChatResponse {

	private final Long chatId; // 방 번호
	private final Long senderId; // 채팅을 보낸 사람
	private final String senderNickName;
	private final String message; // 메시지
	private final LocalDateTime sendDate;

	private ChatResponse(Long chatId, Long senderId, String senderNickName, String message, LocalDateTime sendDate) {
		this.chatId = chatId;
		this.senderId = senderId;
		this.senderNickName = senderNickName;
		this.message = message;
		this.sendDate = sendDate;
	}

	public static ChatResponse create(Chat chat) {
		Long chatId = chat.getId();
		Long senderId = chat.getSender().getId();
		String senderNickName = chat.getSender().getNickName();
		String message = chat.getMessage();
		LocalDateTime sendDate = chat.getSendDate();

		return new ChatResponse(chatId, senderId, senderNickName, message, sendDate);
	}

	public static ChatResponse create(Long chatId, Long senderId, String senderNickName, String message,
		LocalDateTime sendDate) {
		return new ChatResponse(chatId, senderId, senderNickName, message, sendDate);
	}
}
