(function () {
    'use strict';

    angular
        .module('SOApp')
        .controller('PostCreateController', PostCreateController);

    PostCreateController.$inject = ['$scope', '$rootScope', '$http', '$routeParams', '$location', 'messages'];

    /**
     * @namespace PostCreateController
     */
    function PostCreateController($scope, $rootScope, $http, $routeParams, $location, messages) {
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

        vm.froalaOptions = {
            toolbarInline: false,
            placeholderText: 'Escriba el contenido',
            fontFamilySelection: true
        };

        function submit() {
            console.log({
                title: vm.title,
                subtitle: vm.subtitle,
                content: vm.content,
                id: $rootScope.loggedUser.id,
                name: $rootScope.loggedUser.name,
                email: $rootScope.loggedUser.email
            });
            $http.post($rootScope.servicesUrl + 'posts', {
                title: vm.title,
                subtitle: vm.subtitle,
                content: vm.content,
                id: $rootScope.loggedUser.id,
                name: $rootScope.loggedUser.name,
                email: $rootScope.loggedUser.email
            })
                .then(function (response) {
                    console.log('Create post:');
                    console.log(response);
                    $location.path('/');
                    messages.success('El post ' + vm.title + ' ha sido creado exitosamente.');
                }).catch(function (error) {
                    console.error(error);
                    var message = '';
                    var data = error.data;
                    if (data && data.msg) {
                        message = data.msg;
                    } else if (error.statusText) {
                        message = error.statusText;
                    }
                    messages.error('Ha ocurrido un error al crear el post: ' + message);
                });
        }
    }

})();
