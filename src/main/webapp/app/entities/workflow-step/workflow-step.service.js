(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('WorkflowStep', WorkflowStep);

    WorkflowStep.$inject = ['$resource'];

    function WorkflowStep ($resource) {
        var resourceUrl =  'api/workflow-steps/:id';

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
