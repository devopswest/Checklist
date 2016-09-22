(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementChecklistTemplateDeleteController',EngagementChecklistTemplateDeleteController);

    EngagementChecklistTemplateDeleteController.$inject = ['$uibModalInstance', 'entity', 'EngagementChecklistTemplate'];

    function EngagementChecklistTemplateDeleteController($uibModalInstance, entity, EngagementChecklistTemplate) {
        var vm = this;

        vm.engagementChecklistTemplate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EngagementChecklistTemplate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
