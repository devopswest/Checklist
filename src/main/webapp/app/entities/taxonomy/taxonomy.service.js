(function() {
    'use strict';
    angular
        .module('checklistApp')
        .factory('Taxonomy', Taxonomy);

    Taxonomy.$inject = ['$resource'];

    function Taxonomy ($resource) {
        var resourceUrl =  'api/taxonomies/:id';

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
