const http = require('http');
const url = require('url');
const fs   = require('fs');
var NodeSession = require('node-session');
var mustache = require('mustache');
var cardId = 0;

const { parse } = require('querystring');
const querystring = require('querystring');

const hostname = '127.0.0.1';
const port = 3001;
const remoteHost = 'http://127.0.0.1';
const remotePort = 9099;

function buildPage(title, htmlfile, res, context = {}) {
    fs.readFile('./pages/' + htmlfile, function(err, content) {
        context.card_id = cardId;

        if (context) {
            var content = mustache.render("" + content, context);
        }

        buildPageFromString(title, content, res);
    });
}

function buildPageFromString(title, content, res) {
    fs.readFile('./pages/layout.html', function(err, layout) {
        context = {
            title: title,
            content: "" + content,
            card_id: cardId
        }

        var html = mustache.render("" + layout, context);
        res.end(html);
    });
}

session = new NodeSession({secret: 'fkfkj3599hjfjw9usdhsdkfadhs39275'});

const server = http.createServer((req, res) => {
  res.statusCode = 200;
  res.setHeader('Content-Type', 'text/html');
  session.startSession(req, res, function () {
    var query = url.parse(req.url, true).query;
    var parsedUrl = url.parse(req.url, true);
    const err = "";

    console.log(url.parse(req.url, true));

    cardId = req.session.get('card_id', 0);

    if (parsedUrl.pathname == '/') {
        buildPage("Главная", "index.html", res);

    } else if (parsedUrl.pathname == '/out') {
        cardId = 0;
        req.session.flush();
        res.writeHead(301, { Location: '/' });
        res.end("OK");
    } else if (parsedUrl.pathname == '/logo.gif') {
        res.setHeader('Content-Type', 'image/gif');
        fs.readFile('./logo.gif', function(err, data) {
            res.end(data);
        });
    } else if (parsedUrl.pathname == '/about') {
        buildPage("О нас", "about.html", res);
    } else if (parsedUrl.pathname == '/book_out') {
        fs.readFile('./book_out.html', function(err, data) {
            res.end(data);
        });
    } else if (parsedUrl.pathname == '/book_out_results') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);

            if (params.book_id.length > 0) {
                paramPairs.push('"book_id": ' + params.book_id + '');
            } else {
                paramPairs.push('"book_id": null');
            }
            
            if (params.location_id.length > 0) {
                paramPairs.push('"location_id": ' + params.location_id + '');
            } else {
                paramPairs.push('"location_id": null');
            }

            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/ChangeBookStatusOut",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    
                    // response body as string
                    console.log("Response: " + response_body.toString());
                });
            });
            
            request.write(json);
            request.end();

            res.end('ok');
        });

    } else if (parsedUrl.pathname == '/book_return') {
        fs.readFile('./book_return.html', function(err, data) {
            res.end(data);
        });
    } else if (parsedUrl.pathname == '/book_return_results') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);

            if (params.book_id.length > 0) {
                paramPairs.push('"book_id": ' + params.book_id + '');
            } else {
                paramPairs.push('"book_id": null');
            }
            
            if (params.location_id.length > 0) {
                paramPairs.push('"location_id": ' + params.location_id + '');
            } else {
                paramPairs.push('"location_id": null');
            }

            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/ChangeBookStatusOut",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    
                    // response body as string
                    console.log("Response: " + response_body.toString());
                });
            });
            
            request.write(json);
            request.end();

            res.end('ok');
        });

    } else if (parsedUrl.pathname == '/is_book_in') {
        fs.readFile('./is_book_in.html', function(err, data) {
            res.end(data);
        });
    } else if (parsedUrl.pathname == '/is_book_in_results') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);

            if (params.book_id.length > 0) {
                paramPairs.push('"book_id": ' + params.book_id + '');
            } else {
                paramPairs.push('"book_id": null');
            }

            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/IsBookInStock",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    
                    // response body as string
                    console.log("Response: " + response_body.toString());
                });
            });
            
            request.write(json);
            request.end();

            res.end('ok');
        });

    } else if (parsedUrl.pathname == '/get_book_status') {
        fs.readFile('./get_book_status.html', function(err, data) {
            res.end(data);
        });
    } else if (parsedUrl.pathname == '/get_book_status_results') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);

            if (params.book_id.length > 0) {
                paramPairs.push('"book_id": ' + params.book_id + '');
            } else {
                paramPairs.push('"book_id": null');
            }
            
            if (params.location_id.length > 0) {
                paramPairs.push('"location_id": ' + params.location_id + '');
            } else {
                paramPairs.push('"location_id": null');
            }

            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/GetBookStatus",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    
                    // response body as string
                    console.log("Response: " + response_body.toString());
                });
            });
            
            request.write(json);
            request.end();

            res.end('ok');
        });

    } else if (parsedUrl.pathname == '/add_reader') {
        buildPage('Регистрация', 'add_reader.html', res);
    } else if (parsedUrl.pathname == '/add_reader_results') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);

            if (params.card_id.length > 0) {
                paramPairs.push('"card_id": ' + params.card_id + '');
            } else {
                paramPairs.push('"card_id": null');
            }

            if (params.passport.length > 0) {
                paramPairs.push('"passport": ' + params.passport + '');
            } else {
                paramPairs.push('"passport": null');
            }

            if (params.first_name.length > 0) {
                paramPairs.push('"first_name": "' + params.first_name + '"');
            } else {
                paramPairs.push('"first_name": null');
            }

            if (params.middle_name.length > 0) {
                paramPairs.push('"middle_name": "' + params.middle_name + '"');
            } else {
                paramPairs.push('"middle_name": null');
            }

            if (params.last_name.length > 0) {
                paramPairs.push('"last_name": "' + params.last_name + '"');
            } else {
                paramPairs.push('"last_name": null');
            }

            if (params.birthday.length > 0) {
                paramPairs.push('"birthday": "' + params.birthday + '"');
            } else {
                paramPairs.push('"birthday": null');
            }

            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/AddReader",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let content = '<div class="alert alert-success">Пользователь зарегистрирован</div>';
                    let response_body = Buffer.concat(chunks_of_data);
                    buildPageFromString("Регистрация", content, res);
                    console.log("Response: " + response_body.toString());
                });
            });
            
            request.write(json);
            request.end();

            //res.end('ok');
        });

    } else if (parsedUrl.pathname == '/login') {
        buildPage('Авторизация', 'login.html', res);
    } else if (parsedUrl.pathname == '/login_result') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);

            if (params.card_id.length > 0) {
                paramPairs.push('"card_id": ' + params.card_id + '');
            } else {
                paramPairs.push('"card_id": null');
            }

            if (params.passport.length > 0) {
                paramPairs.push('"passport": ' + params.passport + '');
            } else {
                paramPairs.push('"passport": null');
            }

            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/GetReader",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    let reader = JSON.parse(response_body.toString());

                    console.log("Response: " + response_body.toString());
                    
                    if (reader.card_id) {
                        req.session.put('card_id', reader.card_id);
                        res.writeHead(301, { Location: '/card' });
                        res.end();
                
                        /*buildPageFromString(
                            'Авторизация', 
                            'Вы вошли как: ' + reader.first_name + ' ' + reader.last_name, 
                            res
                        );*/
                    } else {
                        const context = {
                            err: "Неверная пара логин/пароль"
                        }
                        
                        buildPage('Авторизация', 'login.html', res, context);
                    }
                });
            });
            
            request.write(json);
            request.end();
        });

    } else if (parsedUrl.pathname == '/card') {
        let paramPairs = [];
        let cardId = req.session.get('card_id', 0);

        if (cardId > 0) {
            paramPairs.push('"card_id": ' + cardId);
            paramPairs.push('"passport": null');
            let json = "{" + paramPairs.join(",") + "}";

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/GetReader",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    let reader = JSON.parse(response_body.toString());

                    fs.readFile('./pages/card.html', function(err, content) {
                        let content2 = mustache.render("" + content, context)
                        buildPageFromString('Карточка читателя: ', content2, res);
                    });

                    const context = {
                        card_id: req.session.get('card_id', 0),
                        reader: reader,
                        books: reader.data.books
                    };
                });
            });
            
            request.write(json);
            request.end();
        } else {
            buildPage('Карточка читателя: требуется авторизация', 'card_na.html', res);
        }

    } else if (parsedUrl.pathname == '/catalog') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);
            paramPairs.push('"author": null');
            paramPairs.push('"title": null');
            paramPairs.push('"science_field": null');
            paramPairs.push('"key_words": null');
            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/GetBooks",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    let response_json = JSON.parse(response_body.toString())

                    let books = [];

                    for (const b of response_json) {
                        b.key_words_str = b.key_words;
                        books.push(b);
                    }

                    let context = {
                        books: books
                    }

                    fs.readFile('./pages/search_results.html', function(err, content) {
                        console.log("context: ");
                        console.debug(context);
                        let page = mustache.render("" + content, context);
                        buildPageFromString('Каталог', page, res);
                    });

                    // response body as string
                    console.log("Response: " + response_body.toString());
                });
            });
            
            request.write(json);
            request.end();
        });
    } else if (parsedUrl.pathname == '/search') {
        buildPage('Поиск книги', 'search.html', res);
    } else if (parsedUrl.pathname == '/search_results') {
        let body = '';
        req.on('data', chunk => {
            body += chunk.toString();
        });
        req.on('end', () => {
            console.log(body);
            let paramPairs = [];
            let params = parse(body);

            if (params.author.length > 0) {
                paramPairs.push('"author": "' + params.author + '"');
            } else {
                paramPairs.push('"author": null');
            }

            if (params.title.length > 0) {
                paramPairs.push('"title": "' + params.title + '"');
            } else {
                paramPairs.push('"title": null');
            }

            if (params.science_field.length > 0) {
                paramPairs.push('"science_field": "' + params.science_field + '"');
            } else {
                paramPairs.push('"science_field": null');
            }

            if (params.key_words.length > 0) {
                paramPairs.push('"key_words": "' + params.key_words + '"');
            } else {
                paramPairs.push('"key_words": null');
            }

            let json = "{" + paramPairs.join(",") + "}";

            console.log("Sending json: " + json);

            const options = {
                url: remoteHost,
                port: remotePort,
                path: "/GetBooks",
                method: "POST"
            }
            
            const request = http.request(options, (response) => {
                let chunks_of_data = [];

                // gather chunk
                response.on('data', (fragments) => {
                    chunks_of_data.push(fragments);
                });
            
                // no more data to come
                // combine all chunks
                response.on('end', () => {
                    let response_body = Buffer.concat(chunks_of_data);
                    let response_json = JSON.parse(response_body.toString())

                    let books = [];

                    for (const b of response_json) {
                        b.key_words_str = b.key_words;
                        books.push(b);
                    }

                    let context = {
                        books: books
                    }

                    fs.readFile('./pages/search_results.html', function(err, content) {
                        console.log("context: ");
                        console.debug(context);
                        let page = mustache.render("" + content, context);
                        buildPageFromString('Результаты поиска', page, res);
                    });

                    // response body as string
                    console.log("Response: " + response_body.toString());
                });
            });

            request.write(json);
            request.end();
        });

    } else {
        res.statusCode = 404;
        res.end("Page not found");
    }
  });
});

server.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});
