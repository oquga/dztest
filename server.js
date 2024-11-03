const http = require("http");
const https = require("https");
const url = require("url");

const PORT = 3000;

function fetchToDos(limit, offset, callback) {
  // Формируем URL с параметрами
  const apiUrl = `https://todo.doczilla.pro/api/todos?limit=${limit}&offset=${offset}`;

  https
    .get(apiUrl, (res) => {
      let data = "";

      // Получаем данные от API
      res.on("data", (chunk) => {
        data += chunk;
      });

      // Обрабатываем завершение запроса
      res.on("end", () => {
        callback(data);
      });
    })
    .on("error", (err) => {
      console.error("Ошибка запроса:", err.message);
      callback(null, `Ошибка запроса: ${err.message}`);
    });
}

https://todo.doczilla.pro/api/todos/date?from=1704926400000&to=1704943800000&status=false&limit=1&offset=0
function fetchToDosByDate(from, to, isDone, limit, offset, callback) {
  let apiUrl = `https://todo.doczilla.pro/api/todos/date?from=${from}&to=${to}&limit=${limit}&offset=${offset}`;
  if(isDone!=null || isDone!=undefined){
     apiUrl = `https://todo.doczilla.pro/api/todos/date?from=${from}&to=${to}&status=${isDone}&limit=${limit}&offset=${offset}`;
  } 
  
  https
    .get(apiUrl, (res) => {
      let data = "";

      // Получаем данные от API
      res.on("data", (chunk) => {
        data += chunk;
      });

      // Обрабатываем завершение запроса
      res.on("end", () => {
        callback(data);
      });
    })
    .on("error", (err) => {
      console.error("Ошибка запроса:", err.message);
      callback(null, `Ошибка запроса: ${err.message}`);
    });
}


function fetchToDosByName(taskName, limit, offset, callback) {
  // Формируем URL с параметрами
  const apiUrl = `https://todo.doczilla.pro/api/todos/find?q=${taskName}&limit=${limit}&offset=${offset}`;
  
  https
    .get(apiUrl, (res) => {
      let data = "";

      // Получаем данные от API
      res.on("data", (chunk) => {
        data += chunk;
      });

      // Обрабатываем завершение запроса
      res.on("end", () => {
        callback(data);
      });
    })
    .on("error", (err) => {
      console.error("Ошибка запроса:", err.message);
      callback(null, `Ошибка запроса: ${err.message}`);
    });
}

const server = http.createServer((req, res) => {
  // Разбираем URL запроса
  const parsedUrl = url.parse(req.url, true);

  if (req.method === "GET" && parsedUrl.pathname.startsWith("/todos")) {
    console.log(parsedUrl.pathname)
    
      const limit = parsedUrl.query.limit || ""; // Значение по умолчанию 2
      const offset = parsedUrl.query.offset || ""; // Значение по умолчанию 0
      
      if (parsedUrl.pathname === "/todos"){
        fetchToDos(limit, offset, (data, error) => {
          res.writeHead(200, {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
          });
          if (error) {
            res.end(JSON.stringify({ error }));
          } else {
            res.end(data);
          }
        });
      } else if(parsedUrl.pathname === "/todos/find"){
        const taskName = parsedUrl.query.q;
        fetchToDosByName(taskName,limit,offset, (data, error) => {
          res.writeHead(200, {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
          });
          if (error) {
            res.end(JSON.stringify({ error }));
          } else {
            res.end(data);
          }
        });

      } else if (parsedUrl.pathname === "/todos/date"){
        const from = parsedUrl.query.from;
        const to = parsedUrl.query.to;
        const status = parsedUrl.query.status || null;
        
        fetchToDosByDate(from,to,status,limit,offset, (data, error) => {
          res.writeHead(200, {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
          });
          if (error) {
            res.end(JSON.stringify({ error }));
          } else {
            res.end(data);
          }
        });
      }else {
        res.writeHead(404, { "Content-Type": "text/plain" });
        res.end("Страница не найдена");
      }

    } 
  
});

server.listen(PORT, () => {
  console.log(`Сервер запущен на http://localhost:${PORT}`);
});
