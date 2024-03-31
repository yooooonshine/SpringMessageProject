package practice.message_project.domain.chat.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import practice.message_project.domain.chat.domain.ChatDTO;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.domain.MessageResponse;
import practice.message_project.domain.chat.service.ChatService;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatController {

	private final ChatService chatService;

	@PostMapping
	public ChatRoom roomAdd(@RequestParam String name) {

		return chatService.addRoom(name);
	}

	@GetMapping("{chatRoomId}/messages")
	public List<MessageResponse> messageList(@PathVariable Long chatRoomId) {
		return chatService.findAllMessagesByRoomId(chatRoomId);
	}

	@GetMapping
	public List<ChatRoom> roomList() {
		return chatService.findAllRoom();
	}
}
