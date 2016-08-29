(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('TaxonomyDialogController', TaxonomyDialogController);

    TaxonomyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Taxonomy', 'License'];

    function TaxonomyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Taxonomy, License) {
        var vm = this;

        vm.taxonomy = entity;
        vm.clear = clear;
        vm.save = save;
        vm.taxonomies = Taxonomy.query();
        vm.licenses = License.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.taxonomy.id !== null) {
                Taxonomy.update(vm.taxonomy, onSaveSuccess, onSaveError);
            } else {
                Taxonomy.save(vm.taxonomy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:taxonomyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
