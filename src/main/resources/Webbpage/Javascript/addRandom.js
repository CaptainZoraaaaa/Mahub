
var offset = 0;
var productIds = [];

$(document).ready(function () {
  document.getElementById("search-btn").addEventListener("click", ()=>{
    sendItems();
  });
  
  getStart();

});

function showDetails(index){
  console.log(productIds[index]);
  window.location.href = 'productInfo.html' + '?id=' + productIds[index]; 
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
      productIds[i] = data[i].productId;
      offset++;
    }
  });
}

function sendItems(){
  var data = {};
  data.name = $("#search-input").val();
  data.proceRangeMin = $("#minField").val();
  data.priceRangeMax = $("#maxField").val();
  var e = document.getElementById("quality_box");

  data.condition = e.value; 

  console.log(data);
  $.ajax({
    method: "POST",
    url: "http://localHost:5500/searchProduct",
    data: JSON.stringify(data),
    headers: {"Accept": "application/Json"}
  }).done(function(data){
      console.log(data);
      offset=0;
      $('#randomProduct').empty();
      for(i=offset; i<data.length+offset; i++){
          $('#randomProduct').append('<div class="flex-item" id="d'+i+'" > <img src="'+data[i].image+'" alt="" id="img'+data.s+'"> <p id="p'+i+'">'+data[i].productName+'</p> <button type="button" onclick="showDetails('+i+')">Show details</button></div>');
          productIds[i] = data[i].productId;
          offset++;
      } 
  });
}