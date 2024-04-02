package practice.message_project.domain.chat.dto.request;

import lombok.*;
import practice.message_project.domain.chat.domain.Chat;

@Getter
public class ChatRequest {
	// 메시지  타입 : 입장, 채팅
	public enum MessageType{
		TALK, OUT
	}

	private MessageType messageType; // 메시지 타입
	private Long chatRoomId; // 방 번호
	private Long senderId; // 채팅을 보낸 사람
	private Long receiverId; // 채팅을 받은 사람
	private String message; // 메시지
}
