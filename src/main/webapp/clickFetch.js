// Persist data on accidental refreshs
console.log("script running")
if (sessionStorage.getItem("cpr") || sessionStorage.getItem("fornavn") || sessionStorage.getItem("efternavn")) {
    // Restore the contents of the text field
    console.log("Restoring")
    document.getElementById("navnfelt_sidebar").innerHTML = sessionStorage.getItem("fornavn") +" "+sessionStorage.getItem("efternavn");
    document.getElementById("cprfelt_sidebar").innerHTML = sessionStorage.getItem("cpr");

}

function clearSession(){
    sessionStorage.clear();

}

async function fetchCPR() {

    let cprform = document.getElementById("cprform");
    const formData = new FormData(cprform);
    const object = Object.fromEntries(formData);
    console.log(object);

    let bearer = "Bearer " + localStorage.getItem("token")
    console.log(localStorage.getItem("token"));

    const res = await fetch("api/" + object.cpr, {
        method: "GET",
        headers: {
            'Authorization' : bearer,
        },
    })

    const json = await res.text();

    if(res.status === 201){
        const obj = JSON.parse(json);
        console.log(obj.fornavn)
        console.log(obj.efternavn)
        console.log(obj.adresse)
        document.getElementById("errorcode").innerHTML = ""
        document.getElementById("navnfelt").innerHTML = obj.fornavn + " " + obj.efternavn;
        document.getElementById("cprfelt").innerHTML = "(" + obj.cpr + ")";

      var button = document.createElement("button");
      button.innerHTML = "Vælg patient";
      button.setAttribute('type', 'button');
      button.classList.add('button');
        button.onclick = function(){
            sessionStorage.setItem("fornavn", obj.fornavn);
            sessionStorage.setItem("efternavn", obj.efternavn);
            sessionStorage.setItem("cpr", obj.cpr);
            document.getElementById("navnfelt_sidebar").innerHTML = obj.fornavn + " " + obj.efternavn;
            document.getElementById("cprfelt_sidebar").innerHTML = obj.cpr;
            document.getElementById("addfelt_sidebar").innerHTML = obj.adresse;
            document.getElementById("successcode").innerHTML = "Du har nu valgt " + sessionStorage.getItem("fornavn") + " " + sessionStorage.getItem("efternavn") + ".";
      };

      var divButton = document.getElementById("chooseButton")
        divButton.innerHTML = "";
      divButton.appendChild(button);



  //      button.onclick =   function() {
 //           loadHTML("sitecontent", "journal.html");
  //          document.getElementById("navnfelt2").innerHTML = obj.fornavn + " " + obj.efternavn;
  //          document.getElementById("cprfelt2").innerHTML = obj.cpr;
 //           document.getElementById("adressefelt2").innerHTML = obj.adresse;
  //      return false;
  //      };



  //  document.getElementById("adressefelt").innerHTML = obj.adresse;

     }else if(res.status === 400){
        document.getElementById("errorcode").innerHTML = "CPR findes ikke"
        document.getElementById("cprfelt").innerHTML = "";
        document.getElementById("navnfelt").innerHTML = "";
        document.getElementById("adressefelt").innerHTML = "";

     }else if(res.status === 401){
        document.getElementById("errorcode").innerHTML = "Adgang nægtet."
        document.getElementById("cprfelt").innerHTML = "";
        document.getElementById("navnfelt").innerHTML = "";
        document.getElementById("adressefelt").innerHTML = "";

    }else if(res.status === 403){
        document.getElementById("errorcode").innerHTML = "Du har ikke adgang til denne funktion."
        document.getElementById("cprfelt").innerHTML = "";
        document.getElementById("navnfelt").innerHTML = "";
        document.getElementById("adressefelt").innerHTML = "";

    }else{
        document.getElementById("errorcode").innerHTML = "Server fejl..."
        document.getElementById("cprfelt").innerHTML = "";
        document.getElementById("navnfelt").innerHTML = "";
        document.getElementById("adressefelt").innerHTML = "";


    }
    console.log(res)

}

function changeVisibility(showDiv, hideDiv){
    document.getElementById(showDiv).classList.remove("hidden");
    document.getElementById(hideDiv).classList.add("hidden")

}

async function fetchAftale(){
    let cpr3 = JSON.stringify({cpr: sessionStorage.getItem("cpr")});

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/"+ sessionStorage.getItem("cpr"), {
        method: "GET",
        headers: {
            'Authorization' : bearer,
        }
    })
    console.log(res)
    const json = await res.text();

    if(res.status === 201) {
        document.getElementById("errorfelt3").innerHTML = "";
        const obj = JSON.parse(json);

        document.getElementById("aftaleTable").innerHTML = "";

        let myTable = document.querySelector('#aftaleTable');

        let headers = ['Startdato', 'Varighed', 'Notat', 'Gå til'];

        let table = document.createElement('table');
        let headerRow = document.createElement('tr');
        headers.forEach(headerText => {
            let header = document.createElement('th');
            let textNode = document.createTextNode(headerText);
            header.appendChild(textNode);
            headerRow.appendChild(header);
        });

        table.appendChild(headerRow);

        obj.forEach(objekt => {
            let row = document.createElement('tr');
            Object.values(objekt).forEach(text => {
                let cell = document.createElement('td');
                let textNode = document.createTextNode(text);
                cell.appendChild(textNode);
                row.appendChild(cell);

            })
            let btn = document.createElement('button');
            btn.innerHTML = "Vælg aftale";
            btn.onclick = function () {
                sessionStorage.setItem("aftaleID", 1); // Unique aftaleID here!
                changeVisibility("visAftale", "aftaleListContainer");

            };
            row.appendChild(btn);
            table.appendChild(row);

        });
        myTable.appendChild(table);

    }else if(res.status === 401){
        document.getElementById("errorfelt3").innerHTML = "Adgang nægtet."

    }else if(res.status === 403){
        document.getElementById("errorfelt3").innerHTML = "Du har ikke adgang til denne funktion."

    }else{
        document.getElementById("errorfelt3").innerHTML = "Der skete en fejl, kunne ikke finde aftaler";
    }
}