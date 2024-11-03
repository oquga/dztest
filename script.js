let isNotCompleteTasksOnly = false;
let currentTaskList;

//change actual criteria 
function displayCurrentCriteria(criteria) {
  console.log("Now criteria is: "+criteria);
  $('#currentCritera').text(criteria);
};

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





//initialize startup variables when document is loaded
document.addEventListener('DOMContentLoaded', function() {
  let isoDate = new Date().toJSON().slice(0, 10);
  todayTasks();
  displayCurrentCriteria(isoDate);
  
});

//Search by name button
document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.getElementById("searchInput");
  const searchButton = document.getElementById("searchButton");

  searchButton.addEventListener('click', function() {
    const searchTerm = searchInput.value;
    console.log("len "+ searchTerm.length);
    if (searchTerm.length === 0) {
      console.log("Search all");
      displayCurrentCriteria("*")
      getAllTasks(4,0);
    } else{
      console.log(`Searching for "${searchTerm}"...`);
      displayCurrentCriteria(searchTerm);

      getTasksByName(searchTerm,4,0);
      
    }
    $('#searchInput').val('');
  });

  searchInput.addEventListener('keyup', function(event) {
    if (event.keyCode === 13) {
      // simulate a button click to trigger the search
      searchButton.click();
    }
  });
});

function todayTasks(){
  const currentDate = new Date();
  let startOfDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 0, 0, 0, 0);
  let startOfDayInt64 = startOfDay.getTime(); // Convert to int64

  let endOfDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 23, 59, 59, 999);
  let endOfDayInt64 = endOfDay.getTime(); // Convert to int64
  
  console.log(startOfDay);
  getTasksByDates(startOfDayInt64,endOfDayInt64,4,0);
}

function thisWeekTasks() {
  const currentDate = new Date();
  
  // Calculate the start of the week (Sunday)
  const startOfWeek = new Date(currentDate);
  startOfWeek.setDate(currentDate.getDate() - currentDate.getDay()); // Set to Sunday
  startOfWeek.setHours(0, 0, 0, 0); // Set time to 00:00:00.000
  let startOfWeekInt64 = startOfWeek.getTime(); // Convert to int64

  // Calculate the end of the week (Saturday)
  const endOfWeek = new Date(startOfWeek);
  endOfWeek.setDate(startOfWeek.getDate() + 6); // Set to Saturday
  endOfWeek.setHours(23, 59, 59, 999); // Set time to 23:59:59.999
  let endOfWeekInt64 = endOfWeek.getTime(); // Convert to int64

  console.log(startOfWeek);
  console.log(endOfWeek);
  
  getTasksByDates(startOfWeekInt64, endOfWeekInt64, 4, 0);
}

//sideBar checkbox handler
document.addEventListener('DOMContentLoaded', function() {
  const sidebarCheckbox = document.getElementById('tasksFilterCheckbox');

  function handleSidebarCheckbox() { 
    if (sidebarCheckbox.checked) {
      isNotCompleteTasksOnly = true;
      console.log("isNotCompleteTaskOnly: "+ isNotCompleteTasksOnly);
    } else {
      isNotCompleteTasksOnly = false;
      console.log("isNotCompleteTaskOnly: "+ isNotCompleteTasksOnly);
    }
  }

  sidebarCheckbox.addEventListener('change', handleSidebarCheckbox);
});




//Parse Array into todo task blocks in index.html
function generateTodoItems(tasks) {
  currentTaskList =tasks;
  const container = document.getElementsByClassName('todo-list')[0];

  container.innerHTML = '';

  // Iterate over each task in the array
  for (let i = 0; i < tasks.length; i++) {
    let todoItem = `
          <div class="todo-item" >
              <div class="taskName" onclick="showOverlay(${i})">
                  <strong>${tasks[i].name}</strong>
              </div>
              <div class="info">
                  <div class="taskShortDesc" onclick="showOverlay(${i})">
                      <p>${tasks[i].shortDesc}</p>
                  </div>
                  <div class="taskStatus">
                      <input type="checkbox" ${tasks[i].status ? 'checked' : ''}>
                  </div>
              </div>
              <div class="taskDate" onclick="showOverlay(${i})">
                  <span>${formatDate(tasks[i].date)}</span>
              </div>
          </div>
      `; 
    container.innerHTML += todoItem;
  } 
}

document.addEventListener('DOMContentLoaded', function() {
  // Показать оверлей с информацией о задаче
  window.showOverlay = function (i) {
    $("#task-title").text(currentTaskList[i].name);
    $("#task-time").text(currentTaskList[i].date);
    $("#task-description").text(currentTaskList[i].fullDesc);
    $("#overlay").css("display", "flex");
  }

  // Скрыть оверлей
  window.hideOverlay = function () {
    $("#overlay").css("display", "none");
  };


  // Обработчик для фильтрации задач
  $("#incomplete-only").on("change", function () {
    if ($(this).is(":checked")) {
      $('.todo-item input[type="checkbox"]:checked').closest(".todo-item").hide();
    } else {
      $(".todo-item").show();
    }
  });
});


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






