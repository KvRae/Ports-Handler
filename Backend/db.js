const mysql = require('mysql');

 const pool = mysql.createPool({
    connectionLimit: 10,
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'my_database'
});
 const getConnection = (callback) => {
    pool.getConnection((err, connection) => {
        callback(err, connection);
    });
}


module.exports = {getConnection, pool}





