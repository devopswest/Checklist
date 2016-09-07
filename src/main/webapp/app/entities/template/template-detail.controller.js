(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('TemplateDetailController', TemplateDetailController);

    TemplateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Template'];

    function TemplateDetailController($scope, $rootScope, $stateParams, previousState, entity, Template) {
        var vm = this;

        vm.template = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('checklistApp:templateUpdate', function(event, result) {
            vm.template = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
