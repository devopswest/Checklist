(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('EngagementMember', EngagementMember);

    EngagementMember.$inject = ['$resource'];

    function EngagementMember ($resource) {
        var resourceUrl =  'api/engagement-members/:id';

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
