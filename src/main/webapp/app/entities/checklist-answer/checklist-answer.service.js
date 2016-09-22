(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ChecklistAnswer', ChecklistAnswer);

    ChecklistAnswer.$inject = ['$resource'];

    function ChecklistAnswer ($resource) {
        var resourceUrl =  'api/checklist-answers/:id';

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
