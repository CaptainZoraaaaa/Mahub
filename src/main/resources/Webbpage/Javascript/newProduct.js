const image_input = document.querySelector("#file_chooser");

image_input.addEventListener("change", function(){
    console.log(image_input.value);
    const reader = new FileReader();
    reader.addEventListener("load", () =>{
        document.querySelector("#image_area").style.backgroundImage = `url(${reader.result})`
    })
    reader.readAsDataURL(this.files[0]);
});

function createProduct (){
    var data = {};
    data.productName = $('[name="productName"]').val();
    data.sellerName = sessionStorage.getItem("user");
    data.price = $('[name="price"]').val();
    data.date = $('[id="date"]').val();
    data.image = $('[id="file_chooser"]').val();
    data.condition = $('[name="quality"]').val();
    data.colour = $('[name="color"]').val();

    $.ajax({
        method:"POST",
        url:"http://localHost:5500/addProduct",
        data: JSON.stringify(data),
        headers: {"Accept": "application/Json"}
    }).done(function(data){
        alert(data);
    });
 
}