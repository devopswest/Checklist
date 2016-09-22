(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('NotificationActionSearch', NotificationActionSearch);

    NotificationActionSearch.$inject = ['$resource'];

    function NotificationActionSearch($resource) {
        var resourceUrl =  'api/_search/notification-actions/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
