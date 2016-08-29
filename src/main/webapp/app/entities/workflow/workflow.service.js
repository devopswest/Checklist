(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('Workflow', Workflow);

    Workflow.$inject = ['$resource'];

    function Workflow ($resource) {
        var resourceUrl =  'api/workflows/:id';

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
