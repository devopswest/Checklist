(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistAnswerDeleteController',ChecklistAnswerDeleteController);

    ChecklistAnswerDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChecklistAnswer'];

    function ChecklistAnswerDeleteController($uibModalInstance, entity, ChecklistAnswer) {
        var vm = this;

        vm.checklistAnswer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChecklistAnswer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
