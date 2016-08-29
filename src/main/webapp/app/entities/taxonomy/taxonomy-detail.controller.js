(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('TaxonomyDetailController', TaxonomyDetailController);

    TaxonomyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Taxonomy', 'License'];

    function TaxonomyDetailController($scope, $rootScope, $stateParams, previousState, entity, Taxonomy, License) {
        var vm = this;

        vm.taxonomy = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:taxonomyUpdate', function(event, result) {
            vm.taxonomy = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
