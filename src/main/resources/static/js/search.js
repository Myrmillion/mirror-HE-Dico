
function search() {
  // Declare variables
  let input = document.getElementById('searchBar');
  let filter = input.value.toUpperCase();
  let tags = document.getElementById("blockTags");
  let labels = tags.querySelectorAll("label");
  // Loop through all list items, and hide those who don't match the search query
  for (i = 0; i < labels.length; i++) {
    let txtValue = labels[i].innerText;
    let forId = labels[i].htmlFor;
    let checkboxInput = document.getElementById(forId);
    if (txtValue.toUpperCase().indexOf(filter) > -1 && filter.length>0) {
      labels[i].style.display = "";
      checkboxInput.style.display="";
    } else {
      if(!checkboxInput.checked){
        labels[i].style.display = "none";
        checkboxInput.style.display="none";
      }
    }
  }
}