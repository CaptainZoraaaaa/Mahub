var user = {};
$(document).ready(function(){
    user.message = sessionStorage.getItem("username");
    $.ajax({
        method: "POST", 
        url: "http://localhost:5500/getInterests", 
        data: JSON.stringify(user),
        headers: {"Accept": "application/Json"}
    }).done(function(data1){
        for(var i=0; i<data1.length; i++){
            $('#interest-list').append('<li class="interest-item"><span>'+data1[i]+'</span><button class="interestRmvBtn" onclick="removeInterest(this)"></button></li>');
        }
    });

});
function addInterest(){
        var data = {};
        data.userName = sessionStorage.getItem("username");
        data.interest = document.getElementById("text").value;

        $.ajax({
            method: "POST", 
            url: "http://localhost:5500/addInterest", 
            data: JSON.stringify(data),
            headers: {"Accept": "application/Json"}
        }).done(function(data1){
        });
        $('#interest-list').append('<li class="interest-item"><span>'+data.interest+'</span><button class="interestRmvBtn" onclick="removeInterest(this)"></button></li>');
    };
function removeInterest(button){
    var data = {};
    var span = button.parentNode.querySelector('span');
    var spanText = span.textContent;
    data.userName = sessionStorage.getItem("username");
    data.interest = spanText;

    $.ajax({
        method: "POST", 
        url: "http://localhost:5500/removeInterest", 
        data: JSON.stringify(data),
        headers: {"Accept": "application/Json"}
    }).done(function(data1){
        var li = button.parentNode;
        var ul = li.parentNode;
        ul.removeChild(li);
    });

    
}