(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('RegisterController', RegisterController);

    RegisterController.$inject = ['$scope', '$rootScope', '$http', '$location', 'messages'];

    /**
     * @namespace RegisterController
     */
    function RegisterController($scope, $rootScope, $http, $location, messages) {
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
            if (vm.name && vm.email) {
                vm.email = vm.email.toLowerCase();

                $http.post($rootScope.servicesUrl + 'users', {
                    name: vm.name,
                    email: vm.email
                })
                    .then(function (response) {
                        console.log('Register user:');
                        console.log(response);
                        $rootScope.loggedUser = response.data.element;
                        $location.path('/');
                        messages.success('El usuario ' + $rootScope.loggedUser.email + ' ha sido creado exitosamente.');
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
                messages.error('Ha ocurrido un error al crear el nuevo usuario: El nombre y correo son obligatorios.');
            }
        }
    }

})();
