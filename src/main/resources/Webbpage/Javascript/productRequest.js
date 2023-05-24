const socket = new WebSocket("ws://" + "localHost" + ":" + "5500" + "/inbox?userid="+sessionStorage.getItem("username"));
var list = [];

$(document).ready(function(){

    document.getElementById("btn2").addEventListener("click", ()=>{
        send();
    })
    document.getElementById("btn").addEventListener("click", ()=>{
        add();
    })
    
    socket.onopen = function(e) {
        alert("[open] Connection established");
        alert("Sending to server");  
    };
    socket.onmessage = function (event){
        alert(event);
    }
});

function send(){
    var data = {};
    
    
    data.interests = list;
    var jsin = JSON.stringify(data);
    socket.send(jsin);
}
function add(){
    list[list.length]=$('#text').val();
    console.log(list);
}