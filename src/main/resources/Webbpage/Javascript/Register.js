

function createUser (){
  var data = {};
  if($('[name="firstName"]').val() == ""){
    alert("Not allowed blank input");
    window.location.href = "Register.html";
  }
  data.firstName = $('[name="firstName"]').val();
  data.lastName = $('[name="lastName"]').val();
  data.dateOfBirth = $('[name="dateOfBirth"]').val();
  data.email = $('[id="email"]').val();
  data.username = $('[name="username"]').val();
  data.password = $('[name="password"]').val();
  console.log(data);
  $.ajax({
    method: "POST",
    url: "http://localHost:5500/register",
    data: JSON.stringify(data),
    headers: {"Accept": "application/Json"}
  }).done(function(data1){
    console.log(data1);
    alert(data1.message)
    window.location.href = "Login.html";
    
  });

}