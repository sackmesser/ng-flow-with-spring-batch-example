/*global angular */
'use strict';

/**
 * The main app module
 * @name app
 * @type {angular.Module}
 */
var app = angular.module('app', ['flow'])
    .config(['flowFactoryProvider', function (flowFactoryProvider) {
        flowFactoryProvider.defaults = {
            target: '../upload',
            permanentErrors: [404, 500, 501],
            maxChunkRetries: 1,
            chunkRetryInterval: 5000,
            simultaneousUploads: 4,
            testChunks: false
        };
        flowFactoryProvider.on('catchAll', function (event) {
            console.log('catchAll', arguments);
        });
        // Can be used with different implementations of Flow.js
        // flowFactoryProvider.factory = fustyFlowFactory;
    }]);

app.controller('AppController', ['$scope', function($scope) {
    $scope.$on('flow::fileSuccess', function (event, $file, $message, $data) {
//        console.log('fileSuccess', event);
//        console.log('fileSuccess', $file);
//        console.log('fileSuccess', $message);
//        console.log('fileSuccess', $message.name);
//        console.log('fileSuccess', $data);
        var file = new Blob([$data], { type: 'application/octet-stream' });
        saveAs(file, $message.name + '.converted.txt');
    });
}]);
