'use strict';

angular.module('todoListApp')
    .controller('mainCtrl', ['$scope', 'Todo', function($scope, Todo) {  // Using array notation
        $scope.todos = [];

        // Load initial todos
        Todo.query().$promise.then(function(todos) {
            $scope.todos = todos;
            console.log('Todos loaded:', todos);
        }).catch(function(error) {
            console.error('Error loading todos:', error);
        });

        $scope.addTodo = function() {
            console.log('Add todo clicked');
            var todo = new Todo({
                name: 'New task!',
                completed: false,
                edited: false
            });

            todo.$save(function(savedTodo) {
                console.log('Todo saved:', savedTodo);
                $scope.todos.unshift(savedTodo);
            }, function(error) {
                console.error('Error saving todo:', error);
            });
        };

        $scope.saveTodos = function() {
            var filteredTodos = $scope.todos.filter(function(todo) {
                return todo.edited;
            });

            filteredTodos.forEach(function(todo) {
                if (todo.id) {
                    todo.$update(function() {
                        console.log('Todo updated successfully');
                        todo.edited = false;
                    }, function(error) {
                        console.error('Error updating todo:', error);
                    });
                } else {
                    todo.$save(function() {
                        console.log('New todo saved successfully');
                        todo.edited = false;
                    }, function(error) {
                        console.error('Error saving new todo:', error);
                    });
                }
            });
        };

        $scope.deleteTodo = function(todo, index) {
            todo.$delete(function() {
                console.log('Todo deleted successfully');
                $scope.todos.splice(index, 1);
            }, function(error) {
                console.error('Error deleting todo:', error);
            });
        };
    }]);