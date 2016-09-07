(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('License', License);

    License.$inject = ['$resource', 'DateUtils'];

    function License ($resource, DateUtils) {
        var resourceUrl =  'api/licenses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.expirationDate = DateUtils.convertDateTimeFromServer(data.expirationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
