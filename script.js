let isCompleteTasksOnly=false;

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

function fetchTasks() {
  // Получаем значения из формы
  // const limit = document.getElementById("limit").value;
  // const offset = document.getElementById("offset").value;

  // Формируем URL с параметрами
  const url = `http://localhost:3000/todos?limit=${1}&offset=${1}`;

  fetch(url)
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
      // document.getElementById("result").innerText = JSON.stringify(data, null, 2);
    })
    .catch((error) => console.error("Ошибка:", error));

    //
}

const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
  

searchInput.addEventListener('keyup', function(event) {
  if (event.keyCode === 13) {
    // simulate a button click to trigger the search
    searchButton.click();
  }
});
// add event listener to the button
searchButton.addEventListener('click', function() {
  // get the search input value
  const searchTerm = searchInput.value;
  //performSearch(searchTerm);
  // do something with the search term (e.g. redirect to a search results page)
  console.log(`Searching for "${searchTerm}"...`);
});

function performSearch(searchTerm) {
  console.log('Searching for:', searchTerm);
  // Implement your search logic here
  // For example, you could make an API call or filter some local data

  // Example: Simulating a search result
  setTimeout(() => {
      alert(`Search results for: ${searchTerm}`);
      // Here you would typically update the DOM with search results
  }, 500);
}

$(document).ready(function () {

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

  // Загрузка задач при загрузке страницы
  loadTasks();

  // Обработчик для фильтрации задач
  $("#incomplete-only").on("change", function () {
    if ($(this).is(":checked")) {
      $('.todo-item input[type="checkbox"]:checked').closest(".todo-item").hide();
    } else {
      $(".todo-item").show();
    }
  });
});
