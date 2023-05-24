$(document).ready(function () {
    const jsonArray = localStorage.getItem("jsonArray");
    const array = JSON.parse(jsonArray);

    array.forEach(function(item) {
      var cartItem = document.createElement('div');
      cartItem.className = 'cart-item';
      
      var image = document.createElement('img');
      image.src = item.image;
      cartItem.appendChild(image);
      
      var itemName = document.createElement('span');
      itemName.textContent = item.name;
      cartItem.appendChild(itemName);
      
      cartContainer.appendChild(cartItem);
    });
  });