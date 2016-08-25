(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('UserProfile', UserProfile);

    UserProfile.$inject = ['$resource'];

    function UserProfile ($resource) {
        var resourceUrl =  'api/user-profiles/:id';

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
