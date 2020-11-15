const http = require('http');

http.get('http://127.0.0.1:3000/p?a=12&b=4', (resp) => {
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


http.get('http://127.0.0.1:3000/m?a=12&b=4', (resp) => {
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
