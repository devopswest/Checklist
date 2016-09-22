(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('DisclosureRequirement', DisclosureRequirement);

    DisclosureRequirement.$inject = ['$resource'];

    function DisclosureRequirement ($resource) {
        var resourceUrl =  'api/disclosure-requirements/:id';

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
