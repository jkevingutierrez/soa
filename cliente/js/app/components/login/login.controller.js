(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('LoginController', LoginController);

    LoginController.$inject = ['$scope', '$rootScope', '$http', '$location', 'messages'];

    /**
     * @namespace LoginController
     */
    function LoginController($scope, $rootScope, $http, $location, messages) {
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

        vm.login = login;

        function login() {
            vm.email = vm.email.toLowerCase();
            $http.post($rootScope.servicesUrl + 'login', {
                name: vm.name,
                email: vm.email
            })
                .then(function (response) {
                    console.log('Login:');
                    console.log(response);
                    $location.path('/');
                    $rootScope.loggedUser = response.data.element;
                    messages.success('El usuario ' + $rootScope.loggedUser.email + ' ha iniciado sesion exitosamente.');
                }).catch(function (error) {
                    console.error(error);
                    var message = '';
                    var data = error.data;
                    if (data && data.msg) {
                        message = data.msg;
                    } else if (error.statusText) {
                        message = error.statusText;
                    }
                    messages.error('Ha ocurrido un error al hacer el login: ' + message);
                });
        }

    }

})();
