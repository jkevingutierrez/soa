(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('MainController', MainController);

    MainController.$inject = ['$scope', '$rootScope', '$http', '$location', 'messages'];

    /**
     * @namespace MainController
     */
    function MainController($scope, $rootScope, $http, $location, messages) {
        // Classes
        var Model = function () {
            if (this instanceof Model) {
                this.atributos = [];
                this.dependencias = [];
            } else {
                return new Model();
            }
        };

        var baseUrl = 'http://localhost:8000/';
        var vm = this;

        vm.hasFinishedLoading = true;
        vm.hasErrors = false;

        vm.logout = logout;

        $rootScope.servicesUrl = baseUrl;
        $rootScope.orderByDate = orderByDate;
        $rootScope.title = 'Blog SOA';
        $rootScope.subtitle = 'Un blog inspirado en los principios solid de la POO';

        function logout() {
            $http.get($rootScope.servicesUrl + 'logout')
                .then(function (response) {
                    console.log('Logout:');
                    console.log(response);
                    $location.path('/');
                    $rootScope.loggedUser = null;
                    messages.success('El usuario ha cerrado sesion exitosamente.');
                }).catch(function (error) {
                    console.error(error);
                    var message = '';
                    var data = error.data;
                    if (data && data.msg) {
                        message = data.msg;
                    } else if (error.statusText) {
                        message = error.statusText;
                    }
                    messages.error('Ha ocurrido un error al hacer el logout: ' + message);
                });
        }

        function orderByDate(item) {
            if (item && item.createdDate) {
                var parts = item.createdDate.split('/');
                var number = parseInt(parts[2] + parts[1] + parts[0]);
                return -number;
            }
            return 0;
        }

        function getLoggedUser() {
            console.log('logged User');
            // $rootScope.loggedUser = {
            //     email: "kevingutierrezg@gmail.com",
            //     id: "58f19e449a7b4a4e0118bdcb",
            //     name: "Kevin"
            // };

            // $http.get($rootScope.servicesUrl + 'login')
            //     .then(function(response) {
            //         console.log('Get logged user:');
            //         console.log(response);
            //         $rootScope.loggedUser = response.data.element;
            //     }).catch(function(error) {
            //         console.error(error);
            //         var message = '';
            //         var data = error.data;
            //         if (data && data.msg) {
            //             message = data.msg;
            //         } else if (error.statusText) {
            //             message = error.statusText;
            //         }
            //         messages.error('Ha ocurrido un error al obtener el usuario loggeado: ' + message);
            //     });
        }

        getLoggedUser();

        $scope.$on("$routeChangeSuccess", function (e, current, previous) {
            $rootScope.title = 'Blog SOA';
            $rootScope.subtitle = 'Un blog inspirado en los principios solid de la POO';
        });
    }

})();
