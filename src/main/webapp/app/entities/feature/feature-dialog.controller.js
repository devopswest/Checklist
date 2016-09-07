(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('FeatureDialogController', FeatureDialogController);

    FeatureDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Feature', 'FeatureAuthority'];

    function FeatureDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Feature, FeatureAuthority) {
        var vm = this;

        vm.feature = entity;
        vm.clear = clear;
        vm.save = save;
        vm.features = Feature.query();
        vm.featureauthorities = FeatureAuthority.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.feature.id !== null) {
                Feature.update(vm.feature, onSaveSuccess, onSaveError);
            } else {
                Feature.save(vm.feature, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:featureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
