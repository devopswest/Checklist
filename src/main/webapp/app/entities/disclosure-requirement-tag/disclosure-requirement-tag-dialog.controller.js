(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('DisclosureRequirementTagDialogController', DisclosureRequirementTagDialogController);

    DisclosureRequirementTagDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DisclosureRequirementTag', 'Taxonomy', 'DisclosureRequirement'];

    function DisclosureRequirementTagDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DisclosureRequirementTag, Taxonomy, DisclosureRequirement) {
        var vm = this;

        vm.disclosureRequirementTag = entity;
        vm.clear = clear;
        vm.save = save;
        vm.taxonomies = Taxonomy.query();
        vm.disclosurerequirements = DisclosureRequirement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.disclosureRequirementTag.id !== null) {
                DisclosureRequirementTag.update(vm.disclosureRequirementTag, onSaveSuccess, onSaveError);
            } else {
                DisclosureRequirementTag.save(vm.disclosureRequirementTag, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:disclosureRequirementTagUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
