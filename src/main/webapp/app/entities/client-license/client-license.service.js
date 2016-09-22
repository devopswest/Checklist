(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('ClientLicense', ClientLicense);

    ClientLicense.$inject = ['$resource', 'DateUtils'];

    function ClientLicense ($resource, DateUtils) {
        var resourceUrl =  'api/client-licenses/:id';

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
