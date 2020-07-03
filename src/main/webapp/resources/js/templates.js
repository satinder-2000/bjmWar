function loadTemplate(templateName){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("content").innerHTML=this.responseText;
            loadStates();
        }
    };
    xhttp.open("GET", "ResourceServlet?type="+templateName, true);
    xhttp.send();
}

function loadStates(){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var stateSelect=document.getElementById("stateCd");
            
            statesJson=JSON.parse(this.responseText);
            for (var i = 0; i < statesJson.length; i++) {
            // POPULATE SELECT ELEMENT WITH JSON.
            stateSelect.innerHTML = stateSelect.innerHTML +
                '<option value="' + statesJson[i]['code'] + '">' + statesJson[i]['name'] + '</option>';
            }
        }
    };
    xhttp.open("GET", "RefDataServlet?type=states", true);
    xhttp.send();
    
}


