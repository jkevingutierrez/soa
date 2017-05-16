(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('PostListController', PostListController);

    PostListController.$inject = ['$scope', '$rootScope', '$http', '$routeParams', 'messages', 'SweetAlert'];

    /**
     * @namespace PostListController
     */
    function PostListController($scope, $rootScope, $http, $routeParams, messages, SweetAlert) {
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

        vm.posts = [];

        vm.remove = remove;

        function remove(post) {
            SweetAlert.swal({
                title: '¿Desea continuar?',
                text: 'Esta a punto de eliminar el post ' + post.title + '. Esta operación no se puede deshacer.',
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d9534f',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            },
                function (isConfirm) {
                    if (isConfirm) {
                        $http.delete($rootScope.servicesUrl + 'posts/' + post.id)
                            .then(function (response) {
                                console.log('Remove post:');
                                console.log(response);
                                vm.posts = vm.posts.filter(function (current) {
                                    return current.id !== post.id;
                                });
                                messages.success('El post ' + post.title + ' ha sido eliminado exitosamente.');
                            }).catch(function (error) {
                                console.error(error);
                                var message = '';
                                var data = error.data;
                                if (data && data.msg) {
                                    message = data.msg;
                                } else if (error.statusText) {
                                    message = error.statusText;
                                }
                                messages.error('Ha ocurrido un error al eliminar el post ' + post.title + ': ' + message);
                            });
                    }
                });
        }

        function getPosts() {
            var userId = $routeParams.userId;
            if ($rootScope.loggedUser && $rootScope.loggedUser.id && $rootScope.loggedUser.id === userId) {
                var id = $rootScope.loggedUser.id;
                $http.get($rootScope.servicesUrl + 'posts/' + userId)
                    .then(function (response) {
                        console.log('Posts:');
                        console.log(response);
                        vm.posts = response.data.elements;
                    }).catch(function (error) {
                        console.error(error);
                        var message = '';
                        var data = error.data;
                        if (data && data.msg) {
                            message = data.msg;
                        } else if (error.statusText) {
                            message = error.statusText;
                        }
                        messages.error('Ha ocurrido un error al cargar los posts: ' + message);
                    });
            } else {
                messages.error('El usuario no tiene permiso para acceder a los posts.');
            }
        }

        getPosts();
    }

})();
