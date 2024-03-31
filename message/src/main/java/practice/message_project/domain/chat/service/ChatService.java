package practice.message_project.domain.chat.service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.domain.Member.domain.Member;
import practice.message_project.domain.Member.repository.MemberRepository;
import practice.message_project.domain.chat.domain.ChatMessage;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.domain.ChatRoomMember;
import practice.message_project.domain.chat.domain.MessageResponse;
import practice.message_project.domain.chat.repository.ChatMessageRepository;
import practice.message_project.domain.chat.repository.ChatRoomMemberRepository;
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
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final MemberRepository memberRepository;

	public List<ChatRoom> findAllRoom(){
		return chatRoomRepository.findAll();
	}

	public Optional<ChatRoom> findChatRoomById(Long roomId){
		return chatRoomRepository.findById(roomId);
	}

	public ChatRoom addRoom(String name) {
		return ChatRoom.create(name);
	}


	// 채팅방 유저 리스트에 유저 추가
	public String addMember(Long roomId, Long memberId){
		ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
		Member member = memberRepository.findById(memberId).orElseThrow();

		ChatRoomMember chatRoomMember = ChatRoomMember.create(room, member);

		ChatRoomMember savedChatRoomMember = chatRoomMemberRepository.save(chatRoomMember);

		return "ok";
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

	// 채팅방 유저 리스트 삭제
	public void deleteMember(Long roomId, Long memberId){
		ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
		Member member = memberRepository.findById(memberId).orElseThrow();

		ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(room, member);

		chatRoomMemberRepository.delete(chatRoomMember);
	}
}
