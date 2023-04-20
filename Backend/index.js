const express = require('express');
const bodyParser = require('body-parser');
const users = require('./routes/user');
const ports = require('./routes/port');
const app = express();
const server = app.listen(5000 || process.env.PORT, () => console.log(`Server started on port ${port}`));


// Port
const port = process.env.PORT || 5000;

// Body Parser Middleware
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json());

// Routes
app.use('/users', users);
app.use('/ports', ports);

// Socket.io
const io = require('socket.io')(server);
var portId = "";
io.on('connection', (socket) => {
    console.log('new connection made.'+socket.id);
    socket.on('connect_port', (data) => {
        portId = data;
        console.log('received from phone '+portId);
        socket.emit('connected_port', 'connected to '+portId);
        console.log('sent to phone '+portId);
    });
});

