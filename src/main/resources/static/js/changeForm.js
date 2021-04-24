function changeForm()
{
	let checkbox = document.getElementById("switch");
	let searchByWordDiv = document.getElementById("searchByWord");
	let searchByTagDiv = document.getElementById("searchByTag");

	if(checkbox.checked)
	{
		searchByWordDiv.style.display = "none";
		searchByTagDiv.style.display="";
	}
	else
	{
		searchByWordDiv.style.display = "";
		searchByTagDiv.style.display="none";
	}
	
}