package practice.message_project.domain.chat.dto.response;

import lombok.Getter;

@Getter
public class RoomResponse {

	private final Long roomId;
	private final String roomName;
	private final String lastMessage;

	public RoomResponse(Long roomId, String roomName, String lastMessage) {
		this.roomId = roomId;
		this.roomName = roomName;
		this.lastMessage = lastMessage;
	}

	public static RoomResponse create(Long roomId, String roomName, String lastMessage) {
		return new RoomResponse(roomId, roomName, lastMessage);
	}

}
