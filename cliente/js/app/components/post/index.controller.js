(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('PostIndexController', PostIndexController);

    PostIndexController.$inject = ['$scope', '$rootScope', '$http', '$routeParams', 'messages'];

    /**
     * @namespace PostIndexController
     */
    function PostIndexController($scope, $rootScope, $http, $routeParams, messages) {
        // Classes
        var Model = function () {
            if (this instanceof Model) {
                this.atributos = [];
                this.dependencias = [];
            } else {
                return new Model();
            }
        };

        var vm = this;

        vm.submit = submit;

        function submit() {
            var content = angular.copy(vm.comment);
            if (content) {
                $http.post($rootScope.servicesUrl + 'comments', {
                    content: content,
                    userid: $rootScope.loggedUser.id,
                    name: $rootScope.loggedUser.name,
                    email: $rootScope.loggedUser.email,
                    postid: vm.post.id
                })
                    .then(function (response) {
                        console.log('Create comment:');
                        console.log(response);
                        var comment = response.data.element;
                        vm.post.comments = vm.post.comments || [];
                        vm.post.comments.push(comment);
                        messages.success('El comentario ha sido creado exitosamente.');
                    }).catch(function (error) {
                        console.error(error);
                        var message = '';
                        var data = error.data;
                        if (data && data.msg) {
                            message = data.msg;
                        } else if (error.statusText) {
                            message = error.statusText;
                        }
                        messages.error('Ha ocurrido un error al crear el comentario: ' + message);
                    });
            } else {
                messages.error('No es posible agregar un comentario sin contenido.');
            }

            vm.comment = '';
        }

        function getPost() {
            var id = $routeParams.id;
            console.log($rootScope.servicesUrl + 'post/' + id);
            $http.get($rootScope.servicesUrl + 'post/' + id)
                .then(function (response) {
                    console.log('Post:');
                    console.log(response);
                    vm.post = response.data.element;
                    vm.post.content = vm.post.content.replace(/\\n/g, '<br />');
                    $rootScope.title = vm.post.title;
                    $rootScope.subtitle = vm.post.subtitle;
                }).catch(function (error) {
                    console.error(error);
                    var message = '';
                    var data = error.data;
                    if (data && data.msg) {
                        message = data.msg;
                    } else if (error.statusText) {
                        message = error.statusText;
                    }
                    messages.error('Ha ocurrido un error al cargar el post ' + id + ': ' + message);
                });
        }

        getPost();
    }

})();
