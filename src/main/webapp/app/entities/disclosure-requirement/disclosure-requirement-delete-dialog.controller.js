(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('DisclosureRequirementDeleteController',DisclosureRequirementDeleteController);

    DisclosureRequirementDeleteController.$inject = ['$uibModalInstance', 'entity', 'DisclosureRequirement'];

    function DisclosureRequirementDeleteController($uibModalInstance, entity, DisclosureRequirement) {
        var vm = this;

        vm.disclosureRequirement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DisclosureRequirement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
