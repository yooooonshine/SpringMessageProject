package practice.message_project.domain.chat.controller;

import org.springframework.data.domain.Slice;
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

import jakarta.websocket.server.PathParam;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.common.dto.CustomResponse;
import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.dto.request.ChatRequest;
import practice.message_project.domain.chat.dto.request.RoomEnterRequest;
import practice.message_project.domain.chat.dto.response.ChatResponse;
import practice.message_project.domain.chat.dto.response.ChatRoomResponse;
import practice.message_project.domain.chat.service.ChatService;

@Slf4j
@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatController {

	private final ChatService chatService;

	//메세지 수신 및 전송
	@MessageMapping("/chatRooms/{roomId}")
	@SendTo("/sub/chatRooms/{roomId}")
	public CustomResponse<ChatResponse> chat(@DestinationVariable Long roomId, @Payload ChatRequest chatRequest) {
		log.info("chat");
		Chat chat = chatService.makeChat(chatRequest);

		log.info("chatResponse Message : {}", chat.getMessage());
		return CustomResponse.ok(ChatResponse.create(chat));
	}

	//방 만들기
	@PostMapping("/api/chatRooms")
	@ResponseBody
	public CustomResponse<ChatRoomResponse> roomEnter(@RequestBody RoomEnterRequest roomEnterRequest) {
		log.info("roomEnter... senderId : {}, receiverId : {}", roomEnterRequest.getSenderId(),
			roomEnterRequest.getReceiverId());
		ChatRoomResponse chatRoomResponse = chatService.enterRoom(roomEnterRequest.getSenderId(),
			roomEnterRequest.getReceiverId());

		return CustomResponse.ok(chatRoomResponse);
	}

	//채팅방 메세지 가져오기
	@GetMapping("/api/chatRooms/{chatRoomId}/messages")
	@ResponseBody
	public CustomResponse<Slice<ChatResponse>> messageList(
		@PathVariable Long chatRoomId,
		@PathParam("pageNumber") int pageNumber,
		@PathParam("pageSize") int pageSize
	) {
		Slice<ChatResponse> chatResponses = chatService.findMessagesByRoomId(chatRoomId, pageNumber, pageSize);

		return CustomResponse.ok(chatResponses);
	}

	//멤버id로 방리스트 가져오기
	@GetMapping("/api/chatRooms/members/{memberId}")
	@ResponseBody
	public CustomResponse<Slice<ChatRoomResponse>> roomListByMemberID(
		@PathVariable Long memberId,
		@PathParam("pageNumber") int pageNumber,
		@PathParam("pageSize") int pageSize
	) {
		Slice<ChatRoomResponse> roomResponses = chatService.findChatRoomsByMemberId(memberId, pageNumber, pageSize);

		return CustomResponse.ok(roomResponses);
	}
}
