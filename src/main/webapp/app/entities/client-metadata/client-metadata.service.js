(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ClientMetadata', ClientMetadata);

    ClientMetadata.$inject = ['$resource'];

    function ClientMetadata ($resource) {
        var resourceUrl =  'api/client-metadata/:id';

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
