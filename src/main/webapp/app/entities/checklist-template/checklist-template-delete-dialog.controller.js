(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistTemplateDeleteController',ChecklistTemplateDeleteController);

    ChecklistTemplateDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChecklistTemplate'];

    function ChecklistTemplateDeleteController($uibModalInstance, entity, ChecklistTemplate) {
        var vm = this;

        vm.checklistTemplate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChecklistTemplate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
