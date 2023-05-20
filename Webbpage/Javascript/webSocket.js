$(document).ready(function(){
    let socket = new WebSocket("wss://javascript.info/article/websocket/demo/hello");

    socket.onopen = function(e) {
    alert("[open] Connection established");
    alert("Sending to server");
    socket.send("My name is John");
};
});