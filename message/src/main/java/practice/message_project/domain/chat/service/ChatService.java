package practice.message_project.domain.chat.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.common.dto.CustomResponse;
import practice.message_project.domain.Member.domain.Member;
import practice.message_project.domain.Member.repository.MemberRepository;
import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.domain.ChatRoomMember;
import practice.message_project.domain.chat.dto.request.ChatRequest;
import practice.message_project.domain.chat.dto.response.ChatResponse;
import practice.message_project.domain.chat.dto.response.RoomResponse;
import practice.message_project.domain.chat.repository.ChatRepository;
import practice.message_project.domain.chat.repository.ChatRoomMemberRepository;
import practice.message_project.domain.chat.repository.ChatRoomRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatService {
	private final String OUT_MESSAGE = "%s님이 채팅방에서 나가셨습니다.";

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;
	private final MemberRepository memberRepository;


	//모든 방 찾기
	@Transactional(readOnly = true)
	public List<ChatRoom> findAllRoom(){
		return chatRoomRepository.findAll();
	}


	//멤버가 참여하는 방 찾기
	@Transactional(readOnly = true)
	public List<RoomResponse> findChatRoomsByMemberId(Long memberId){
		Member member = memberRepository.findById(memberId).orElseThrow();

		List<ChatRoomMember> chatRoomMembers = chatRoomMemberRepository.findAllByMember(member);

		return chatRoomMembers.stream()
			.map(chatRoomMember -> RoomResponse.create(chatRoomMember.getChatRoom().getId(), null, null
			))
			.toList();
	}

	//방 만들고 입장하기
	public RoomResponse enterRoom(Long senderId, Long receiverId) {
		Member sender = memberRepository.findById(senderId).orElseThrow();
		Member receiver = memberRepository.findById(receiverId).orElseThrow();

		//방 만들기
		ChatRoom chatRoom = addRoom();

		//방에 멤버 넣기
		addChatRoomMember(chatRoom, sender);
		addChatRoomMember(chatRoom, receiver);

		return RoomResponse.create(chatRoom.getId(), sender.getNickName(), chatRoom.getChats().get(chatRoom.getChats().size()).getMessage());
	}

	//방 만들기
	private ChatRoom addRoom() {
		ChatRoom chatRoom = ChatRoom.create();

		return chatRoomRepository.save(chatRoom);
	}

	private Chat addChat(ChatRoom chatRoom, Member sender, String message) {

		Chat chat = Chat.create(chatRoom, sender, message);

		return chatRepository.save(chat);
	}


	private ChatRoomMember addChatRoomMember(ChatRoom chatRoom, Member member) {
		ChatRoomMember chatRoomMember = ChatRoomMember.create(chatRoom, member);

		return chatRoomMemberRepository.save(chatRoomMember);
	}


	//채팅 응답 만들기
	public Chat makeChat(ChatRequest chatRequest) {
		if (chatRequest.getMessageType() == ChatRequest.MessageType.TALK) {
			return makeTalkChat(chatRequest);
		} else {
			return makeOutChat(chatRequest);
		}
	}

	//대화 채팅 만들기
	private Chat makeTalkChat(ChatRequest chatRequest) {

		log.info("makeTalkChat");
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId()).orElseThrow();
		Member sender = memberRepository.findById(chatRequest.getSenderId()).orElseThrow();
		String message = chatRequest.getMessage();

		//chat 저장하기
		Chat chat = addChat(chatRoom, sender, message);

		//chatRoom에 chat 넣기
		chatRoom.addChat(chat);

		return chat;
	}

	//나가기 채팅 만들기
	private Chat makeOutChat(ChatRequest chatRequest) {

		log.info("makeOutChat");
		ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId()).orElseThrow();
		Member sender = memberRepository.findById(chatRequest.getSenderId()).orElseThrow();

		deleteChatRoomMember(chatRoom, sender);

		//채팅방에 아무도 없으면 채팅방 및 채팅 삭제
		if (!chatRoomMemberRepository.existsByChatRoom(chatRoom)) {
			chatRoomRepository.delete(chatRoom);
		}

		//chat 저장하기
		Chat chat = addChat(chatRoom, sender, String.format(OUT_MESSAGE, sender.getNickName()));

		//chatRoom에 chat 넣기
		chatRoom.addChat(chat);

		return chat;
	}

	// 방id로 방의 모든 message 찾기
	@Transactional(readOnly = true)
	public List<ChatResponse> findAllMessagesByRoomId(Long chatRoomId) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();

		List<Chat> chats = chatRepository.findAllByChatRoom(chatRoom);
		return chats.stream()
			.map(ChatResponse::create)
			.toList();
	}

	//방 id로 방의 message 일부 찾기
	@Transactional(readOnly = true)
	public CustomResponse<Slice<ChatResponse>> findMessagesByRoomId(Long chatRoomId, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

		Slice<Chat> chats = chatRepository.findByChatRoomId(chatRoomId, pageable);

		return CustomResponse.ok(chats.map(ChatResponse::create));

	}

	// ChatRoomMember 삭제
	public void deleteChatRoomMember(ChatRoom chatRoom, Member member) {
		ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(chatRoom, member).orElseThrow();

		chatRoomMemberRepository.delete(chatRoomMember);
	}

	// 채팅방 유저 리스트 삭제
	public void deleteMember(Long roomId, Long memberId){
		ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();
		Member member = memberRepository.findById(memberId).orElseThrow();

		ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByChatRoomAndMember(room, member).orElseThrow();

		chatRoomMemberRepository.delete(chatRoomMember);
	}

}
