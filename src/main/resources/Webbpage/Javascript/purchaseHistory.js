var user;
$(document).ready(function(){
    user = sessionStorage.getItem("username");
   
})
function search(){
    var data = {};
    data.username = user;
    data.start = document.getElementById("dateStart").value;
    data.end = document.getElementById("dateEnd").value;
    $.ajax({
        method: "POST", 
        url: "http://localhost:5500/getPurchaseHistory", 
        data: JSON.stringify(data),
        headers: {"Accept": "application/Json"}
    }).done(function(data1){
        console.log(data1);
        console.log(data1[0].datePurchased);

        for(i=0; i<data1.length; i++){
            console.log(i);
            $('#historyList').append('<li class="history-item"><span>Date: '+data1[i].datePurchased+' Product: '+data1[i].productName + '</span></li>');
        }
    });
}