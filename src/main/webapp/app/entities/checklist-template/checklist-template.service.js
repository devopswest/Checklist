(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ChecklistTemplate', ChecklistTemplate);

    ChecklistTemplate.$inject = ['$resource'];

    function ChecklistTemplate ($resource) {
        var resourceUrl =  'api/checklist-templates/:id';

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
