var urlParam = new URLSearchParams(location.search);
var id;
var dataObject;




$(document).ready(function () {
    id = urlParam.get("id");
    console.log(id);
    $.ajax({
      method: "GET",
      url: "http://localHost:5500/getProduct/" + id,
      headers: {"Accept": "application/Json"}
    }).done(function (data){
        dataObject = data;
        document.querySelector("#image_area").style.backgroundImage = "url("+data.image+")";
        $('#file_chooser').append(data.productName);
        $('#productName').append(data.productName);
        $('#date').append(data.date);
        $('#price').append(data.price);
        // ========================================================================================
    })
});

function addToCart(){
    var jsonArray = localStorage.getItem("jsonArray");
    var array = JSON.parse(jsonArray);
    array[array.length] = dataObject;

    jsonArray = JSON.stringify(array);
    localStorage.setItem('jsonArray', jsonArray);
  
    array.forEach(element => {
        console.log(element.productId);
    });
}