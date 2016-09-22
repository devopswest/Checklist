(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('DisclosureRequirementTagDeleteController',DisclosureRequirementTagDeleteController);

    DisclosureRequirementTagDeleteController.$inject = ['$uibModalInstance', 'entity', 'DisclosureRequirementTag'];

    function DisclosureRequirementTagDeleteController($uibModalInstance, entity, DisclosureRequirementTag) {
        var vm = this;

        vm.disclosureRequirementTag = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DisclosureRequirementTag.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
