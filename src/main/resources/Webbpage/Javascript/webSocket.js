$(document).ready(function(){
    sessionStorage.setItem("yoo",2)
    let socket = new WebSocket("ws://" + "localHost" + ":" + "5500" + "/inbox/13");

    socket.onopen = function(e) {
        alert("[open] Connection established");
        alert("Sending to server");
        socket.send("hej");
    };
});