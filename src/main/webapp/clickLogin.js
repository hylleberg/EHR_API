async function clickLogin() {

    let loginform = document.getElementById("login");
    const formData = new FormData(loginform);
    const object = Object.fromEntries(formData);
    console.log(object);

    const res = await fetch("api/auth", {
        method: "POST",
        body: JSON.stringify(object),
        headers: {
            "content-type": "application/json"
        }
    })

    const token = await res.text();

    obj = parseJwt(token);

    console.log(obj);
    if (res.status === 201) {
        localStorage.setItem("token", token);
        sessionStorage.setItem("user", obj.username);
        sessionStorage.setItem("role", obj.Role);

        if(obj.Role == "?patient?"){
            sessionStorage.setItem("cpr", obj.username.substring(1, ((obj.username.length)-1)));
        }

        window.location.href = "home.html";

    } else  {
        document.getElementById("error").innerHTML = obj.errorMessage;
    }
    console.log(res.status)
}
//  https://stackoverflow.com/questions/38552003/how-to-decode-jwt-token-in-javascript-without-using-a-library
function parseJwt (token) {
    var base64Url = token.split('.')[1];
    var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    var jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
}