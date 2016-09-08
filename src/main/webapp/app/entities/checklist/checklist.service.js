(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('Checklist', Checklist);

    Checklist.$inject = ['$resource'];

    function Checklist ($resource) {
        var resourceUrl =  'api/checklists/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'loadQuestions': { method: 'GET', isArray: false},
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
