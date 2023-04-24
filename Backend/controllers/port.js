const pool = require('../db');


//get port by user
exports.getPortByUser = async (req, res) => {
    try {
        await pool.getConnection((err, connection) => {
            if (err) throw err;
            console.log('connected as id ' + connection.threadId);
            connection.query('SELECT * FROM port WHERE carte_user = ?', [req.params.id], (err, rows) => {
                connection.release();
                if (!err) {
                    res.json(rows);
                } else {
                    console.log(err);
                }
            });
        });
    } catch (err) {
        console.error(err.message);
    }
}