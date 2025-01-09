'use strict';

angular.module('todoListApp')
    .factory('Todo', function($resource){
        return $resource('http://127.0.0.1:8081/api/v1/todos/:id', {id: '@id'}, {
            update: {
                method: 'PUT'
            }
        });
    });

