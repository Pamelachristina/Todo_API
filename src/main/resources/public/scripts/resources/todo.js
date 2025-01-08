'use strict';

angular.module('todoListApp')
    .factory('Todo', function($resource) {
        return $resource('http://localhost:8081/api/v1/todos/:id',  // Changed /todo to /todos
            { id: '@id' },
            {
                update: {
                    method: 'PUT'
                }
            }
        );
    });