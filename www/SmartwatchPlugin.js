var exec = require('cordova/exec');

exports.sendTemplate = function (jsonTemplate, success, error) {
    exec(success, error, 'SmartwatchPlugin', 'sendTemplate', [JSON.stringify(jsonTemplate)]);
};

exports.isSmartwatchConnected = function (success, error) {
    exec(success, error, 'SmartwatchPlugin', 'isSmartwatchConnected', []);
};