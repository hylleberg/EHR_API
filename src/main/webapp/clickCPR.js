console.log("login");

async function clickCPR() {

    let cprform = document.getElementById("cprform");
    const formData = new FormData(cprform);
    const object = Object.fromEntries(formData);
    console.log(object);
    //Bruger fetch-API til at sende data - POST. JSON.stringify for at serialisere objekt til string.


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
        console.log(obj.cpr)
        console.log(obj.fornavn)
        console.log(obj.efternavn)
        console.log(obj.adresse)
        document.getElementById("errorcode").innerHTML = ""
    document.getElementById("cprfelt").innerHTML = obj.cpr;
    document.getElementById("navnfelt").innerHTML = obj.fornavn + " " + obj.efternavn;
    document.getElementById("adressefelt").innerHTML = obj.adresse;

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