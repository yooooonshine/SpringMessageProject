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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.common.dto.CustomResponse;
import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.dto.request.RoomEnterRequest;
import practice.message_project.domain.chat.dto.response.ChatResponse;
import practice.message_project.domain.chat.domain.MessageResponse;
import practice.message_project.domain.chat.dto.request.ChatRequest;
import practice.message_project.domain.chat.dto.response.RoomResponse;
import practice.message_project.domain.chat.service.ChatService;

@Slf4j
@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatController {

	private final ChatService chatService;

	//메세지 수신 및 전송
	@MessageMapping("/{roomId}")
	@SendTo("/sub/rooms/{roomId}")
	public CustomResponse<ChatResponse> chat(@DestinationVariable Long roomId, @Payload ChatRequest chatRequest) {
		log.info("chat");
		Chat chat = chatService.makeChat(chatRequest);

		log.info("chatResponse Message : {}", chat.getMessage() );
		return CustomResponse.ok(ChatResponse.create(chat));
	}

	//방 만들기
	@PostMapping("/room")
	@ResponseBody
	public CustomResponse<RoomResponse> roomEnter(@RequestBody RoomEnterRequest roomEnterRequest) {
		log.info("roomEnter... senderId : {}, receiverId : {}", roomEnterRequest.getSenderId(), roomEnterRequest.getReceiverId());
		RoomResponse roomResponse = chatService.enterRoom(roomEnterRequest.getSenderId(), roomEnterRequest.getReceiverId());

		return CustomResponse.ok(roomResponse);
	}

	//채팅방 메세지 가져오기
	@GetMapping("{chatRoomId}/messages")
	@ResponseBody
	public CustomResponse<List<MessageResponse>> messageList(@PathVariable Long chatRoomId) {
		List<MessageResponse> messageResponses = chatService.findAllMessagesByRoomId(chatRoomId);

		return CustomResponse.ok(messageResponses);
	}

	//멤버id로 방리스트 가져오기
	@GetMapping("/rooms/member/{memberId}")
	@ResponseBody
	public CustomResponse<List<RoomResponse>> roomListByMemberID(@PathVariable Long memberId) {
		List<RoomResponse> roomResponses = chatService.findChatRoomsByMemberId(memberId);

		return CustomResponse.ok(roomResponses);
	}
}
