'use strict';

angular.module('todoListApp')
    .controller('todoCtrl', function($scope, Todo) {
        $scope.deleteTodo = function(todo, index) {
            todo.$delete().then(function() {
                $scope.todos.splice(index, 1);
            });
        };

        $scope.saveTodos = function() {
            var filteredTodos = $scope.todos.filter(function(todo) {
                return todo.edited;
            });

            filteredTodos.forEach(function(todo) {
                if (todo.id) {
                    todo.$update().then(function() {
                        todo.edited = false;
                    });
                } else {
                    todo.$save().then(function() {
                        todo.edited = false;
                    });
                }
            });
        };
    });