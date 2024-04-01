package practice.message_project.domain.chat.controller;

import java.util.List;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.dto.response.ChatResponse;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.domain.MessageResponse;
import practice.message_project.domain.chat.dto.request.ChatRequest;
import practice.message_project.domain.chat.service.ChatService;

@Slf4j
@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatController {

	private final ChatService chatService;

	// /pub/roomId로 요청이 오면 chat 실행
	@MessageMapping("/{roomId}")
	@SendTo("/sub/rooms/{roomId}")
	public ChatResponse chat(@DestinationVariable Long roomId, @Payload ChatRequest chatRequest) {
		log.info("roomId : {}", roomId);
		log.info("memberId: {}", chatRequest.getSenderId());
		Chat chat = chatService.makeChat(chatRequest);

		return ChatResponse.create(chat);
	}

	@PostMapping("/room")
	@ResponseBody
	public Long roomAdd() {

		ChatRoom chatRoom = chatService.addRoom();

		return chatRoom.getId();
	}

	@GetMapping("{chatRoomId}/messages")
	@ResponseBody
	public List<MessageResponse> messageList(@PathVariable Long chatRoomId) {
		return chatService.findAllMessagesByRoomId(chatRoomId);
	}

	@GetMapping("/rooms/member/{memberId}")
	@ResponseBody
	public List<Long> roomListByMemberID(@PathVariable Long memberId) {
		return chatService.findChatRoomsByMemberId(memberId);
	}
}
