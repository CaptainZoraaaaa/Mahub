$(document).ready(function () {
    document.getElementById("new-btn").addEventListener("click", () =>{
        getStart();
    });
});

    function getStart(){
        $.ajax({
            method: "GET",
            url: "http://localHost:5500/",
            headers: {"Accept": "application/Json"}
        }).done(function (data){
            
        })
    }