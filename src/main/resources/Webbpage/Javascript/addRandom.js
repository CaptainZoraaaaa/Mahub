
var offset = 0;
var productIds = [];

$(document).ready(function () {
    getStart();

});

function showDetails(index){
  console.log(recipeIds[index]);
  window.location.href = 'prductInfo.html' + '?id=' + productIds[index]; 
} 

function getStart(){
  $.ajax({
      method: "GET",
      url: "http://localHost:5500/getProducts/?offset="+offset,
      headers: {"Accept": "application/Json"}
  }).done(function (data){
    console.log(data);
    for(i=offset; i<data.length+offset; i++){
      $('#randomProduct').append('<div class="flex-item" id="d'+i+'" > <img src="'+data[i].image+'" alt="Image" id="img'+data.s+'"> <p id="p'+i+'">'+data[i].productName+'</p> <button type="button" onclick="showDetails('+i+')">Show details</button></div>');
      productIds[i] = data[i].id;
      offset++;
    }
  })
}