(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('DisclosureRequirementTag', DisclosureRequirementTag);

    DisclosureRequirementTag.$inject = ['$resource'];

    function DisclosureRequirementTag ($resource) {
        var resourceUrl =  'api/disclosure-requirement-tags/:id';

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
