(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistHistoryChangesDeleteController',ChecklistHistoryChangesDeleteController);

    ChecklistHistoryChangesDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChecklistHistoryChanges'];

    function ChecklistHistoryChangesDeleteController($uibModalInstance, entity, ChecklistHistoryChanges) {
        var vm = this;

        vm.checklistHistoryChanges = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChecklistHistoryChanges.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
