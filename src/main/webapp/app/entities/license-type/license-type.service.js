(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('LicenseType', LicenseType);

    LicenseType.$inject = ['$resource'];

    function LicenseType ($resource) {
        var resourceUrl =  'api/license-types/:id';

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
