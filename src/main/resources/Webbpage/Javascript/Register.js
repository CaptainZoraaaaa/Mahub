
let socket = new WebSocket("wss:http://localHost:5500/inbox/13");
socket.onopen = function(e) {
  alert("[open] Connection established");
  alert("Sending to server");
  socket.send("My name is John");
};