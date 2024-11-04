let isNotCompleteTasksOnly = false;

let searchName;
let startDate;
let endDate;

let currentTaskList;



const today = new Date();



document.addEventListener('DOMContentLoaded', function() {
  const searchInput = document.getElementById("searchInput");
  const searchButton = document.getElementById("searchButton");
  const sidebarCheckbox = document.getElementById('tasksFilterCheckbox');
  
  //returns lastCritera to allow pagination by this critera
  searchButton.addEventListener('click', function() {
    search();
    if (sidebarCheckbox.checked){
      sidebarCheckbox.click();
    }
  });

  searchInput.addEventListener('keyup', function(event) {
    if (event.keyCode === 13) {
      // simulate a button click to trigger the search
      searchButton.click();
    }
  });

  

  function handleSidebarCheckbox() { 
    if (sidebarCheckbox.checked) {
      isNotCompleteTasksOnly = true;
    } else {
      isNotCompleteTasksOnly = false;
    }
    console.log("isNotCompleteTaskOnly: "+ isNotCompleteTasksOnly);
  }

  sidebarCheckbox.addEventListener('change', handleSidebarCheckbox);


   todayTasks();
});

function search(){
  currentPage = 1;
  let searchTerm = searchInput.value;
    console.log("startDate "+ startDate +": endDate: "+endDate);

    if(searchTerm.length === 0 ){ //если запрос пуст
      if(startDate === undefined){ // проверить даты
        getAllTasks(currentPage-1)
        lastSearch = getAllTasks;
        //sort array by date
        //sort array by checked
        currentCriteria = "All Tasks";
      } else{
        searchByDates(startDate,endDate,currentPage-1);
        lastSearch = searchByDates;

        currentCriteria = toHumanReadable(startDate);
        if(currentCriteria != toHumanReadable(endDate)){
          currentCriteria += (" : " + new Date(endDate).toLocaleDateString('en-GB', options));
        }
        console.log(currentCriteria)
      } 
    } else{ // если в поисковой строке что то есть 

      if(startDate){ //если указана дата
        console.log("Requested dates with name")
        //getAllTasks()          //придется запрашивать без пагинации все задания
        // и сортировать большой массив 
        //sortByName
        //sortByDate
        currentCriteria = searchTerm + ": "+ startDate + "->" + endDate;
      } else{ //если даты не указаны то можео прямой запрос с пагинацией
        getTasksByName(searchTerm,limit,currentPage-1);
        lastSearch = getTasksByName;
        currentCriteria=searchTerm;
      }
    }

    displayCurrentCriteria(currentCriteria);
    $('#searchInput').val('');
    startDate = undefined;
    endDate = undefined;
}

function sortTasks(taskArray){    
    
}


function searchByDates(start, end, page){
    getTasksByDates(start,end,limit, page*limit);
  
}


function todayTasks(){
  const currentDate = new Date();

  let startOfDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 0, 0, 0, 0);
  let startOfDayInt64 = startOfDay.getTime(); // Convert to int64

  let endOfDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate(), 23, 59, 59, 999);
  let endOfDayInt64 = endOfDay.getTime(); // Convert to int64
  
  startDate = startOfDayInt64;
  endDate = endOfDayInt64;
  //getTasksByDates(startOfDayInt64,endOfDayInt64,4,0);
  console.log(startDate)
  console.log(endDate);
   search();
}

function getWeekRange() {
  var curr = new Date(); // Get current date
  var first = curr.getDate() - curr.getDay() + 1; // First day is Monday
  var last = first + 6; // Last day is Sunday

  // Create new Date objects for first and last day of the week
  startDate = new Date(curr.setDate(first)).getTime();
  endDate = new Date(curr.setDate(last)).getTime();

  search();
}

//sideBar checkbox handler
document.addEventListener('DOMContentLoaded', function() {
  
});

