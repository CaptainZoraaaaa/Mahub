$(document).ready(function () {
    let newOption = new Option('Option Text','Option Value');
    const select = document.querySelector('select'); 
    select.addEventListener("change", function (){
        updateBuyer();
    });
    select.add(newOption,undefined);
    
    
    
    $.ajax({
        method: "POST",
        url: "http://localHost:5500/register",
        data: JSON.stringify(sessionStorage.getItem("username")),
        headers: {"Accept": "application/Json"}
      }).done(function(data){
        
      });



      
    document.getElementById("img-left").src = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6erUYJRNBnfJi6Hxr-S-FWzlKf1AikfFx84-l3AW_iA&s";
    for(i= 0; i<5;i++){
        $('#list').append('<li>dasdasd  <button class="btn-accept"></button><button class="btn-reject"></button> </li>')
    }
    
})

function updateBuyer(){
    $('#list').empty(); 
    document.getElementById("img-left").src = "https://image.smythstoys.com/original/desktop/219919.jpg";
    for(i= 0; i<2;i++){
        $('#list').append('<li>dasdasd  <button class="btn-accept"></button><button class="btn-reject"></button> </li>')
    }
}