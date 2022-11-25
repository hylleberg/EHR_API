
async function fetchCPR() {

    let cprform = document.getElementById("cprform");
    const formData = new FormData(cprform);
    const object = Object.fromEntries(formData);
    console.log(object);

    const res = await fetch("api/cpr", {
        method: "POST",
        body: JSON.stringify(object),
        headers: {
            "content-type": "application/json"
        }


    })

    const json = await res.text();

    if(res.status === 201){
        const obj = JSON.parse(json);
        console.log(obj.fornavn)
        console.log(obj.efternavn)
        console.log(obj.adresse)
        document.getElementById("errorcode").innerHTML = ""
        document.getElementById("navnfelt").innerHTML = obj.fornavn + " " + obj.efternavn;
        document.getElementById("cprfelt").innerHTML = obj.cpr;
        document.getElementById("adressefelt").innerHTML = obj.adresse;

 //       var button = document.createElement("button");
 //       button.innerHTML = "Vælg patient";
 //       button.setAttribute('type', 'button')
 //       var divButton = document.getElementById("chooseButton")

 //       divButton.appendChild(button);


  //      button.onclick =   function() {
 //           loadHTML("sitecontent", "patienthome.html");
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

     }else{
        document.getElementById("errorcode").innerHTML = "Server fejl..."
        document.getElementById("cprfelt").innerHTML = "";
        document.getElementById("navnfelt").innerHTML = "";
        document.getElementById("adressefelt").innerHTML = "";


    }
    console.log(res)

}


async function fetchAftale(){
    let cpr3 = JSON.stringify({cpr: document.getElementById("cprfelt").innerHTML});

    console.log(cpr3)
    const res = await fetch("api/aftale", {
        method: "POST",
        body: cpr3,
        headers: {
            "content-type": "application/json"
        }
    })
    console.log(res)
    const json = await res.text();

    if(res.status === 201) {
        const obj = JSON.parse(json);

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


            };
            row.appendChild(btn);
            table.appendChild(row);

        });
        myTable.appendChild(table);

    }else {
        document.getElementById("errorfelt3").innerHTML = "Der skete en fejl, kunne ikke finde aftaler";
    }
}