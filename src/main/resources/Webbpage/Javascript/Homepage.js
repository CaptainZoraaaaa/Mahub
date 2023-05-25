$(document).ready(function () {
      $.ajax({
        method: "GET",
        url: "http://localHost:5500/getLatest",
        headers: {"Accept": "application/Json"}
      }).done(function(data){
           var img = {};
           for(var i = 0; i<data.length;i++){
            img = document.getElementById("img"+i);
            img.src = data[i].image;
           }
        });
})