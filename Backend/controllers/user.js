const pool = require('../db');
bcrypt = require('bcryptjs');



// login user
exports.login = async (req, res) => {
    try {
        await pool.getConnection((err, connection) => {
            if (err) throw err;
            connection.query('SELECT * FROM user WHERE nom = ? ', [req.body.nom], (err, rows) => {
                connection.release();
                if (!err) {
                    if (rows.length > 0) {
                        bcrypt.compare(req.body.password, rows[0].password, (err, result) => {
                            if (result) {
                                res.status(201).json(rows[0]);
                            } else {
                                res.status(401).json({message: 'Wrong password'});
                            }
                        });
                    } else {
                        res.status(404).json({message: 'User does not exist'});
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





