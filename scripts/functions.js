function showOverlay(i) {
    const overlayTaskName = document.getElementById('overlayTaskName');
    const overlayTaskDate = document.getElementById('overlayTaskDate');
    const overlayCheckbox = document.getElementsByClassName('overlay-checkbox')[0];
    const overlayDescription= document.getElementById('overlayFullDescription');
    
    overlayTaskName.innerHTML = currentTaskList[i].name;
    overlayTaskDate.innerText = formatDate(currentTaskList[i].date);
    overlayDescription.innerText = currentTaskList[i].fullDesc;

    if(currentTaskList[i].status){
        overlayCheckbox.innerHTML = `<input type="checkbox" id="overlayCheckbox" checked>`
    } else{
        overlayCheckbox.innerHTML = `<input type="checkbox" id="overlayCheckbox">`
    }
    document.getElementById('overlay').style.display = 'flex';

}

function hideOverlay() {
    document.getElementById('overlay').style.display = 'none';
}

  
  //ISO string to (DD.MM.YYYY HH:mm)
function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', hour12: false };
    return date.toLocaleString('en-GB', options).replace(',', '');
}

function getTasksByName(searchTerm,limit,offset){
const url = `http://localhost:3000/todos/find?q=${searchTerm}&limit=${limit}&offset=${offset}`;
    fetch(url)
    .then(response => response.json())
    .then((data) => {
        console.log(data);
        generateTodoItems(data);
        return data;
    })
    .catch((error) => console.error("Ошибка:", error));
}

function getAllTasks(offset) {
const url = `http://localhost:3000/todos?limit=${limit}&offset=${offset}`;
console.log(url);
    fetch(url)
    .then(response => response.json())
    .then((data) => {
         generateTodoItems(data);
    })
    .catch((error) => console.error("Ошибка:", error));
}


function getTasksByDates(from,to,status,limit,offset) {
    let url = `http://localhost:3000/todos/date?from=${from}&to=${to}&limit=${limit}&offset=${offset}`;
    if(status){
        url = `http://localhost:3000/todos/date?from=${from}&to=${to}&status=${false}&limit=${limit}&offset=${offset}`;
    } 

    fetch(url)
    .then(response => response.json())
    .then((data) => {
        console.log(data)
        generateTodoItems(data);
    })
    .catch((error) => console.error("Ошибка:", error));
}