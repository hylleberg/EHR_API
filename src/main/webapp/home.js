document.addEventListener("DOMContentLoaded", () =>{
    const aftaleMainPage = document.querySelector("#aftaleMain");
    const aftaleSelectPage = document.querySelector("#aftaleSelect");

    document.querySelector("#linkSelectPage").addEventListener("click", e =>{
        e.preventDefault();
        aftaleMainPage.classList.add("hidden");
        aftaleSelectPage.classList.remove("hidden");
    });

    document.querySelector("#linkGoBack").addEventListener("click", e=>{
        e.preventDefault();
        aftaleMainPage.classList.remove("hidden");
        aftaleSelectPage.classList.add("hidden");
    });
})