package practice.message_project.domain.chat.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import practice.message_project.domain.Member.domain.Member;
import practice.message_project.domain.Member.repository.MemberRepository;
import practice.message_project.domain.chat.domain.Chat;
import practice.message_project.domain.chat.domain.ChatRoom;
import practice.message_project.domain.chat.dto.request.ChatRequest;
import practice.message_project.domain.chat.dto.response.ChatResponse;
import practice.message_project.domain.chat.dto.response.RoomResponse;
import practice.message_project.domain.chat.repository.ChatRepository;
import practice.message_project.domain.chat.repository.ChatRoomRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatService {
	private final String OUT_MESSAGE = "%s님이 채팅방에서 나가셨습니다.";
	private final String UNKNOWN_MAN = "(알 수 없음)";

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRepository chatRepository;
	private final MemberRepository memberRepository;

	//멤버가 참여하는 방 찾기
	@Transactional(readOnly = true)
	public Slice<RoomResponse> findChatRoomsByMemberId(Long memberId, int pageNumber, int pageSize) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

		Slice<ChatRoom> chatRooms = chatRoomRepository.findAllByFromMemberOrToMember(member, member, pageable);

		return chatRooms.map(chatRoom -> {
			Long chatRoomId = chatRoom.getId();
			String roomName = findChatRoomName(member, chatRoom);
			String message = findLastMessage(chatRoom);

			return RoomResponse.create(chatRoomId, roomName, message);
		});
	}

	//채팅방에 메세지 있는 지 확인하고, 없으면 null, 있으면 message리턴
	private String findLastMessage(ChatRoom chatRoom) {
		return chatRepository.findTopByChatRoomOrderById(chatRoom)
			.map(Chat::getMessage)
			.orElse(null);
	}

	//채팅방 이름 찾기
	private String findChatRoomName(Member sender, ChatRoom chatRoom) {

		// 메세지는 보내는 사람과 다른 사람의 이름이 채팅방 이름
		if (chatRoom.getFromMember() == null || chatRoom.getToMember() == null) {
			return UNKNOWN_MAN;
		} else if (chatRoom.getFromMember() == sender) {
			return chatRoom.getToMember().getNickName();
		} else {
			return chatRoom.getFromMember().getNickName();
		}
	}

	//방 만들고 입장하기
	public RoomResponse enterRoom(Long senderId, Long receiverId) {
		Member sender = memberRepository.findById(senderId).orElseThrow();
		Member receiver = memberRepository.findById(receiverId).orElseThrow();

		//방 만들기
		ChatRoom chatRoom = addRoom(sender, receiver);

		return RoomResponse.create(chatRoom.getId(), sender.getNickName(), null);
	}

	//방 만들기
	private ChatRoom addRoom(Member fromMember, Member toMember) {
		ChatRoom chatRoom = ChatRoom.create(fromMember, toMember);

		return chatRoomRepository.save(chatRoom);
	}

	private Chat addChat(ChatRoom chatRoom, Member sender, String message) {

		Chat chat = Chat.create(chatRoom, sender, message);

		return chatRepository.save(chat);
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

		ChatRoom chatRoom = chatRoomRepository.findById(chatRequest.getChatRoomId()).orElseThrow();
		Member sender = memberRepository.findById(chatRequest.getSenderId()).orElseThrow();

		//채팅방 나가기
		leaveChatRoom(chatRoom, sender);

		//채팅방에 아무도 없으면 채팅방 및 채팅 삭제
		if (isEmptyRoom(chatRoom)) {
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
	public Slice<ChatResponse> findAllMessagesByRoomId(Long chatRoomId, int pageNumber, int pageSize) {
		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

		Slice<Chat> chats = chatRepository.findAllByChatRoom(chatRoom, pageable);
		return chats
			.map(ChatResponse::create);
	}

	//방 id로 방의 message 일부 찾기
	@Transactional(readOnly = true)
	public Slice<ChatResponse> findMessagesByRoomId(Long chatRoomId, int pageNumber, int pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));

		Slice<Chat> chats = chatRepository.findByChatRoomId(chatRoomId, pageable);

		return chats.map(ChatResponse::create);
	}

	// 채팅방 나가기
	private void leaveChatRoom(ChatRoom chatRoom, Member member) {
		if (member == chatRoom.getFromMember()) {
			chatRoom.deleteFromMember();
		}
		chatRoom.deleteToMember();
	}

	// 채팅방 삭제 검사
	private boolean isEmptyRoom(ChatRoom chatRoom) {
		return (chatRoom.getToMember() == null && chatRoom.getFromMember() == null);
	}

}
