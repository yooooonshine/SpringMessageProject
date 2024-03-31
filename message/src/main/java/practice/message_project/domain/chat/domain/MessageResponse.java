package practice.message_project.domain.chat.domain;

import lombok.Getter;

@Getter
public class MessageResponse {

	private final String text;

	private MessageResponse(String text) {
		this.text = text;
	}

	public static MessageResponse create(ChatMessage chatMessage) {
		return new MessageResponse(chatMessage.getText());
	}
}
