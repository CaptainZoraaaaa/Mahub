function login(){
    const data = {}
    data.username = $('[name="username"]').val();
    data.password = $('[name="password"]').val();
    $.ajax({
        method: "POST",
        url: "http://localHost:5500/login",
        data: JSON.stringify(data),
        headers: {"Accept": "application/Json"}
      }).done(function(data1){
        console.log(data1);
        if(data!=null){
            window.location.href = "index.html";
        }
      });
}