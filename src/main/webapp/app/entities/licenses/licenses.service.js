(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('Licenses', Licenses);

    Licenses.$inject = ['$resource'];

    function Licenses ($resource) {
        var resourceUrl =  'api/licenses/:id';

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
