
$(document).ready(function () {
  console.log("text:" + sessionStorage.getItem('yoo'));
    getStart();
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
    for(i=0; i<9; i++){
      $('#randomProduct').append('<div class="flex-item" id="d'+i+'" > <img src="'+"images/webpic.jpg"+'" alt="Image" id="img'+data.s+'"> <p id="p'+i+'">'+"TITLE"+'</p></div>');
    }
  })
}