
let senderId = 1;
let receiverId = 2;
let chatRoomId = 1;

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws-stomp'
});

window.onload = function () {
    getChatRooms();
};

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

//stompClient가 활성화되면 같이 실행되는 메서드
stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    for (let i = 0; i < memberChatRoomIds.length; i++) {
        stompClient.subscribe('/sub/rooms/' + memberChatRoomIds[i], function(chat) {
            console.log("connectMessageData: " + chat);
            let response = JSON.parse(chat.body);
            showMessage(response.data.senderNickName, response.data.message);
        });
    }
};


stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};


// function connect() {
//     var socket = new SockJS("/ws-stomp");
//     stompClient = Stomp.over(socket);
//     stompClient.connect({}, function (frame) {
//         setConnected(true);
//         console.log('Connected: ' + frame);
//         loadChat(chatList)  //저장된 채팅 불러오기
//
//         //구독
//         stompClient.subscribe('/room/'+chatRoomId, function (chatMessage) {
//             showChat(JSON.parse(chatMessage.body));
//         });
//     });
// }

// 멤버 선택하기
// memberId를 가져올 수 없어 임의로 만든 기능이니 추후 삭제 요망
function switchMember(id) {
    senderId = id;
    getChatRooms();
}

// 상대 멤버 선택하기
// memberId를 가져올 수 없어 임의로 만든 기능이니 추후 삭제 요망
function switchReceiver(id) {
    receiverId = id;
}

// 채팅방 목록 가져오기
let memberChatRoomIds = [];
function getChatRooms() {
    $.ajax({
        type: 'get',
        url: '/rooms/member/' + senderId,
        async: false,
        dataType: 'JSON',
        contentType : "application/json",
        success: function (data) {
            memberChatRoomIds = data.data.roomIds;
        },
    });
    showRooms();
}

// 채팅방 목록 띄우기
function showRooms() {
    $("#room-group").empty();
    for (let i = 0; i < memberChatRoomIds.length; i++) {
        let text = '<button class="btn btn-primary" type="submit" onclick="selectRoom(' + memberChatRoomIds[i] + ')">' + memberChatRoomIds[i] + '번 채팅방</button>';
        $("#room-group").append(text);
    }

}



// 방 들어가기
function selectRoom(roomId) {
    chatRoomId = roomId;
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}



//방만들기
function makeRoom() {
    let data = {
        'senderId': senderId,
        'receiverId' : receiverId,
    };

    $.ajax({
        type: 'post',
        url: '/room',
        async: true,
        data: JSON.stringify(data),
        dataType: 'JSON',
        contentType : "application/json",
        success: function (data) {
            chatRoomId = data.data.roomId;
        },
    });
}

//메세지 보내기
function sendTalkMessage() {
    console.log("message: " + $("#message").val());
    console.log("chatRoomId: " + chatRoomId)
    console.log("senderId: " + senderId);
    console.log("receiverId: " + receiverId);

    stompClient.publish({
        destination: "/pub/" + chatRoomId,
        body: JSON.stringify({'messageType': "TALK", 'message': $("#message").val(), "chatRoomId" : chatRoomId, "senderId" : senderId, "receiverId" : receiverId})
    });
}

//나가기
function sendOutMessage() {
    stompClient.publish({
        destination: "/pub/" + chatRoomId,
        body: JSON.stringify({'messageType': "OUT", "chatRoomId" : chatRoomId, "senderId" : senderId, "receiverId" : receiverId})
    });
}



function showMessage(memberName,message) {
    console.log(message);
    $("#greetings").append("<tr><td>" + memberName + ":" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
});




// //보낸 채팅 보기
// function showChat(chatMessage) {
//     if (chatMessage.senderEmail == senderEmail) {
//         $("#chatting").append(
//             "<div class = 'chatting_own'><tr><td>" + chatMessage.message + "</td></tr></div>"
//         );
//     } else {
//         $("#chatting").append(
//             "<div class = 'chatting'><tr><td>" + "[" + chatMessage.sender + "] " + chatMessage.message + "</td></tr></div>"
//         );
//     }
//     $('.col-md-12').scrollTop($('.col-md-12')[0].scrollHeight);
// }
//


//채팅방 채팅 내역 가져오기
function getChats() {
    let data = {
        'senderId': senderId,
    };

    $.ajax({
        type: 'post',
        url: '/room',
        async: true,
        data: JSON.stringify(data),
        dataType: 'JSON',
        contentType : "application/json",
        success: function (data) {
            chatRoomId = data.data.roomId;
        },
    });
}

//채팅내역 쓰기
function loadChat(chatList){
    if(chatList != null) {
        for(chat in chatList) {
            if (chatList[chat].senderEmail == senderEmail) {
                $("#chatting").append(
                    "<div class = 'chatting_own'><tr><td>" + chatList[chat].message + "</td></tr></div>"
                );
            } else {
                $("#chatting").append(
                    "<div class = 'chatting'><tr><td>" + "[" + chatList[chat].sender + "] " + chatList[chat].message + "</td></tr></div>"
                );
            }
        }
    }
    $('.col-md-12').scrollTop($('.col-md-12')[0].scrollHeight); // 채팅이 많아질시에 자동 스크롤
}