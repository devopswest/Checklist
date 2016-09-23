(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('EngagementChecklistTemplate', EngagementChecklistTemplate);

    EngagementChecklistTemplate.$inject = ['$resource'];

    function EngagementChecklistTemplate ($resource) {
        var resourceUrl =  'api/engagement-checklist-templates/:id';

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