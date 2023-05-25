const socket= new WebSocket("ws://" + "localHost" + ":" + "5500" + "/inbox?userid="+sessionStorage.getItem("username"));
$(document).ready(function(){
    console.log(sessionStorage.getItem("username"));
    document.getElementById("btn2").addEventListener("click", ()=>{
        send();
    })
    socket.onmessage = function (event){
        console.log(event.data);
        alert(event.data);
    }
});
