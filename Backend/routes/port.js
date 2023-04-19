const router = require('express').Router();
const portController = require('../controllers/port');

// Get all ports
router.get('/', portController.getPorts);
// Get a port
router.get('/:id', portController.getPort);
// Create a port
router.post('/', portController.createPort);
// update a port
router.put('/:id', portController.updatePort);
// delete a port
router.delete('/:id', portController.deletePort);
// get port by user
router.get('/user/:id', portController.getPortByUser);

module.exports = router;