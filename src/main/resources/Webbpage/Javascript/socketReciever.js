$(document).ready(function(){
    let socket = sessionStorage.getItem("socket");
    socket.onmessage = function(event) {
        alert(event);
    };
});