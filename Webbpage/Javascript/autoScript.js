var counter = 1;
document.getElementById('btn'+counter).checked = true;
setInterval(function(){
    document.getElementById('btn'+counter).checked = true;
    counter++;
    if(counter>4){
        counter=1;
    }
}, 5000);

