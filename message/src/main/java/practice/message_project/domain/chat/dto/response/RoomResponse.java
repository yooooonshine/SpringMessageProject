package practice.message_project.domain.chat.dto.response;

import lombok.Getter;

@Getter
public class RoomResponse {
	private final Long roomId;

	private RoomResponse(Long roomId) {
		this.roomId = roomId;
	}

	public RoomResponse create(Long roomId) {
		return new RoomResponse(roomId);
	}
}
