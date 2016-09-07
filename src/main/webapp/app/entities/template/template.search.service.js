(function() {
    'use strict';

    angular
        .module('checklistApp')
        .factory('TemplateSearch', TemplateSearch);

    TemplateSearch.$inject = ['$resource'];

    function TemplateSearch($resource) {
        var resourceUrl =  'api/_search/templates/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
