const router = require('express').Router();
const userController = require('../controllers/user');

// Get all users
router.get('/', userController.getUsers);
// Get a user
router.get('/:id', userController.getUser);
// Create a user
router.post('/', userController.createUser);
// Sign in
router.post('/login', userController.login);



module.exports = router;