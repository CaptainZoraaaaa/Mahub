var urlParam = new URLSearchParams(location.search);
var id;
var dataObject;




$(document).ready(function () {
    id = urlParam.get("id");
    console.log(id);
    var data ={};
    data.productId = id;
    $.ajax({
      method: "POST",
      data: JSON.stringify(data),
      url: "http://localHost:5500/getProductDetails",
      headers: {"Accept": "application/Json"}
    }).done(function (data){
        dataObject = data;
        document.querySelector("#image_area").style.backgroundImage = "url("+data.image+")";
        $('#file_chooser').append(data.productName);
        $('#productName').append("Product: "+data.productName);
        $('#date').append("Date added: "+data.date);
        $('#price').append("Price: "+data.price+" $");
        // ========================================================================================
    })
});

function addToCart(){
    var jsonArray = localStorage.getItem("jsonArray");
    var array = JSON.parse(jsonArray);
    if (array != null) {
        array[array.length] = dataObject;
    }
    else{
        array = [];
        array[array.length] = dataObject;
    }

    jsonArray = JSON.stringify(array);
    localStorage.setItem('jsonArray', jsonArray);
  
    array.forEach(element => {
        console.log(element.productId);
    });
}