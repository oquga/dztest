let isNotCompleteTasksOnly = false;
let sortByDate = false;

let searchName;
let startDate;
let endDate;

let pageNum;
let limit = 4;

let currentTaskList;
let currentPageTaskList;

//change actual criteria 
function displayCurrentCriteria(criteria) {
  console.log("Now criteria is: "+criteria);
  $('#currentCritera').text(criteria);
};




//initialize startup variables when document is loaded
document.addEventListener('DOMContentLoaded', function() {
  todayTasks();
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
  let isoDate = new Date().toJSON().slice(0, 10);

  let startOfDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 0, 0, 0, 0);
  let startOfDayInt64 = startOfDay.getTime(); // Convert to int64

  let endOfDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 23, 59, 59, 999);
  let endOfDayInt64 = endOfDay.getTime(); // Convert to int64
  
  console.log(startOfDay);
  displayCurrentCriteria(isoDate)
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
    } else {
      isNotCompleteTasksOnly = false;
    }
    console.log("isNotCompleteTaskOnly: "+ isNotCompleteTasksOnly);
  }

  sidebarCheckbox.addEventListener('change', handleSidebarCheckbox);
});

function generateTodoItems(tasks) {
  console.log(tasks);
  currentTaskList =tasks;
  const todoListcontainer = document.getElementsByClassName('todo-list')[0];
  
  todoListcontainer.innerHTML = '';
  
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
      todoListcontainer.innerHTML += todoItem;
  } 
  }


// document.addEventListener('DOMContentLoaded', function() {
//   window.showOverlay = function (i) {
//     $("#task-title").text(currentTaskList[i].name);
//     $("#task-time").text(currentTaskList[i].date);
//     $("#task-description").text(currentTaskList[i].fullDesc);
//     $("#overlay").css("display", "flex");
//   }

//   window.hideOverlay = function () {
//     $("#overlay").css("display", "none");
//   };


//   // Обработчик для фильтрации задач
//   $("#incomplete-only").on("change", function () {
//     if ($(this).is(":checked")) {
//       $('.todo-item input[type="checkbox"]:checked').closest(".todo-item").hide();
//     } else {
//       $(".todo-item").show();
//     }
//   });
// });








