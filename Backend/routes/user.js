const router = require('express').Router();
const userController = require('../controllers/user');


// Sign in
router.post('/login', userController.login);



module.exports = router;