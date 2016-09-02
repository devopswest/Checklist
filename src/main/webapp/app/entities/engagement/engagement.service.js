(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('Engagement', Engagement);

    Engagement.$inject = ['$resource'];

    function Engagement ($resource) {
        var resourceUrl =  'api/engagements/:id';

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
