package practice.message_project.domain.chat.dto.response;

import lombok.Getter;

@Getter
public class ChatRoomResponse {

	private final Long chatRoomId;
	private final String roomName;
	private final String lastMessage;

	public ChatRoomResponse(Long chatRoomId, String roomName, String lastMessage) {
		this.chatRoomId = chatRoomId;
		this.roomName = roomName;
		this.lastMessage = lastMessage;
	}

	public static ChatRoomResponse create(Long chatRoomId, String roomName, String lastMessage) {
		return new ChatRoomResponse(chatRoomId, roomName, lastMessage);
	}

}
