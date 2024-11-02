const http = require("http");
const https = require("https");
const url = require("url");
const fs = require('fs');

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

const server = http.createServer((req, res) => {
  // Разбираем URL запроса
  const parsedUrl = url.parse(req.url, true);

  if (req.method === "GET" && parsedUrl.pathname === "/") {
    // Read the index.html file
    fs.readFile('public/index.html', 'utf8', (err, data) => {
      if (err) {
        // If there's an error reading the file, respond with a 500 status
        res.writeHead(500, {'Content-Type': 'text/plain'});
        res.end('Internal Server Error');
        return;
      }
      // If successful, respond with the content of index.html
      res.writeHead(200, {'Content-Type': 'text/html'});
      res.end(data);
    });
  } else {
    // Handle 404 for other routes
    res.writeHead(404, {'Content-Type': 'text/plain'});
    res.end('Not Found');
  }


    if (req.method === "GET" && parsedUrl.pathname === "/todos") {
    // Получаем параметры из строки запроса
    const limit = parsedUrl.query.limit || ""; // Значение по умолчанию 2
    const offset = parsedUrl.query.offset || ""; // Значение по умолчанию 0

    // Выполняем запрос к API с параметрами
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
  } else {
    res.writeHead(404, { "Content-Type": "text/plain" });
    res.end("Страница не найдена");
  }
});

server.listen(PORT, () => {
  console.log(`Сервер запущен на http://localhost:${PORT}`);
});
