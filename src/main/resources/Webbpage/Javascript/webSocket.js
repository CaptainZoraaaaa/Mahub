const socket= new WebSocket("ws://" + "localHost" + ":" + "5500" + "/inbox?userid="+sessionStorage.getItem("username"));
$(document).ready(function(){
    console.log(sessionStorage.getItem("username"));
    socket.onmessage = function (event){
        console.log(event.data);
        alert(event.data);
    }
});
