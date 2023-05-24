const socket= new WebSocket("ws://" + "localHost" + ":" + "5500" + "/inbox?userid="+sessionStorage.getItem("username"));
$(document).ready(function(){
    document.getElementById("btn2").addEventListener("click", ()=>{
        send();
    })
    sessionStorage.setItem("socket",socket);
    socket.onopen = function(e) {
        alert("[open] Connection established");
        alert("Sending to server");
        
    };
});

function send(){
    var data = {};
    var list = ["Saab", "Volvo", "BMW"];
    
    data.interests = list;
    var jsin = JSON.stringify(data);
    socket.send(jsin);
}
