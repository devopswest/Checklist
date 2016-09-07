(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('FeatureAuthorityDialogController', FeatureAuthorityDialogController);

    FeatureAuthorityDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FeatureAuthority', 'Feature'];

    function FeatureAuthorityDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FeatureAuthority, Feature) {
        var vm = this;

        vm.featureAuthority = entity;
        vm.clear = clear;
        vm.save = save;
        vm.features = Feature.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.featureAuthority.id !== null) {
                FeatureAuthority.update(vm.featureAuthority, onSaveSuccess, onSaveError);
            } else {
                FeatureAuthority.save(vm.featureAuthority, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:featureAuthorityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
