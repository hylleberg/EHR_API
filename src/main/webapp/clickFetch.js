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
    sessionStorage.setItem("workerusername", sessionStorage.getItem("user").substring(1, (sessionStorage.getItem("user").length)-1));
    let roleText = sessionStorage.getItem("role").substring(1, (sessionStorage.getItem("role").length)-1)
    document.getElementById("userID").innerHTML =  userText;
    document.getElementById("roleID").innerHTML =  roleText;
//*******************************************************************************************************************//
//Check if user is patient //
//*******************************************************************************************************************//

if(sessionStorage.getItem("role") == "?patient?"){
    document.getElementById("findPatientDiv").classList.add("hidden");
    fetchPatientData();
}
//Check if user is doctor //
//*******************************************************************************************************************//
if(sessionStorage.getItem("role") == "?doctor?"){

    fetchFlags();
}
// function to API fetch a logged in patient data //
//*******************************************************************************************************************//
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

    if(res.status != 201){

        document.getElementById("error_sidebar").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage;
    }else{
        document.getElementById("navnfelt_sidebar").innerHTML = obj.firstname + " " + obj.lastname;
        document.getElementById("cprfelt_sidebar").innerHTML = obj.cpr;
        document.getElementById("addfelt_sidebar").innerHTML = obj.address;
        document.getElementById("cityfelt_sidebar").innerHTML = obj.city;
        document.getElementById("error_sidebar").innerHTML = "";

    }


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
    });

    const json = await res.text();
    const obj = JSON.parse(json);
    if(res.status != 201) {
        document.getElementById("error_sidebar").innerHTML = "Fejlkode: " + obj.errorCode + ". " +obj.errorMessage;

    }else{
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

            var button2 = document.createElement("button");
            button2.innerHTML = "Vælg ny patient.";
            button2.setAttribute('type', 'button');
            button2.classList.add('button');
            button2.onclick = function() {

                changeVisibility("findPatientDiv", "showPatientDiv");
                sessionStorage.setItem("firstname", "");
                sessionStorage.setItem("lastname", "");
                sessionStorage.setItem("cpr", "");
                document.getElementById("navnfelt_sidebar").innerHTML = "";
                document.getElementById("cprfelt_sidebar").innerHTML = "";
                document.getElementById("addfelt_sidebar").innerHTML = "";
                document.getElementById("navnfelt").innerHTML = "";
                document.getElementById("cprfelt").innerHTML = "";
                document.getElementById("chooseButton").innerHTML = "";



            };
            var divButton2 = document.getElementById("chooseNewPatientButton")
            divButton2.innerHTML = "";
            divButton2.appendChild(button2);

            changeVisibility("showPatientDiv", "findPatientDiv");

      };



      var divButton = document.getElementById("chooseButton")
        divButton.innerHTML = "";
      divButton.appendChild(button);

     }
    console.log(res)

}
//*******************************************************************************************************************//
// change visibility of div element //
//*******************************************************************************************************************//
function changeVisibility(showDiv, hideDiv){
    document.getElementById(showDiv).classList.remove("hidden");
    document.getElementById(hideDiv).classList.add("hidden");

}
//*******************************************************************************************************************//

// API fetch all consultations from a chosen CPR //
//*******************************************************************************************************************//
async function fetchAftalerPatient(){
    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/patient/"+ sessionStorage.getItem("cpr"), {
        method: "GET",
        headers: {
            'Authorization' : bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(res.status);
    if (res.status != 201){

        document.getElementById("errorfelt3").innerHTML = "Fejlkode :" + obj.errorCode + " " +obj.errorMessage;
        document.getElementById("errorfelt4").innerHTML = "Fejlkode :" + obj.errorCode + " " +obj.errorMessage;
    }

    else{
        changeVisibility('previousAftalerPatient', 'konsultationContainer');
        document.getElementById("aftalePatientTable").innerHTML = "";

        console.log(obj);
        const obj2 = obj;
        obj2.forEach(o => delete o.note);
        obj2.forEach(o => delete o.cpr);
       // obj2.forEach(o => delete o.id);
        obj2.forEach(o => o.duration = secondsToHms(o.duration));

        console.log(obj2);

        let myTable = document.querySelector('#aftalePatientTable');

        let headers = ['Behandler', 'Startdato', 'Varighed', 'Gå til'];

        let table = document.createElement('table');
        table.classList.add("table1");
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
            btn.classList.add("button_select")
            btn.innerHTML = "Vælg aftale";
            btn.onclick = function () {
               // sessionStorage.setItem("aftaleID", 1); // Unique aftaleID here!
                console.log("Aftale ID:" + objekt.id);
                fetchSingleAftale(objekt.id);
               // changeVisibility("visAftale", "aftaleListContainer");

            };
            row.appendChild(btn);
            table.appendChild(row);

        });

        myTable.appendChild(table);


    }
}

//*******************************************************************************************************************//

async function fetchSingleAftale(aftaleID){
    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/" + aftaleID, {
        method: "GET",
        headers: {
            'Authorization' : bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(res.status);
    if (res.status != 201){

    }else{

        console.log(obj);
        console.log("Hurra!");
        changeVisibility("showAftaleDiv", "previousAftalerPatient");
        document.getElementById("datefelt").innerHTML = obj.datetime;
        document.getElementById("durationfelt").innerHTML = obj.duration;
        document.getElementById("workerusernamefelt").innerHTML = obj.workerusername;
        document.getElementById("commentfelt").innerHTML = obj.note;

    }

}
//*******************************************************************************************************************//
// API fetch all consultations from a chosen workerusername//
//*******************************************************************************************************************//
async function fetchAftalerWorker(){
    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/worker/"+ sessionStorage.getItem("workerusername"), {
        method: "GET",
        headers: {
            'Authorization' : bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    if (res.status != 201){

        document.getElementById("errorfelt3").innerHTML = "Fejlkode :" + obj.errorCode + " " +obj.errorMessage;
        document.getElementById("errorfelt4").innerHTML = "Fejlkode :" + obj.errorCode + " " +obj.errorMessage;
    }

    else{
        changeVisibility('previousAftalerWorker', 'konsultationContainer');
        document.getElementById("errorfelt3").innerHTML = "";
        console.log(obj);
        const obj2 = obj;
        obj2.forEach(o => delete o.note);
        obj2.forEach(o => delete o.workerusername);
        obj2.forEach(o => o.duration = secondsToHms(o.duration));

        console.log(obj2);

        let myTable = document.querySelector('#aftaleWorkerTable');

        let headers = ['CPR nr', 'Startdato', 'Varighed', 'Gå til'];

        let table = document.createElement('table');
        table.classList.add("table1");
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
            btn.classList.add("button_select")
            btn.innerHTML = "Vælg aftale";
            btn.onclick = function () {
                sessionStorage.setItem("aftaleID", 1); // Unique aftaleID here!
                changeVisibility("visAftale", "aftaleListContainer");

            };
            row.appendChild(btn);
            table.appendChild(row);

        });

        myTable.appendChild(table);


    }
}
//*******************************************************************************************************************//
// API post consultation request//
//*******************************************************************************************************************//
async function requestAftale(){
    let aftaleForm = document.getElementById("aftaleForm");
    const formData = new FormData(aftaleForm);
    const object = Object.fromEntries(formData);
    object.datetime = object.datetime + "T00:00:00Z"

    let cprobject = {};
    cprobject["cpr"] = sessionStorage.getItem("cpr");
    object2 = Object.assign(cprobject, object);
    console.log(object2);

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/requestcons", {
        method: "POST",
        body: JSON.stringify(object2),
        headers: {
            "content-type": "application/json",
            'Authorization' : bearer
        }
    })

    if(res.status === 201) {
        document.getElementById("successcode2").innerHTML = "Du har nu anmodet om en aftale.";
    }
}

//*******************************************************************************************************************//

async function fetchFlags(){
    let object = {};
    object["workerusername"] = sessionStorage.getItem("workerusername");

    console.log(object);

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/flags/" + object.workerusername, {
        method: "GET",
        headers: {
            'Authorization': bearer,
        },
    });

    let flagArray = await res.text();
    let flagArray2 = JSON.parse(flagArray);

    console.log(flagArray);
    console.log(flagArray2);


    // https://masteringjs.io/tutorials/fundamentals/foreach-object

    const counts = {};
    flagArray2.forEach(function (x) { counts[x] = (counts[x] || 0) + 1; });
    console.log("counts: " + counts);
    let reqString = "";
    Object.entries(counts).forEach(entry => {


        const [key, value] = entry;
        console.log(key, value);
        reqString = reqString + "Patient: " + key + "," + value + " anmodninger" + "<br><br>";
    });


    document.getElementById("flagUserDiv").innerHTML = reqString;

    let btn2 = document.createElement('button');
    btn2.classList.add("button")
    btn2.innerHTML = "Gå til anmodninger";
    btn2.onclick = function () {
        sessionStorage.setItem("aftaleID", 1); // Unique aftaleID here!

    };
    document.getElementById("flagUserDiv").append(btn2);


}

//*******************************************************************************************************************//







function loadWelcome(){
    if(sessionStorage.getItem("role") == "?patient?") {
        loadHTML("sitecontent", "welcome_patient.html");
    }else{
        loadHTML("sitecontent", "welcome_worker.html");
        fetchFlags();

    }

}

function loadAftaler(){
    if(sessionStorage.getItem("role") == "?patient?") {
        loadHTML("sitecontent", "aftaler_patient.html");
    }else{
        loadHTML("sitecontent", "aftaler_worker.html");
        fetchFlags();

    }

}
//*******************************************************************************************************************//
//** https://stackoverflow.com/questions/37096367/how-to-convert-seconds-to-minutes-and-hours-in-javascript **//

function secondsToHms(d) {
    d = Number(d);
    var h = Math.floor(d / 3600);
    var m = Math.floor(d % 3600 / 60);
    var s = Math.floor(d % 3600 % 60);

    var hDisplay = h > 0 ? h + (h == 1 ? " time, " : " timer, ") : "";
    var mDisplay = m > 0 ? m + (m == 1 ? " minut, " : " minutter, ") : "";
    var sDisplay = s > 0 ? s + (s == 1 ? " sekonder" : " sekonder") : "";
    return hDisplay + mDisplay + sDisplay;}