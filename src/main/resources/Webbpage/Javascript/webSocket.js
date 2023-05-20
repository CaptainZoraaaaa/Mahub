$(document).ready(function(){
    
    let socket = new WebSocket("ws://" + "localHost" + ":" + "5500" + "/inbox");

    socket.onopen = function(e) {
        alert("[open] Connection established");
        alert("Sending to server");
        socket.send("hej");
    };
});