// Persist data on accidental refreshs //
//*******************************************************************************************************************//
console.log("script running")
if (sessionStorage.getItem("cpr") && sessionStorage.getItem("firstname") && sessionStorage.getItem("lastname") && (sessionStorage.getItem("role") === "?doctor?")) {
    // Restore the contents of the text field
    console.log("Restoring")
    document.getElementById("navnfelt_sidebar").innerHTML = sessionStorage.getItem("firstname") + " " + sessionStorage.getItem("lastname");
    document.getElementById("cprfelt_sidebar").innerHTML = sessionStorage.getItem("cpr");
    document.getElementById("addfelt_sidebar").innerHTML = sessionStorage.getItem("address")
    document.getElementById("cityfelt_sidebar").innerHTML = sessionStorage.getItem("city")

    var button2 = document.createElement("button");
    button2.innerHTML = "Vælg ny patient.";
    button2.setAttribute('type', 'button');
    button2.classList.add('button');
    button2.onclick = function () {

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
}

function clearSession() {
    sessionStorage.clear();

}

//*******************************************************************************************************************//
// Removing padding for view //
//*******************************************************************************************************************//

let userText = sessionStorage.getItem("user").substring(1, (sessionStorage.getItem("user").length) - 1)
sessionStorage.setItem("workerusername", sessionStorage.getItem("user").substring(1, (sessionStorage.getItem("user").length) - 1));
let roleText = sessionStorage.getItem("role").substring(1, (sessionStorage.getItem("role").length) - 1)
document.getElementById("userID").innerHTML = userText;
document.getElementById("roleID").innerHTML = roleText;
//*******************************************************************************************************************//
//Check if user is patient //
//*******************************************************************************************************************//

if (sessionStorage.getItem("role") == "?patient?") {
    document.getElementById("findPatientDiv").classList.add("hidden");
    fetchPatientData();
}
//Check if user is doctor //
//*******************************************************************************************************************//
if (sessionStorage.getItem("role") == "?doctor?") {

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

    if (res.status != 201) {

        document.getElementById("error_sidebar").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";
    } else {
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
            'Authorization': bearer,
        },
    });

    const json = await res.text();
    const obj = JSON.parse(json);
    if (res.status != 201) {
        document.getElementById("error_sidebar").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage;

    } else {
        console.log(obj.firstname)
        console.log(obj.lastname)
        console.log(obj.adresse)
        document.getElementById("error_sidebar").innerHTML = ""
        document.getElementById("navnfelt").innerHTML = obj.firstname + " " + obj.lastname;
        document.getElementById("cprfelt").innerHTML = "(" + obj.cpr + ")";

        var button = document.createElement("button");
        button.innerHTML = "Vælg patient";
        button.setAttribute('type', 'button');
        button.classList.add('button');
        button.onclick = function () {
            sessionStorage.setItem("firstname", obj.firstname);
            sessionStorage.setItem("lastname", obj.lastname);
            sessionStorage.setItem("address", obj.address);
            sessionStorage.setItem("city", obj.city);
            sessionStorage.setItem("cpr", obj.cpr);
            document.getElementById("navnfelt_sidebar").innerHTML = obj.firstname + " " + obj.lastname;
            document.getElementById("cprfelt_sidebar").innerHTML = obj.cpr;
            document.getElementById("addfelt_sidebar").innerHTML = obj.address;
            document.getElementById("cityfelt_sidebar").innerHTML = obj.city;

            var button2 = document.createElement("button");
            button2.innerHTML = "Vælg ny patient.";
            button2.setAttribute('type', 'button');
            button2.classList.add('button');
            button2.onclick = function () {

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
function changeVisibility(showDiv, hideDiv) {
    document.getElementById("succescode").innerHTML = "";
    document.getElementById("errorfelt3").innerHTML = "";
    document.getElementById("error_sidebar").innerHTML = "";
    document.getElementById(showDiv).classList.remove("hidden");
    document.getElementById(hideDiv).classList.add("hidden");
}

//*******************************************************************************************************************//
// API fetch all EKG recording list from a chosen CPR //
//*******************************************************************************************************************//
async function fetchEKGList() {

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/ekg/fetch/list/" + sessionStorage.getItem("cpr"), {
        method: "GET",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(res.status);
    if (res.status != 201) {

        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";

    } else {


        console.log(obj);

        obj.forEach(o => delete o.ekgDataList)
        // Print table for future consultations
        let myTable = document.querySelector('#ekgListTable');
        let headers = ['Patient', 'Dato og tid', 'EKG Målings ID'];

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

        obj.forEach(objekt => {
            let row = document.createElement('tr');
            Object.values(objekt).forEach(text => {
                let cell = document.createElement('td');
                let textNode = document.createTextNode(text);
                cell.appendChild(textNode);
                row.appendChild(cell);


            })
            let btn = document.createElement('button');
            btn.classList.add("button")
            btn.innerHTML = "Vælg måling";
            btn.onclick = async function () {
                console.log("EKG ID:" + objekt.ekgID);
                const arrayObj = await fetchEKGRecording(objekt.ekgID)
                console.log(arrayObj);
                makeGraph(arrayObj);
                changeVisibility("ekgMonitorContainer", "ekgListContainer")
            };


            row.appendChild(btn);

            table.appendChild(row);

        });

        myTable.appendChild(table);


    }
}

//*******************************************************************************************************************//
// API fetch all EKG recording list from a chosen CPR //
//*******************************************************************************************************************//
async function fetchEKGRecording(ekgID) {
    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/ekg/fetch/recording/" + sessionStorage.getItem("cpr") + "/" + ekgID, {
        method: "GET",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(res.status);
    if (res.status != 201) {

        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";

    } else {

        // console.log("Array of EKG voltages.." + obj)
        return obj;

    }
}

//*******************************************************************************************************************//
// API fetch all consultations from a chosen CPR //
//*******************************************************************************************************************//
async function fetchAftalerPatient(aftaleSite) {


    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/patient/" + sessionStorage.getItem("cpr"), {
        method: "GET",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(res.status);
    if (res.status != 201) {

        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";

    } else {
        changeVisibility('previousAftalerPatient', 'konsultationContainer');
        document.getElementById("previousAftalerPatientTable").innerHTML = "";

        console.log(obj);
        const obj2 = obj;
        obj2.forEach(o => delete o.note);
        obj2.forEach(o => delete o.cpr);
        obj2.forEach(o => o.duration = secondsToHms(o.duration));

        var futureListObject = new Object;
        var pastListObject = new Object;

        var currentDate = new Date().toISOString();
        var currentTime = currentDate.toLocaleString('da-DK', {timeZone: 'Europe/Copenhagen'});

        futureListObject = obj2.filter(obj2 => new Date(obj2.datetime) > new Date(currentTime));
        pastListObject = obj2.filter(obj2 => new Date(obj2.datetime) < new Date(currentTime));

        console.log(futureListObject);
        console.log(pastListObject);

        // Print table for future consultations
        let myTable = document.querySelector('#comingAftalerPatientTable');
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

        futureListObject.forEach(objekt => {
            let row = document.createElement('tr');
            Object.values(objekt).forEach(text => {
                let cell = document.createElement('td');
                let textNode = document.createTextNode(text);
                cell.appendChild(textNode);
                row.appendChild(cell);


            })
            let btn = document.createElement('button');
            btn.classList.add("button")
            btn.innerHTML = "Vælg aftale";
            btn.onclick = function () {
                // sessionStorage.setItem("aftaleID", 1); // Unique aftaleID here!
                console.log("Aftale ID:" + objekt.aftaleid);
                fetchSingleAftale(objekt.aftaleid, 'showPatientAftaleDiv');
                // changeVisibility("visAftale", "aftaleListContainer");

            };
            let buttondiv = "btn" + objekt.aftaleid;
            let btn3 = document.createElement('button');
            btn3.classList.add("button_select")
            btn3.innerHTML = "Aflys aftale";
            btn3.setAttribute("id", buttondiv);
            btn3.onclick = function () {
                document.getElementById(buttondiv).classList.add("button_warning")
                document.getElementById(buttondiv).innerHTML = "Tryk igen for at bekræfte";
                document.getElementById(buttondiv).onclick = function () {
                    deleteAftale(objekt.aftaleid);
                    loadHTML('sitecontent', 'aftaler_patient.html');
                    fetchAftalerPatient('previousAftalerPatient');
                    console.log("Bekræftet!");
                }
                console.log("Sletter aftale.....");
                // document.location.reload(true)


            };
            row.appendChild(btn);
            row.appendChild(btn3);
            table.appendChild(row);

        });

        myTable.appendChild(table);

// Print table for previous consultations
        let myTable2 = document.querySelector('#previousAftalerPatientTable');
        let headers2 = ['Behandler', 'Startdato', 'Varighed', 'Gå til'];

        let table2 = document.createElement('table');
        table2.classList.add("table1");
        let headerRow2 = document.createElement('tr');
        headers2.forEach(headerText2 => {
            let header2 = document.createElement('th');
            let textNode2 = document.createTextNode(headerText2);
            header2.appendChild(textNode2);
            headerRow2.appendChild(header2);
        });

        table2.appendChild(headerRow2);

        pastListObject.forEach(objekt => {
            let row2 = document.createElement('tr');
            Object.values(objekt).forEach(text2 => {
                let cell2 = document.createElement('td');
                let textNode2 = document.createTextNode(text2);
                cell2.appendChild(textNode2);
                row2.appendChild(cell2);


            })
            let btn2 = document.createElement('button');
            btn2.classList.add("button")
            btn2.innerHTML = "Vælg aftale";
            btn2.onclick = function () {
                // sessionStorage.setItem("aftaleID", 1); // Unique aftaleID here!
                console.log("Aftale ID:" + objekt.aftaleid);
                fetchSingleAftale(objekt.aftaleid, 'showPatientAftaleDiv');
                // changeVisibility("visAftale", "aftaleListContainer");

            };
            row2.appendChild(btn2);
            table2.appendChild(row2);

        });

        myTable2.appendChild(table2);
    }
}

//*******************************************************************************************************************//
// API fetch all consultations from a chosen CPR //
//*******************************************************************************************************************//
async function deleteAftale(aftaleid) {


    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/delete/" + aftaleid, {
        method: "DELETE",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    console.log(res.status);
    if (res.status != 201) {
        console.log("Issue deleting..")
        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";

    } else {
        console.log("Aftale deleted!")

    }

}

//*******************************************************************************************************************//
// API fetch single consultation from a chosen aftaleID//
//*******************************************************************************************************************//
async function fetchSingleAftale(aftaleID, div) {

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/" + aftaleID, {
        method: "GET",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);

    console.log(res.status);
    if (res.status != 201) {
        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";
    } else {

        console.log(obj);
        console.log("Hurra!");
        obj.duration = secondsToHms(obj.duration);
        if (div == "showPatientAftaleDiv") {
            changeVisibility(div, "previousAftalerPatient");
            document.getElementById("datefelt").innerHTML = obj.datetime;
            document.getElementById("durationfelt").innerHTML = obj.duration;
            document.getElementById("workerusernamefelt").innerHTML = obj.workerusername;
            document.getElementById("commentfelt").innerHTML = obj.note;
        } else {
            changeVisibility(div, "previousAftalerWorker");
            document.getElementById("datefelt2").innerHTML = obj.datetime;
            document.getElementById("durationfelt2").innerHTML = obj.duration;
            document.getElementById("workerusernamefelt2").innerHTML = obj.workerusername;
            document.getElementById("patientfelt2").innerHTML = obj.cpr;
            document.getElementById("commentfelt2").innerHTML = obj.note;

        }


    }

}

//*******************************************************************************************************************//
// API fetch all consultations from a chosen workerusername//
//*******************************************************************************************************************//
async function fetchAftalerWorker() {


    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/worker/" + sessionStorage.getItem("workerusername"), {
        method: "GET",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    if (res.status != 201) {

        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";

    } else {
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

                console.log("Aftale ID:" + objekt.aftaleid);
                fetchSingleAftale(objekt.aftaleid, 'showWorkerAftaleDiv');


            };
            row.appendChild(btn);
            table.appendChild(row);

        });

        myTable.appendChild(table);


    }
}

//*******************************************************************************************************************//
// API post consultation request to DB//
//*******************************************************************************************************************//
async function requestAftale() {


    let aftaleForm = document.getElementById("aftaleForm");
    const formData = new FormData(aftaleForm);
    const object = Object.fromEntries(formData);
    object.datetime = object.datetime + "T00:00:00Z"

    let cprobject = {};
    cprobject["cpr"] = sessionStorage.getItem("cpr");
    object2 = Object.assign(cprobject, object);
    console.log(object2);

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/request/save/request", {
        method: "POST",
        body: JSON.stringify(object2),
        headers: {
            "content-type": "application/json",
            'Authorization': bearer
        }
    })

    if (res.status === 201) {
        document.getElementById("succescode").innerHTML = "Du har nu anmodet om en aftale.";

    } else {
        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";
    }
}

//*******************************************************************************************************************//
// API post consultation from req to DB//
//*******************************************************************************************************************//
async function createAftaleFromReq() {


    let aftaleReqForm = document.getElementById("aftaleReqForm");
    const formData = new FormData(aftaleReqForm);
    const object = Object.fromEntries(formData);
    console.log(object);
    object.datetime = object.datetime.substring(0, object.datetime.indexOf("T")) + "T" + object.time + ":00Z";
    object.duration = (Number(object.duration) * 60).toString();
    delete object['time'];

    console.log("createaftaleform req obj " + object.datetime);

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/request/save/aftale", {
        method: "POST",
        body: JSON.stringify(object),
        headers: {
            "content-type": "application/json",
            'Authorization': bearer
        }
    })

    if (res.status === 201) {
        document.getElementById("succescode").innerHTML = "Du har nu oprettet en aftale fra anmodning.";

    } else {
        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";
    }
}

//*******************************************************************************************************************//
// API post consultation to DB//
//*******************************************************************************************************************//
async function createAftale() {


    let createAftaleForm = document.getElementById("createAftaleForm");
    const formData = new FormData(createAftaleForm);
    const object = Object.fromEntries(formData);
    console.log(object);
    object.datetime = object.datetime + "T" + object.time + ":00Z";
    object.duration = (Number(object.duration) * 60).toString();
    delete object['time'];

    console.log("createaftaleform req obj " + object.datetime);

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/aftale/create", {
        method: "POST",
        body: JSON.stringify(object),
        headers: {
            "content-type": "application/json",
            'Authorization': bearer
        }
    })

    if (res.status === 201) {
        document.getElementById("succescode").innerHTML = "Du har nu oprettet en aftale fra anmodning.";

    } else {
        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";
    }
}

//*******************************************************************************************************************//
// API fetch all request flags//
//*******************************************************************************************************************//
async function fetchFlags() {


    let object = {};
    object["workerusername"] = sessionStorage.getItem("workerusername");

    console.log(object);

    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/request/flags/" + sessionStorage.getItem("workerusername"), {
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
    flagArray2.forEach(function (x) {
        counts[x] = (counts[x] || 0) + 1;
    });
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
        loadHTML('sitecontent', 'request.html')
        fetchRequests()

    };
    document.getElementById("flagUserDiv").append(btn2);


}

//*******************************************************************************************************************//
// API fetch all requests for a worker //
//*******************************************************************************************************************//
async function fetchRequests() {


    console.log("Fetching request list..")
    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/request/list/" + sessionStorage.getItem("workerusername"), {
        method: "GET",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(res.status);
    if (res.status != 201) {
        document.getElementById("errorfelt3").innerHTML = "Fejlkode :" + obj.errorCode + " " + obj.errorMessage;
    } else {
        document.getElementById("requestTable").innerHTML = "";

        console.log(obj);
        const obj2 = obj;
        obj2.forEach(o => delete o.workerusername);
        obj2.forEach(o => o.note = o.note.substring(0, 25) + "...");

        console.log(obj2);

        let myTable = document.querySelector('#requestTable');

        let headers = ['Patient', 'Ønsket dato', 'Ønsket tidspunkt', 'Årsag til konsultation', 'Gå til'];

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
            btn.innerHTML = "Vælg anmodning";
            btn.onclick = function () {
                // sessionStorage.setItem("aftaleID", 1); // Unique aftaleID here!
                console.log("Aftale ID:" + objekt.aftaleid);
                fetchSingleRequest(objekt.aftaleid);


            };
            row.appendChild(btn);
            table.appendChild(row);

        });

        myTable.appendChild(table);


    }
}

//*******************************************************************************************************************//
// API fetch single requests for a worker //
//*******************************************************************************************************************//
async function fetchSingleRequest(aftaleID) {


    let bearer = "Bearer " + localStorage.getItem("token")
    const res = await fetch("api/request/" + aftaleID, {
        method: "GET",
        headers: {
            'Authorization': bearer,
        }
    })
    console.log(res)
    const json = await res.text();
    const obj = JSON.parse(json);
    console.log(res.status);
    if (res.status != 201) {
        document.getElementById("errorfelt3").innerHTML = "Fejlkode: " + obj.errorCode + ". " + obj.errorMessage + ".";
    } else {

        console.log(obj);
        console.log("Hurra!");
        changeVisibility("requestSingleContainer", "requestListContainer");
        document.getElementById("datefeltreq").value = obj.datetime;
        document.getElementById("timeofdayfeltreq").value = obj.timeOfDay;
        document.getElementById("workerusernamefeltreq").value = obj.workerusername;
        document.getElementById("commentfeltreq").innerHTML = obj.note;
        document.getElementById("patientfeltreq").value = obj.cpr;
        document.getElementById("aftaleidfelt").value = obj.aftaleid;


    }
}

function makeGraph(obj) {

    //setup
    var labels = [...Array(5000).keys()]
    var labels2 = labels.map(function (x) { return (x * (1/500)).toFixed(2)})
    const data = {
        labels: labels2,
        datasets: [{
            label: 'EKG Monitor',
            backgroundColor: [
                'rgb(0,0,0)',
            ],
            borderColor: [
                'rgb(0,0,0)',
            ],
            data: obj,
            borderWidth: 1,

        }]
    };
    //config
    const conf = {
        type: 'line',
        data,
        options: {
            elements: {
                point: {
                    radius: 0
                }
            },
            scales: {
                y: {
                    title:{
                        display: true,
                        text: "mV",
                    }

                },
                x: {
                    title:{
                        display: true,
                        text: "Sekunder"
                    }

                }
            }
        }
    };
    //render
    var myChart = new Chart(document.getElementById('ekg_canvas'), conf);


}

function loadWelcome() {
    if (sessionStorage.getItem("role") == "?patient?") {
        loadHTML("sitecontent", "welcome_patient.html");
    } else {
        loadHTML("sitecontent", "welcome_worker.html");
        fetchFlags();

    }
}

function loadAftaler() {
    if (sessionStorage.getItem("role") == "?patient?") {
        loadHTML("sitecontent", "aftaler_patient.html");
    } else {
        loadHTML("sitecontent", "aftaler_worker.html");
        fetchFlags();

    }

}

function loadLab() {
    if (sessionStorage.getItem("role") == "?patient?") {
        loadHTML("sitecontent", "lab.html");
        fetchEKGList();
    } else {
        loadHTML("sitecontent", "lab.html");
        fetchEKGList();

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
    return hDisplay + mDisplay + sDisplay;
}