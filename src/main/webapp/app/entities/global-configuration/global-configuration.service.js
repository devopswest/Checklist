(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('GlobalConfiguration', GlobalConfiguration);

    GlobalConfiguration.$inject = ['$resource'];

    function GlobalConfiguration ($resource) {
        var resourceUrl =  'api/global-configurations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
