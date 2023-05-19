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
    data.file = $('[id="file_chooser"]').val();
    data.name = $('[name="productName"]').val();
    
    $.ajax({
        method:"POST",
        url:"http://localHost:5500/addProduct",
        data: JSON.stringify(data),
        headers: {"Accept": "application/Json"}
    }).done({

    })
    console.log(data.file);
    console.log(data.name);
}