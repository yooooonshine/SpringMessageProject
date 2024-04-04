package practice.message_project.domain.chat.dto.response;

import lombok.Getter;

@Getter
public class RoomResponse {

	private final Long roomId;
	private final String roomName;
	private final String recentChat;

	public RoomResponse(Long roomId, String roomName, String recentChat) {
		this.roomId = roomId;
		this.roomName = roomName;
		this.recentChat = recentChat;
	}

	public static RoomResponse create(Long roomId, String roomName, String recentChat) {
		return new RoomResponse(roomId, roomName, recentChat);
	}

}
