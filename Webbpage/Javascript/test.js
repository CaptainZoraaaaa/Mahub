
$(document).ready(function () {
    document.getElementById("new-btn").addEventListener("click", () =>{
        getStart();
    });
});
function getStart(){
  $.ajax({
      method: "GET",
      url: "http://localHost:5500/testPage",
      headers: {"Accept": "application/Json"}
  }).done(function (data){
    console.log(data.s);
    for(i=0; i<8; i++){
      $('#randomProduct').append('<div class="flex-item" id="d'+i+'" > <img src="'+"images/webpic.jpg"+'" alt="Image" id="img'+data.s+'"> <p id="p'+i+'">'+"TITLE"+'</p></div>');
    }
  })
}