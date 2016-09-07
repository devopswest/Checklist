(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('FeatureAuthority', FeatureAuthority);

    FeatureAuthority.$inject = ['$resource'];

    function FeatureAuthority ($resource) {
        var resourceUrl =  'api/feature-authorities/:id';

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
