
function loadHTML(id,filename){
    document.getElementById("succescode").innerHTML = "";
    document.getElementById("errorfelt3").innerHTML = "";

    console.log(id);
    console.log(filename);
    let xhttp;
    let element = document.getElementById(id);
    let file = filename;
    if(file){
        xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function(){
            if (this.readyState == 4) {
                if(this.status == 200) {element.innerHTML = this.responseText;}
                if (this.status == 404) {element.innerHTML = "Page not found";}
            }
        }
        xhttp.open("GET", file, true);
        xhttp.send();
    }

}