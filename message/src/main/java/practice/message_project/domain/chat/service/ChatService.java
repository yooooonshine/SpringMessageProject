package practice.message_project.domain.chat.service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.domain.chat.domain.ChatMessage;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.domain.MessageResponse;
import practice.message_project.domain.chat.repository.ChatMessageRepository;
import practice.message_project.domain.chat.repository.ChatRoomRepository;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Data
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatMessageRepository chatMessageRepository;

	public List<ChatRoom> findAllRoom(){
		return chatRoomRepository.findAll();
	}

	public Optional<ChatRoom> findChatRoomById(Long roomId){
		return chatRoomRepository.findById(roomId);
	}

	public ChatRoom addRoom(String name) {
		return ChatRoom.create(name);
	}

	public List<MessageResponse> findAllMessagesByRoomId(Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		List<MessageResponse> messageResponses;

		List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom(chatRoom);
		messageResponses = chatMessages.stream()
			.map(MessageResponse::create)
			.toList();

		return messageResponses;
	}
}
