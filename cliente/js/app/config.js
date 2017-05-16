(function () {
    'use strict';

    angular
        .module('SOApp')
        .config(config);

    config.$inject = ['$interpolateProvider', '$locationProvider', '$provide', '$httpProvider', '$routeProvider'];

    /**
     * @name config
     * @desc Define valid application routes and some configuration stuffs
     */
    function config($interpolateProvider, $locationProvider, $provide, $httpProvider, $routeProvider) {
        httpInterceptor.$inject = ['$q', '$rootScope'];

        function httpInterceptor($q, $rootScope) {
            return {
                'request': function (config) {
                    //Aca se muestra el diálogo de cargando. Se ignoran los casos en que lee un template o una imagen svg
                    if (config.url.indexOf('.svg') === -1 && config.url.indexOf('.html') === -1 && config.url.indexOf('/getStatusDescription/') === -1) {
                        $rootScope.isLoading = true;
                    }
                    config.headers['X-Requested-With'] = 'XMLHttpRequest';

                    $rootScope.$broadcast('httpRequest', config);
                    return config || $q.when(config);
                },
                'response': function (response) {
                    //Aca se oculta el diálogo
                    if (response.config.url.indexOf('.svg') === -1 && response.config.url.indexOf('.html') === -1) {
                        $rootScope.isLoading = false;
                    }

                    $rootScope.$broadcast('httpResponse', response);
                    return response || $q.when(response);
                },
                'requestError': function (rejection) {
                    $rootScope.isLoading = false;

                    $rootScope.$broadcast('httpRequestError', rejection);
                    return $q.reject(rejection);
                },
                'responseError': function (rejection) {
                    $rootScope.isLoading = false;

                    if (rejection.status == 901) {
                        window.location.reload(true);
                        return;
                    }

                    $rootScope.$broadcast('httpResponseError', rejection);
                    return $q.reject(rejection);
                }
            };
        }

        $provide.factory('httpInterceptor', httpInterceptor);

        $httpProvider.interceptors.push('httpInterceptor');

        $routeProvider.when('/', {
            templateUrl: 'js/app/components/index/index.html',
            controller: 'IndexController',
            controllerAs: 'vm'
        }).when('/login', {
            templateUrl: 'js/app/components/login/login.html',
            controller: 'LoginController',
            controllerAs: 'vm'
        }).when('/register', {
            templateUrl: 'js/app/components/register/register.html',
            controller: 'RegisterController',
            controllerAs: 'vm'
        }).when('/users/', {
            templateUrl: 'js/app/components/users/list.html',
            controller: 'UsersListController',
            controllerAs: 'vm'
        }).when('/posts', {
            templateUrl: 'js/app/components/index/index.html',
            controller: 'IndexController',
            controllerAs: 'vm'
        }).when('/posts/create', {
            templateUrl: 'js/app/components/post/create.html',
            controller: 'PostCreateController',
            controllerAs: 'vm'
        }).when('/post/edit/:id', {
            templateUrl: 'js/app/components/post/index.html',
            controller: 'PostIndexController',
            controllerAs: 'vm'
        }).when('/posts/:userId', {
            templateUrl: 'js/app/components/post/list.html',
            controller: 'PostListController',
            controllerAs: 'vm'
        }).when('/post/:id', {
            templateUrl: 'js/app/components/post/index.html',
            controller: 'PostIndexController',
            controllerAs: 'vm'
        }).otherwise({
            redirectTo: '/'
        });

        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });

        // $locationProvider.html5Mode(false);
        // $locationProvider.hashPrefix("!");
    }

})();
