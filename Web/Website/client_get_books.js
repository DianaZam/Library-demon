const http = require('http');

let author = "Feinman";
let title = "";
let science_field = "";
let key_words = ""

let params = [];

if (author) {
    params.put("author:" + author);
}

json = "{" + params.join(",") + "}"

http.get('http://127.0.0.1:3000/GetBooks?' + json, (resp) => {
  let data = '';

  // A chunk of data has been recieved.
  resp.on('data', (chunk) => {
    data += chunk;
  });

  // The whole response has been received. Print out the result.
  resp.on('end', () => {
    console.log("ans: " + data);
  });

}).on("error", (err) => {
  console.log("Error: " + err.message);
});
