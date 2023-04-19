const pool = require('../db');

// Get all ports
exports.getPorts = async (req, res) => {
    try {
        await pool.getConnection((err, connection) => {
            if (err) throw err;
            console.log('connected as id ' + connection.threadId);
            connection.query('SELECT * FROM port', (err, rows) => {
                connection.release();
                if (!err) {
                    res.json(rows);
                } else {
                    console.log(err);
                }
            });
        }
        );
    } catch (err) {
        console.error(err.message);
    }
}

// Get a port
exports.getPort = async (req, res) => {
try {
    await pool.getConnection((err, connection) => {
        if (err) throw err;
        console.log('connected as id ' + connection.threadId);
        connection.query('SELECT * FROM port WHERE id = ?', [req.params.id], (err, rows) => {
            connection.release();
            if (!err) {
                res.json(rows);
            } else {
                console.log(err);
            }
        });
    });

} catch (err) {

}
}

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

// Create a port
exports.createPort = async (req, res) => {
    try {
        await pool.getConnection((err, connection) => {
            if (err) throw err;
            connection.query('INSERT INTO port SET nom = ?, carte_user = ?', [req.body.nom, req.body.carte_user], (err, rows) => {
                connection.release();
                if (!err) {
                    res.json({message: 'Port registered successfully'});
                } else {
                    console.log(err);
                    res.json({message: err.message});
                }
            });
        });
    } catch (err) {
        console.error(err.message);
    }
}

// Update a port
exports.updatePort = async (req, res) => {
    try {
        await pool.getConnection((err, connection) => {
            if (err) throw err;
            connection.query('UPDATE port SET nom = ?, carte_user = ? WHERE id = ?', [req.body.nom, req.body.carte_user, req.params.id], (err, rows) => {
                connection.release();
                if (!err) {
                    res.json({message: 'Port updated successfully'});
                } else {
                    console.log(err);
                    res.json({message: 'Port exists already'});
                }
            });
        });
    } catch (err) {
        console.error(err.message);
    }
}

// Delete a port
exports.deletePort = async (req, res) => {
    try {
        await pool.getConnection((err, connection) => {
            if (err) throw err;
            connection.query('DELETE FROM port WHERE id = ?', [req.params.id], (err, rows) => {
                connection.release();
                if (!err) {
                    res.json({message: 'Port deleted successfully'});
                } else {
                    console.log(err);
                    res.json({message: 'Port exists already'});
                }
            });
        });
    } catch (err) {
        console.error(err.message);
    }
}