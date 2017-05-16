(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('UsersListController', UsersListController);

    UsersListController.$inject = ['$scope', '$rootScope', '$http', '$routeParams', 'messages', 'SweetAlert'];

    /**
     * @namespace UsersListController
     */
    function UsersListController($scope, $rootScope, $http, $routeParams, messages, SweetAlert) {
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

        vm.users = [];

        vm.remove = remove;

        vm.create = create;

        vm.update = update;

        vm.addNew = addNew;

        function remove(user) {
            SweetAlert.swal({
                title: '¿Desea continuar?',
                text: 'Esta a punto de eliminar al usuario ' + user.email + '. Esta operación no se puede deshacer.',
                type: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d9534f',
                confirmButtonText: 'Continuar',
                cancelButtonText: 'Cancelar'
            },
                function (isConfirm) {
                    if (isConfirm) {
                        $http.delete($rootScope.servicesUrl + 'users/' + user.id)
                            .then(function (response) {
                                console.log('Remove user:');
                                console.log(response);
                                vm.users = vm.users.filter(function (current) {
                                    return current.id !== user.id;
                                });
                                messages.success('El usuario ' + user.email + ' ha sido eliminado exitosamente.');
                            }).catch(function (error) {
                                console.error(error);
                                var message = '';
                                var data = error.data;
                                if (data && data.msg) {
                                    message = data.msg;
                                } else if (error.statusText) {
                                    message = error.statusText;
                                }
                                messages.error('Ha ocurrido un error al eliminar el usuario ' + user.email + ': ' + message);
                            });
                    }
                });
        }

        function create() {
            if (vm.name && vm.email) {
                vm.email = vm.email.toLowerCase();
                var index = vm.users.findIndex(function (user) {
                    return user.email.toLowerCase() === vm.email;
                });
                if (index === -1) {
                    $http.post($rootScope.servicesUrl + 'users', {
                        name: vm.name,
                        email: vm.email
                    })
                        .then(function (response) {
                            console.log('Create user:');
                            console.log(response);
                            var user = response.data.element;
                            vm.users.push(user);
                            messages.success('El usuario ' + user.email + ' ha sido creado exitosamente.');
                        }).catch(function (error) {
                            console.error(error);
                            var message = '';
                            var data = error.data;
                            if (data && data.msg) {
                                message = data.msg;
                            } else if (error.statusText) {
                                message = error.statusText;
                            }
                            messages.error('Ha ocurrido un error al crear el nuevo usuario: ' + message);
                        });
                } else {
                    messages.error('Ha ocurrido un error al crear el nuevo usuario: El usuario con el correo ' + vm.email + ' ya existe.');
                }

            } else {
                messages.error('Ha ocurrido un error al crear el nuevo usuario: El nombre y correo son obligatorios.');
            }

            vm.allowNewUser = false;
        }

        function update() {

        }

        function addNew() {
            vm.name = '';
            vm.email = '';
            vm.allowNewUser = true;
        }

        function getUsers() {
            if ($rootScope.loggedUser && $rootScope.loggedUser.id) {
                var id = $rootScope.loggedUser.id;
                $http.get($rootScope.servicesUrl + 'users')
                    .then(function (response) {
                        console.log('Users:');
                        console.log(response);
                        vm.users = response.data.elements;
                    }).catch(function (error) {
                        console.error(error);
                        var message = '';
                        var data = error.data;
                        if (data && data.msg) {
                            message = data.msg;
                        } else if (error.statusText) {
                            message = error.statusText;
                        }
                        messages.error('Ha ocurrido un error al cargar los usuarios: ' + message);
                    });
            } else {
                messages.error('El usuario no tiene permiso para acceder a los usuarios.');
            }
        }


        getUsers();
    }

})();
