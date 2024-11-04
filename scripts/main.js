let lastSearch;

let sortByDate = false;
let currentPage;
let nextPage;

const limit = 4;

const options = { day: 'numeric', month: 'long', year: 'numeric' };
// const formattedDate = date.toLocaleDateString('en-GB', options)


function toHumanReadable(date){
    date = new Date(date).toLocaleDateString('en-GB', options);
    return date
}

//change actual criteria 
function displayCurrentCriteria(criteria) {
    console.log("Now criteria is: "+criteria);
    $('#currentCritera').text(criteria);
};

function generateTodoItems(tasks) {
    console.log(tasks);
    currentTaskList = tasks;
    const todoListcontainer = document.getElementsByClassName('todo-list')[0];
    
    todoListcontainer.innerHTML = '';
    
    for (let i = 0; i < tasks.length; i++) {
        let todoItem = `
            <div class="todo-item" onclick="showOverlay(${i})">
                <div class="taskName">
                    ${tasks[i].name}
                </div>
                <div class="info">
                    <div class="taskShortDesc">
                        ${tasks[i].shortDesc}
                    </div>
                    <div class="taskStatus">
                        <input type="checkbox" ${tasks[i].status ? 'checked' : ''}>
                    </div>
                </div>
                <div class="taskDate">
                    ${formatDate(tasks[i].date)}
                </div>
            </div>
        `; 
        todoListcontainer.innerHTML += todoItem;
    } 
  }
  
  