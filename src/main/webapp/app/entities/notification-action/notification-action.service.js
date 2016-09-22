(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('NotificationAction', NotificationAction);

    NotificationAction.$inject = ['$resource'];

    function NotificationAction ($resource) {
        var resourceUrl =  'api/notification-actions/:id';

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
