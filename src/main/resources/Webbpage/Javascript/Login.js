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
            sessionStorage.setItem("firstName", data1.firstName);
            sessionStorage.setItem("username", data1.username);
            window.location.href = "LoginIndex.html";
        }
      });
}