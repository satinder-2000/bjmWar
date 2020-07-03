function makeNav(){
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("top").innerHTML=this.responseText;
        }
    };
    xhttp.open("GET", "TopServlet", true);
    xhttp.send();
    
    
    
}


