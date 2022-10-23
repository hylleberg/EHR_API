console.log("login");

async function clickLogin() {

    let loginform = document.getElementById("login");
    const formData = new FormData(loginform);
    const object = Object.fromEntries(formData);
    console.log(object);
    //Bruger fetch-API til at sende data - POST. JSON.stringify for at serialisere objekt til string.


        const res = await fetch("api/login", {
            method: "POST",
            body: JSON.stringify(object),
            headers: {
                "content-type": "application/json"
            }


        })
        if(res.status === 201){
            window.location.href="home.html"

        }else{
            document.getElementById("error").innerHTML = "Forkert brugernavn/password kombination, prøv igen."

        }
    console.log(res.status)


}