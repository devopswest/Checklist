(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ClientTag', ClientTag);

    ClientTag.$inject = ['$resource'];

    function ClientTag ($resource) {
        var resourceUrl =  'api/client-tags/:id';

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
