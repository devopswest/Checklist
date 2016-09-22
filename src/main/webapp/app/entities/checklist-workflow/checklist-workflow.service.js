(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ChecklistWorkflow', ChecklistWorkflow);

    ChecklistWorkflow.$inject = ['$resource', 'DateUtils'];

    function ChecklistWorkflow ($resource, DateUtils) {
        var resourceUrl =  'api/checklist-workflows/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.happened = DateUtils.convertDateTimeFromServer(data.happened);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
