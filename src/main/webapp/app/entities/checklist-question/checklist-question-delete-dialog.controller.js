(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistQuestionDeleteController',ChecklistQuestionDeleteController);

    ChecklistQuestionDeleteController.$inject = ['$uibModalInstance', 'entity', 'ChecklistQuestion'];

    function ChecklistQuestionDeleteController($uibModalInstance, entity, ChecklistQuestion) {
        var vm = this;

        vm.checklistQuestion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ChecklistQuestion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
