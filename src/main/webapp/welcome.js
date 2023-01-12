function loadWelcome(){

    if(sessionStorage.getItem("role") == "?patient?"){
        document.getElementById("welcomeContainerDoctor").classList.add("hidden");

        fetchPatientData();
    }
}