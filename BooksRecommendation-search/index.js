var express = require('express');
var app = express();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var port = process.env.PORT || 2333;
var message_push = io.of('/socket.io');
var ZongJi = require('@rodrigogs/zongji');
var zongji = new ZongJi({
  host: 'localhost',
  user: 'root',
  password: 'docker_qa'
});

app.use(express.json());

app.all('*', function (req, res, next) {
  res.header('Access-Control-Allow-Origin', '*');
  res.header('Access-Control-Allow-Headers', 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild');
  res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS');
  if (req.method == 'OPTIONS') {
    res.sendStatus(200);
  }
  else {
    next();
  }
});

const { Client } = require('@elastic/elasticsearch')
var client = new Client({ node: 'http://localhost:9200' });
async function esSearchBooks(keyword) {
  return await client.search({
    index: 'books',
    type: 'bxbooks',
    body: {
      query: {
        bool: {
          should: [{
            match: {
              BookTitle: keyword
            }
          }, {
            match: {
              BookAuthor: keyword
            }
          }]
        }
      },
      // highlight: {
      //   pre_tags: ["<em>"],
      //   post_tags: ["</em>"],
      //   fields: {
      //     BookTitle: { fragment_size: 200, number_of_fragments: 1 }
      //   }
      // }
    }
  }).catch(function (err) {
    console.log(err);
  });
}
app.post('/search', function (req, res) {
  const keyword = req.body.keyword;
  esSearchBooks(keyword).then(
    results => {
      const messages = []
      results.body.hits.hits.forEach(e => {
        messages.push(e);
      })
      res.json(messages);
    }
  ).catch(function (err) {
    console.log(err);
  });
})

async function esPushBooks(data) {
  await client.index({
    index: 'books',
    type: 'bxbooks',
    id: data.bookid,
    body: {
      BookID: data.bookid,
      ISBN: data.ISBN,
      BookTitle: data.BookTitle,
      BookAuthor: data.BookAuthor,
      YearOfPublication: data.YearOfPublication,
      Publisher: data.Publisher,
      ImageURLS: data.ImageURLS,
      ImageURLM: data.ImageURLM,
      ImageURLL: data.ImageURLL,
      score: data.score
    }
  }).catch(function (err) {
    console.log(err);
  });
}

async function esDeleteBooks(data) {
  await client.delete({
    index: 'books',
    type: 'bxbooks',
    id: data.bookid,
  }).catch(function (err) {
    console.log(err);
  });
}

async function esUpdateBooks(data) {
  console.log(data),
  console.log(data.after.bookid)
  await client.update({
    index:'books',
    type:'bxbooks',
    id:data.after.bookid,
    body:{
      doc:{
      score:data.after.score
      }
    }
  }).catch(function(err){
    console.log(err);
  });
}

var jwt = require('jsonwebtoken');
function getUserId(token) {
  return String(jwt.verify(token, 'JKKLJOoasdlfj', { algorithms: ['HS256'] }).userID);
}

var usocket = {};

message_push.on('connection', function (socket) {
  socket.on('login', function (msg) {
    var userID = getUserId(msg);
    if (!(userID in usocket)) {
      console.log(userID + ' 已连接');
      socket.username = userID;
      usocket[userID] = socket;
    }
  });
  socket.on('disconnect', function (reson) {
    if (socket.username in usocket) {
      delete (usocket[socket.username]);
      console.log(reson);
    }
  });
});

http.listen(2333, function () {
  console.log('listening on:2333');
});

zongji.on('binlog', function (evt) {
  var event = evt.getEventName();
  var table = String(evt.tableMap[evt.tableId].tableName).toLowerCase();
  var rows = evt.rows;
  if (event === 'writerows') {
    if (table === 'bxbooks') {
      esPushBooks(rows[0]);
    }
    console.log(evt.rows);
  } else if (event === 'deleterows') {
    if (table === 'bxbooks') {
      esDeleteBooks(rows[0]);
    } 
    console.log(evt.rows);
  }else if(event==='updaterows'){
    if(table==='bxbooks'){
      for(var i in rows){
        esUpdateBooks(rows[i]);
      }
    }
    // console.log(evt.rows);
  }
});

zongji.start({
  serverId: 1,
  startAtEnd: true,
  includeEvents: ['tablemap', 'writerows', 'updaterows', 'deleterows'],
  includeSchema: { 'books': ['bxbooks'], 'another_db': false }
});

process.on('SIGINT', function () {
  console.log('Got SIGINT.');
  zongji.stop();
  process.exit();
});