
let memberId = 1;
let receiverId = 2;
let chatRoomId = 1;

const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/ws-stomp'
});

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

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/sub/rooms/' + chatRoomId, (chat) => {
        let response = JSON.parse(chat.body);
        showMessage(response.senderNickName, response.message);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

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

function connect() {
    // makeRoom();
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

//방만들기
function makeRoom() {
    $.ajax({
        type: 'post',
        url: '/room',
        async: false,
        dataType: 'JSON',
        success: function (data) {
            chatRoomId = data;
        },
    });

    console.log(chatRoomId);
}

//입장하기
function sendEnterMessage() {
    stompClient.publish({
        destination: "/pub/" + chatRoomId,
        body: JSON.stringify({'messageType': "ENTER", "chatRoomId" : chatRoomId, "senderId" : memberId, "receiverId" : receiverId})
    });

}

//메세지 보내기
function sendTalkMessage() {
    stompClient.publish({
        destination: "/pub/" + chatRoomId,
        body: JSON.stringify({'messageType': "TALK", 'message': $("#message").val(), "chatRoomId" : chatRoomId, "senderId" : memberId, "receiverId" : receiverId})
    });
}

//나가기
function sendOutMessage() {
    stompClient.publish({
        destination: "/pub/" + chatRoomId,
        body: JSON.stringify({'messageType': "OUT", "chatRoomId" : chatRoomId, "senderId" : memberId, "receiverId" : receiverId})
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

function switchMember(id) {
    memberId = id;
}

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
// //저장된 채팅 불러오기
// function loadChat(chatList){
//     if(chatList != null) {
//         for(chat in chatList) {
//             if (chatList[chat].senderEmail == senderEmail) {
//                 $("#chatting").append(
//                     "<div class = 'chatting_own'><tr><td>" + chatList[chat].message + "</td></tr></div>"
//                 );
//             } else {
//                 $("#chatting").append(
//                     "<div class = 'chatting'><tr><td>" + "[" + chatList[chat].sender + "] " + chatList[chat].message + "</td></tr></div>"
//                 );
//             }
//         }
//     }
//     $('.col-md-12').scrollTop($('.col-md-12')[0].scrollHeight); // 채팅이 많아질시에 자동 스크롤
// }