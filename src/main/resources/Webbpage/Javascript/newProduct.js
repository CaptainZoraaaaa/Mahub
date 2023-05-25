

function createProduct (){
    var data = {};
    data.productName = $('[name="productName"]').val();
    data.sellerName = sessionStorage.getItem("username");
    console.log(data.sellerName);
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