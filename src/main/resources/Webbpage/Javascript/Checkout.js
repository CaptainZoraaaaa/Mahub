$(document).ready(function () {
    var jsonArray = localStorage.getItem("jsonArray");
    var array = JSON.parse(jsonArray);
    console.log(array);
    var cartContainer = document.getElementById('cart-items');

    var counter =0;
    array.forEach(function(item) {
     
      var cartItem = document.createElement('div');
      cartItem.className = 'cart-item';
      cartItem.id = counter;

      var btn = document.createElement('button');
      btn.className = 'remove-btn';
      btn.onclick = function(){
        var div = btn.parentNode;
        var something = div.parentNode;
        var id = div.id;
        if(id > -1){
          array.splice(id,1);
        }
        something.removeChild(div);
    
 
        jsonArray = JSON.stringify(array);
        localStorage.setItem('jsonArray', jsonArray);
      };

      cartItem.appendChild(btn);
      
      var image = document.createElement('img');
      image.src = item.image;
      cartItem.appendChild(image);
      
      var itemName = document.createElement('div');
      itemName.className = 'item-name';
      itemName.textContent = item.productName;
      cartItem.appendChild(itemName);

      var cost = document.createElement('div');
      cost.className = 'cost-class';
      cost.textContent = item.price;
      cartItem.appendChild(cost);

      counter++;

      cartContainer.appendChild(cartItem);
    });
  });

  function sendItems(){
    var data = {};
    data.buyerName = sessionStorage.getItem("username");
    var jsonArray = localStorage.getItem("jsonArray");
    var array = [];
    array = JSON.parse(jsonArray)
    var finalArray = [];
    for (let i = 0; i < array.length; i++) {
        finalArray[i]=array[i].productId;
    }
    data.productIds = finalArray;

    $.ajax({
      method:"POST",
      url:"http://localHost:5500/buyRequest",
      data: JSON.stringify(data),
      headers: {"Accept": "application/Json"}
    }).done(function(data){
        alert(data);
    });
  }
