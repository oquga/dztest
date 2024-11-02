$(document).ready(function () {
  // Функция для загрузки задач с сервера
  function loadTasks() {
    $.ajax({
      url: "https://todo.doczilla.pro/api/todos",
      method: "GET",
      dataType: "application/json",
      success: function (data) {
        $("#todo-list").empty(); // Очищаем список задач
        console.log(data);
        data.tasks.forEach(function (task) {
          let taskItem = `
                      <div class="todo-item" onclick="showOverlay('${task.title}', '${
            task.time
          }', '${task.description}')">
                          <div class="info">
                              <strong>${task.title}</strong>
                              <p>${task.short_description}</p>
                          </div>
                          <span>${task.time}</span>
                          <input type="checkbox" ${task.completed ? "checked" : ""}>
                      </div>`;
          $("#todo-list").append(taskItem);
        });
      },
      error: function () {
        alert("Не удалось загрузить задачи");
      },
    });
  }

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
