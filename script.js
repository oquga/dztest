let isCompleteTasksOnly=false;

//initialize startup variables when document is loaded
document.addEventListener('DOMContentLoaded', function() {
  let isoDate = new Date().toJSON().slice(0, 10);
  let todayDate = isoDate;
  displayCurrentCriteria(todayDate);
});

//change actual criteria function
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

//sideBar checkbox handler
document.addEventListener('DOMContentLoaded', function() {
  // console.log("isCompleteTaskOnly: "+ isCompleteTasksOnly);
  const sidebarCheckbox = document.getElementById('tasksFilterCheckbox');

  function handleSidebarCheckbox() { 
    if (sidebarCheckbox.checked) {
      isCompleteTasksOnly = true;
      console.log("isCompleteTaskOnly: "+ isCompleteTasksOnly);
    } else {
      isCompleteTasksOnly = false;
      console.log("isCompleteTaskOnly: "+ isCompleteTasksOnly);
    }
  }

  sidebarCheckbox.addEventListener('change', handleSidebarCheckbox);
});


//Search by name button
document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.getElementById("searchInput");
  const searchButton = document.getElementById("searchButton");

  // add event listener to the button
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

      const url = `http://localhost:3000/todos/find?q=${searchTerm}&limit=${4}&offset=${0}`;

      fetch(url)
      .then(response => response.json())
        .then((data) => {
          console.log(data);
          generateTodoItems(data);
        })
        .catch((error) => console.error("Ошибка:", error));
      
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

function getAllTasks(limit,offset) {
  // Формируем URL с параметрами
  const url = `http://localhost:3000/todos?limit=${limit}&offset=${offset}`;

  fetch(url)
  .then(response => response.text())
    .then((data) => {
      console.log(JSON.parse(data));
     
      // generateTodoItems(data.tasks);
    })
    .catch((error) => console.error("Ошибка:", error));
}


function generateTodoItems(tasks) {
  // Get the container where the todo items will be appended
  const container = document.getElementsByClassName('todo-list')[0];
  console.log("tasks: "+tasks[0].name);
  // Clear existing content (optional)
  container.innerHTML = '';

  // Iterate over each task in the array
  for (let i = 0; i < tasks.length; i++) {
    let todoItem = `
          <div class="todo-item">
              <div class="taskName" onclick="showOverlay()">
                  <strong>${tasks[i].name}</strong>
              </div>
              <div class="info">
                  <div class="taskShortDesc" onclick="showOverlay()">
                      <p>${tasks[i].shortDesc}</p>
                  </div>
                  <div class="taskStatus">
                      <input type="checkbox" ${tasks[i].status ? 'checked' : ''}>
                  </div>
              </div>
              <div class="taskDate" onclick="showOverlay()">
                  <span>${formatDate(tasks[i].date)}</span>
              </div>
          </div>
      `; 
    container.innerHTML += todoItem;
  }
  
}


document.addEventListener('DOMContentLoaded', function() {
  // Показать оверлей с информацией о задаче
  window.showOverlay = function (title, time, description) {
    $("#task-title").text(title);
    $("#task-time").text(time);
    $("#task-description").text(description);
    $("#overlay").css("display", "flex");
  };

  // Скрыть оверлей
  window.hideOverlay = function () {
    $("#overlay").css("display", "none");
  };

  // // Загрузка задач при загрузке страницы
  // loadTasks();

  // Обработчик для фильтрации задач
  $("#incomplete-only").on("change", function () {
    if ($(this).is(":checked")) {
      $('.todo-item input[type="checkbox"]:checked').closest(".todo-item").hide();
    } else {
      $(".todo-item").show();
    }
  });
});



