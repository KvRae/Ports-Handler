const router = require('express').Router();
const portController = require('../controllers/port');

// get port by user
router.get('/user/:id', portController.getPortByUser);

module.exports = router;