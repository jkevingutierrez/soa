(function () {
    'use strict';

    angular.module('SOApp', ['ngRoute', 'ngSanitize', 'ngAlertify', 'oitozero.ngSweetAlert', 'froala']);

    angular.element(document).ready(function () {
        angular.bootstrap(document.body, ['SOApp']);
    });

})();
