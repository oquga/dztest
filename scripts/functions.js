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

function convertDateToInt64(dateString, isStart) {
    const parts = dateString.split('-');
    const day = parseInt(parts[0], 10);
    const month = parseInt(parts[1], 10) - 1; // Month is 0-indexed in JavaScript
    const year = parseInt(parts[2], 10) + 2000; // Adjust for two-digit year
  

    const date = new Date(year, month, day, 23, 59, 59, 999)
    
    if(isStart == "true"){
      date = new Date(year, month, day, 0, 0, 0, 0)
    } 
  
    // Get the timestamp in milliseconds (int64)
    const timestamp = date.getTime();
    console.log(timestamp);
    console.log(date);
    return timestamp; // 
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
    })
    .catch((error) => console.error("Ошибка:", error));
}

function getAllTasks(limit,offset) {
const url = `http://localhost:3000/todos?limit=${limit}&offset=${offset}`;
    fetch(url)
    .then(response => response.json())
    .then((data) => {
        generateTodoItems(data);
    })
    .catch((error) => console.error("Ошибка:", error));
}

function getTasksByDates(from,to,limit,offset) {

    let isDone = !isNotCompleteTasksOnly;
const url = `http://localhost:3000/todos/date?from=${from}&to=${to}&status=${isDone}&limit=${limit}&offset=${offset}`;

    fetch(url)
    .then(response => response.json())
    .then((data) => {
        console.log(data)
        generateTodoItems(data);
    })
    .catch((error) => console.error("Ошибка:", error));
}