const pool = require('../db');
bcrypt = require('bcryptjs');


// Get all users
exports.getUsers = async (req, res) => {
    try {
        await pool.getConnection((err, connection) => {
            if (err) throw err;
            console.log('connected as id ' + connection.threadId);
            connection.query('SELECT * FROM user', (err, rows) => {
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
// Get a user
exports.getUser = async (req, res) => {
try {
    await pool.getConnection((err, connection) => {
        if (err) throw err;
        console.log('connected as id ' + connection.threadId);
        connection.query('SELECT * FROM user WHERE id = ?', [req.params.id], (err, rows) => {
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

// Create a user
exports.createUser = async (req, res) => {
    try {
        //hash password
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(req.body.password, salt);

        await pool.getConnection((err, connection) => {
            if (err) throw err;
            connection.query('INSERT INTO user SET nom = ?, password = ?, num_carte = ?', [req.body.nom,hashedPassword, req.body.num_carte], (err, rows) => {
                connection.release();
                if (!err) {
                    res.json({message: 'User registered successfully'});
                } else {
                    console.log(err);
                    res.json({message: 'User exists already'});
                }
            });
        });
    } catch (err) {
        console.error(err.message);
    }
}

// login user
exports.login = async (req, res) => {
    try {
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(req.body.password, salt);

        await pool.getConnection((err, connection) => {
            if (err) throw err;
            connection.query('SELECT * FROM user WHERE num_carte = ? ', [req.body.num_carte], (err, rows) => {
                connection.release();
                if (!err) {
                    if (rows.length > 0) {
                        bcrypt.compare(req.body.password, rows[0].password, (err, result) => {
                            if (result) {
                                res.json({message: 'User logged in successfully'});
                            } else {
                                res.json({message: 'Wrong password'});
                            }
                        });
                    } else {
                        res.json({message: 'User does not exist'});
                    }
                } else {
                    console.log(err);
                }
            });
        });
    }
    catch (err) {
        console.error(err.message);
    }
}





