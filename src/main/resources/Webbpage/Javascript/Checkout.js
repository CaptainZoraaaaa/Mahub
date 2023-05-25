$(document).ready(function () {
    const jsonArray = localStorage.getItem("jsonArray");
    const array = JSON.parse(jsonArray);

    var cartContainer = document.getElementById('cart-items');

    console.log(array);

    array.forEach(function(item) {
      var cartItem = document.createElement('div');
      cartItem.className = 'cart-item';

      var btn = document.createElement('button');
      btn.className = 'remove-btn';
      btn.onclick = function(){
        var div = btn.parentNode;
        var something = div.parentNode;
        something.removeChild(div);
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

      cartContainer.appendChild(cartItem);
    });
  });
