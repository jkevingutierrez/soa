(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('IndexController', IndexController);

    IndexController.$inject = ['$scope', '$rootScope', '$http', 'messages'];

    /**
     * @namespace IndexController
     */
    function IndexController($scope, $rootScope, $http, messages) {
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

        function getPosts() {
            $http.get($rootScope.servicesUrl + 'posts')
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
        }

        getPosts();
    }

})();
