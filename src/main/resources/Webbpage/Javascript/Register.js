

function createUser (){
    var data = {};
    data.firstName = $('[name="firstName"]').val();
    data.lastName = $('[name="lastName"]').val();
    data.dateOfBirth = $('[name="dateOfBirth"]').val();
    data.email = $('[id="email"]').val();
    data.username = $('[name="username"]').val();
    data.password = $('[name="password"]').val();
    $.ajax({
        method:"POST",
        url:"http://localHost:5500/signup",
        data: JSON.stringify(data),
        headers: {"Accept": "application/Json"}
    }).done(function (){
      window.location.href = "http://127.0.0.1:5501/src/main/resources/Webbpage/Login.html";
    });
}