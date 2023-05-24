var listan = {} ;
$(document).ready(function () {
    var array =[];
    var user = {};
    user.message = sessionStorage.getItem("username");
    select.addEventListener("change", function (){
        updateBuyer();
    });



      $.ajax({
        method: "POST",
        url: "http://localHost:5500/getOffers",
        data: JSON.stringify(user),
        headers: {"Accept": "application/Json"}
      }).done(function(data){
        console.log(listan);
        listan = data;
        
        const select = document.querySelector('select'); 
        data.forEach(element => {
            var ok = true;
            for (let i = 0; i < array.length; i++) {
                if(element.productId == array[i]){
                    ok = false;
                }
                
            }
          if(ok===true){
                    let newOption = new Option(element.productName,element.productId);
                    select.add(newOption,undefined);
                    array[array.length] = element.productId;
          }
           
        });
        document.getElementById("img-left").src = data[0].image;
        for(i= 0; i<data.length;i++){
            if(data[0].productId == data[i].productId){
                var name = data[i].buyerName;
                $('#list').append('<li class="buyer-li" id="li'+i+'"><span class="text-container">'+data[i].buyerName+' has made an offer to buy your product!</span><div class="button-container"><button class="btn-accept" onclick="acceptOffer('+i+')")></button><button class="btn-reject" onclick="rejectOffer('+i+')")></button></div></li>')
            }
        }
      });
      
    
})

function updateBuyer(){
    $('#list').empty(); 
    var counter =-1
    var e = document.getElementById("select");
    var value = e.value;
    var counter =0;
    //-------------------------------------------------------------------------------------------
    for (let i = 0; i < listan.length; i++) {  
        if(listan[i].productId ==value){
            document.getElementById("img-left").src = listan[i].image
            $('#list').append('<li class="buyer-li" id="li'+i+'"><span class="text-container">'+listan[i].buyerName+' has made an offer to buy your product!</span><div class="button-container"><button class="btn-accept" onclick="acceptOffer('+i+')")></button><button class="btn-reject" onclick="rejectOffer('+i+","+counter+')")></button></div></li>')
        }
    }
}


function acceptOffer(index){
    var obj ={};
    var e = document.getElementById("select");
    obj.productId = e.value;
    obj.buyerName = listan[index].buyerName;


    $.ajax({
        method: "POST",
        url: "http://localHost:5500/acceptBuyRequest",
        data: JSON.stringify(obj),
        headers: {"Accept": "application/Json"}
      }).done(function(data){
        console.log(data);
        location.reload()
      });
    
}

function rejectOffer(index,counter){
    var obj ={};
    var e = document.getElementById("select");
    obj.productId = e.value;
    obj.buyerName = listan[index].buyerName;
    $.ajax({
        method: "POST",
        url: "http://localHost:5500/denyBuyRequest",
        data: JSON.stringify(obj),
        headers: {"Accept": "application/Json"}
      }).done(function(data){
        console.log(data);
        $("#li"+index).remove();
        // var listElements = document.querySelectorAll("#list li");
        // li = listElements[counter]
        // li.parentNode.removeChild(li);
        
      });
}
                    



