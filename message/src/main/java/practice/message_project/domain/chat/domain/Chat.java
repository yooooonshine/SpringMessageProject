package practice.message_project.domain.chat.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import practice.message_project.domain.Member.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Chat {

	@Id
	@Column(name = "CHAT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHAT_ROOM_ID")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private ChatRoom chatRoom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID")
	private Member sender;

	@NotNull
	@Column(length = 1000)
	private String message;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime sendDate;

	private Chat(ChatRoom chatRoom, Member sender, String message) {
		this.chatRoom = chatRoom;
		this.sender = sender;
		this.message = message;
	}

	public static Chat create(ChatRoom chatRoom, Member sender, String message) {
		return new Chat(chatRoom,sender, message);
	}
}
