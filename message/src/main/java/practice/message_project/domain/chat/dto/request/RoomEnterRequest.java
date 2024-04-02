package practice.message_project.domain.chat.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RoomEnterRequest {

	private Long senderId;
	private Long receiverId;
}
