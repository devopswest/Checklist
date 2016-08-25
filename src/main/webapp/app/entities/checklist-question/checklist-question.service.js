(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ChecklistQuestion', ChecklistQuestion);

    ChecklistQuestion.$inject = ['$resource'];

    function ChecklistQuestion ($resource) {
        var resourceUrl =  'api/checklist-questions/:id';

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
