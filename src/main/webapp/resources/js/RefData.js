function loadStates(){
    alert("loadStates");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            var stateSelect=document.getElementById("stateCd");
            newOption=document.createElement("OPTION");
            newOptionVal=document.createTextNode("Chandigarh");
            newOption.appendChild(newOptionVal);
            stateSelect.insertBefore(newOption,stateSelect.lastChild);
            /*statesJson=JSON.parse(this.responseText);
            alert(statesJson);
            for (x in statesJson){
                stateSelect.options[stateSelect.options.length] = new Option(statesJson[x].code, statesJson[x].name, false, false);
            }*/
        }
    };
    xhttp.open("GET", "RefDataServlet?type=states", true);
    xhttp.send();
    
}


