package practice.message_project.domain.chat.service;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.domain.Member.domain.Member;
import practice.message_project.domain.Member.repository.MemberRepository;
import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.domain.ChatRoomMember;
import practice.message_project.domain.chat.domain.MessageResponse;
import practice.message_project.domain.chat.dto.request.ChatRequest;
import practice.message_project.domain.chat.repository.ChatRepository;
import practice.message_project.domain.chat.repository.ChatRoomMemberRepository;
import practice.message_project.domain.chat.repository.ChatRoomRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Data
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatService {
	private final String WELCOME_MESSAGE = "대화를 시작해보세요.";
	private final String OUT_MESSAGE = "{}님이 채팅방에서 나가셨습니다.";

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public List<ChatRoom> findAllRoom(){
		return chatRoomRepository.findAll();
	}

	@Transactional(readOnly = true)
	public List<Long> findChatRoomsByMemberId(Long memberId){
		Member member = memberRepository.findById(memberId).orElseThrow();

		List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByMember(member);

		return chatRoomMembers.stream()
			.map(chatRoomMember -> chatRoomMember.getChatRoom().getId())
			.toList();
	}

	public ChatRoom addRoom() {
		ChatRoom chatRoom = ChatRoom.create();

		return chatRoomRepository.save(chatRoom);
	}

	public Chat addChat(ChatRequest chatRequest) {
		Member sender = memberRepository.findById(chatRequest.getSenderId()).orElseThrow();
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId()).orElseThrow();
		String message = chatRequest.getMessage();

		Chat chat = Chat.create(chatRoom, sender, message);

		return chatRepository.save(chat);
	}

	public Chat addChat(ChatRoom chatRoom, Member sender, String message) {

		Chat chat = Chat.create(chatRoom, sender, message);

		return chatRepository.save(chat);
	}

	public ChatRoomMember addChatRoomMember(ChatRoom chatRoom, Member member) {
		ChatRoomMember chatRoomMember = ChatRoomMember.create(chatRoom, member);

		return chatRoomMemberRepository.save(chatRoomMember);
	}

	public void deleteChatRoomMember(ChatRoom chatRoom, Member member) {
		ChatRoomMember chatRoomAndMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member);

		chatRoomMemberRepository.delete(chatRoomAndMember);
	}

	public Chat makeChat(ChatRequest chatRequest) {

		if (chatRequest.getMessageType() == ChatRequest.MessageType.ENTER) {
			return makeEnterChat(chatRequest);
		} else if (chatRequest.getMessageType() == ChatRequest.MessageType.TALK) {
			return makeTalkChat(chatRequest);
		} else {
			return makeOutChat(chatRequest);
		}
	}

	private Chat makeEnterChat(ChatRequest chatRequest) {
		Member sender = memberRepository.findById(chatRequest.getSenderId()).orElseThrow();
		Member receiver = memberRepository.findById(chatRequest.getReceiverId()).orElseThrow();
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId()).orElseThrow();

		log.info("makeEnterChat");

		addChatRoomMember(chatRoom, sender);
		addChatRoomMember(chatRoom, receiver);

		Chat chat = addChat(chatRoom, sender, WELCOME_MESSAGE);

		chatRoom.addChat(chat);

		log.info("chatMessage : {}", chat.getMessage());

		return chat;
	}

	private Chat makeTalkChat(ChatRequest chatRequest) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId()).orElseThrow();
		Member sender = memberRepository.findById(chatRequest.getSenderId()).orElseThrow();
		String message = chatRequest.getMessage();

		//chat 저장하기
		Chat chat = addChat(chatRoom, sender, message);

		//chatRoom에 chat 넣기
		chatRoom.addChat(chat);

		return chat;
	}

	private Chat makeOutChat(ChatRequest chatRequest) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId()).orElseThrow();
		Member sender = memberRepository.findById(chatRequest.getSenderId()).orElseThrow();

		deleteChatRoomMember(chatRoom, sender);
		//chat 저장하기
		Chat chat = addChat(chatRoom, sender, OUT_MESSAGE);

		//chatRoom에 chat 넣기
		chatRoom.addChat(chat);

		return chat;
	}


	// 채팅방 유저 리스트에 유저 추가
	public String addMember(Long roomId, Long memberId){
		ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
		Member member = memberRepository.findById(memberId).orElseThrow();

		ChatRoomMember chatRoomMember = ChatRoomMember.create(room, member);

		ChatRoomMember savedChatRoomMember = chatRoomMemberRepository.save(chatRoomMember);

		return "ok";
	}

	@Transactional(readOnly = true)
	public List<MessageResponse> findAllMessagesByRoomId(Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		List<MessageResponse> messageResponses;

		List<Chat> chats = chatRepository.findAllByChatRoom(chatRoom);
		messageResponses = chats.stream()
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
