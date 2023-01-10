// Persist data on accidental refreshs //
//*******************************************************************************************************************//
console.log("script running")
if (sessionStorage.getItem("cpr") || sessionStorage.getItem("firstname") || sessionStorage.getItem("lastname")) {
    // Restore the contents of the text field
    console.log("Restoring")
//    document.getElementById("navnfelt_sidebar").innerHTML = sessionStorage.getItem("firstname") +" "+sessionStorage.getItem("lastname");
 //   document.getElementById("cprfelt_sidebar").innerHTML = sessionStorage.getItem("cpr");

}

function clearSession(){
    sessionStorage.clear();

}
//*******************************************************************************************************************//

// Removing padding for view //
//*******************************************************************************************************************//

    let userText = sessionStorage.getItem("user").substring(1, (sessionStorage.getItem("user").length)-1)
    let roleText = sessionStorage.getItem("role").substring(1, (sessionStorage.getItem("role").length)-1)
document.getElementById("userID").innerHTML =  userText;
document.getElementById("roleID").innerHTML = roleText;
//*******************************************************************************************************************//


//Check if user is patient //
//*******************************************************************************************************************//

if(sessionStorage.getItem("role") == "?patient?"){
    document.getElementById("findPatient").classList.add("hidden");
    fetchPatientData();

}
async function fetchPatientData() {
    let object = {};
    object["cpr"] = sessionStorage.getItem("cpr");
    console.log(object);

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/" + object.cpr, {
        method: "GET",
        headers: {
            'Authorization': bearer,
        },
    });

    const json = await res.text();
    const obj = JSON.parse(json);

    document.getElementById("navnfelt_sidebar").innerHTML = obj.firstname + " " + obj.lastname;
    document.getElementById("cprfelt_sidebar").innerHTML = obj.cpr;
    document.getElementById("addfelt_sidebar").innerHTML = obj.address;
    document.getElementById("cityfelt_sidebar").innerHTML = obj.city;
    document.getElementById("error_sidebar").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage;
}

//*******************************************************************************************************************//

// function to API fetch a patient //
//*******************************************************************************************************************//
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
    const obj = JSON.parse(json);

    if(res.status === 201){
        console.log(obj.firstname)
        console.log(obj.lastname)
        console.log(obj.adresse)
        document.getElementById("errorcode").innerHTML = ""
        document.getElementById("navnfelt").innerHTML = obj.firstname + " " + obj.lastname;
        document.getElementById("cprfelt").innerHTML = "(" + obj.cpr + ")";

      var button = document.createElement("button");
      button.innerHTML = "Vælg patient";
      button.setAttribute('type', 'button');
      button.classList.add('button');
        button.onclick = function(){
            sessionStorage.setItem("firstname", obj.firstname);
            sessionStorage.setItem("lastname", obj.lastname);
            sessionStorage.setItem("cpr", obj.cpr);
            document.getElementById("navnfelt_sidebar").innerHTML = obj.firstname + " " + obj.lastname;
            document.getElementById("cprfelt_sidebar").innerHTML = obj.cpr;
            document.getElementById("addfelt_sidebar").innerHTML = obj.address;
            document.getElementById("cityfelt_sidebar").innerHTML = obj.city;
            document.getElementById("successcode").innerHTML = "Du har nu valgt " + sessionStorage.getItem("firstname") + " " + sessionStorage.getItem("lastname") + ".";
      };

      var divButton = document.getElementById("chooseButton")
        divButton.innerHTML = "";
      divButton.appendChild(button);

     }else{

        document.getElementById("errorcode").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage;
        document.getElementById("cprfelt").innerHTML = "";
        document.getElementById("navnfelt").innerHTML = "";
     }
    console.log(res)

}

// change visibility of div element //
//*******************************************************************************************************************//
function changeVisibility(showDiv, hideDiv){
    document.getElementById(showDiv).classList.remove("hidden");
    document.getElementById(hideDiv).classList.add("hidden");

}
//*******************************************************************************************************************//

// API fetch all consultations from a chosen CPR //
//*******************************************************************************************************************//
async function fetchAftale(){
    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/"+ sessionStorage.getItem("cpr"), {
        method: "GET",
        headers: {
            'Authorization' : bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(obj);
    const obj2 = obj;
    obj2.forEach(o => delete o.note);
    console.log(obj2);


    if(res.status === 201) {
        changeVisibility('previousAftaler', 'konsultationContainer');
        document.getElementById("errorfelt3").innerHTML = "";
        document.getElementById("aftaleTable").innerHTML = "";

        let myTable = document.querySelector('#aftaleTable');

        let headers = ['Startdato', 'Varighed', 'Gå til'];

        let table = document.createElement('table');
        let headerRow = document.createElement('tr');
        headers.forEach(headerText => {
            let header = document.createElement('th');
            let textNode = document.createTextNode(headerText);
            header.appendChild(textNode);
            headerRow.appendChild(header);
        });

        table.appendChild(headerRow);

        obj2.forEach(objekt => {
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
        table.classList.add("table")
        myTable.appendChild(table);

    }else{
        document.getElementById("errorfelt3").innerHTML = "Fejlkode :" + obj.errorCode + " " +obj.errorMessage;
    }
}

//*******************************************************************************************************************//

async function createAftale(){
    let aftaleForm = document.getElementById("aftaleForm");
    const formData = new FormData(aftaleForm);
    const object = Object.fromEntries(formData);
    console.log(object);

}