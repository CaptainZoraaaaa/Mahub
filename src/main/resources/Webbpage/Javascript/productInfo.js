var urlParam = new URLSearchParams(location.search);


$(document).ready(function () {
    var id = urlParam.get("id");
    console.log(id);
    $.ajax({
      method: "GET",
      url: "http://localHost:5500/getProduct/" + id,
      headers: {"Accept": "application/Json"}
    }).done(function (data){
        console.log(data);
        document.querySelector("#image_area").style.backgroundImage = "url("+data.image+")";
        $('#file_chooser').append(data.productName);
        $('#productName').append(data.productName);
        $('#date').append(data.date);
        $('#price').append(data.price);
        // ========================================================================================
    })
  });