async function clickLogin() {

    let loginform = document.getElementById("login");
    const formData = new FormData(loginform);
    const object = Object.fromEntries(formData);
    console.log(object);

    const res = await fetch("api/login", {
        method: "POST",
        body: JSON.stringify(object),
        headers: {
            "content-type": "application/json"
        }
    })
    if (res.status === 201) {
        window.location.href = "home.html"

    } else if (rest.status === 500) {
        document.getElementById("error").innerHTML = "Server fejl, prøv igen."
    } else {
        document.getElementById("error").innerHTML = "Forkert brugernavn/password kombination, prøv igen."
    }
    console.log(res.status)
}