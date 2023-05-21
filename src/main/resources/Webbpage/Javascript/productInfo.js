var urlParam = new URLSearchParams(location.search);
var id = urlParam.get("id");

$(document).ready(function () {
    $.ajax({
      method: "GET",
      url: "http://localHost:5500/recipe/" + id,
      headers: {"Accept": "application/Json"}
    }).done(function (data){
      console.log(data)
      $('#recipetitle').append(data.title);
      $('#recipeimg').attr("src",data.image);
      $('#recipesummary').append(data.summary);
      for(i=0; i<data.extendedIngredients.length; i++){
          $('#ingredientlist').append('<li>'+data.extendedIngredients[i].original+'</li>');
      }
      // ========================================================================================
      for(i=0; i<data.analyzedInstructions[0].steps.length; i++){
          $('#steplist').append('<li>'+data.analyzedInstructions[0].steps[i].step+'</li>');
      } 
    })
  });