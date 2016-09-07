(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('Template', Template);

    Template.$inject = ['$resource'];

    function Template ($resource) {
        var resourceUrl =  'api/templates/:id';

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
