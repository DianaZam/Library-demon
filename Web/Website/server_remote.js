const http = require('http');
const url = require('url');
const fs   = require('fs');

const { parse } = require('querystring');

const hostname = '127.0.0.1';
const port = 3002;

function buildResponse(filename, res) {
    fs.readFile('./ans/' + filename, function(err, data) {
        res.end(data);
    });
}

const server = http.createServer((req, res) => {
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/json');
  
  var query = url.parse(req.url, true).query;
  var parsedUrl = url.parse(req.url, true);
  console.log(url.parse(req.url, true));
  // 
// res.end(req.url);
    if (parsedUrl.pathname == '/') {
        res.end("{ result: 'OK' }");
    } else if (parsedUrl.pathname == '/GetBooks') {
        buildResponse('book_list.json', res);
    } else if (parsedUrl.pathname == '/GetReader') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            
            let paramPairs = [];
            let params = JSON.parse(body);
            console.log(params);

            if (params.card_id == "123") {
                buildResponse('no_result.json', res);
            } else {
                buildResponse('reader_list.json', res);
            }
        });
    } else if (parsedUrl.pathname == '/AddReader') {
        buildResponse('reader_add.json', res);
    } else if (parsedUrl.pathname == '/GetBookStatus') {
        buildResponse('book_status.json', res);
    } else if (parsedUrl.pathname == '/IsBookInStock') {
        buildResponse('is_book_in.json', res);
    } else if (parsedUrl.pathname == '/ChangeBookStatusOut') {
        buildResponse('book_out.json', res);
    } else {
        res.statusCode = 404;
        res.setHeader('Content-Type', 'text/html');
        res.end("Page not found");
    }
});

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});
