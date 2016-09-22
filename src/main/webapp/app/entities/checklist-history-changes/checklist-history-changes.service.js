(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ChecklistHistoryChanges', ChecklistHistoryChanges);

    ChecklistHistoryChanges.$inject = ['$resource', 'DateUtils'];

    function ChecklistHistoryChanges ($resource, DateUtils) {
        var resourceUrl =  'api/checklist-history-changes/:id';

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
