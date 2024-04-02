package practice.message_project.domain.chat.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class RoomsResponse {
	private final List<Long> roomIds;

	private RoomsResponse(List<Long> roomIds) {
		this.roomIds = roomIds;
	}

	public static RoomsResponse create(List<Long> roomIds) {
		return new RoomsResponse(roomIds);
	}
}
