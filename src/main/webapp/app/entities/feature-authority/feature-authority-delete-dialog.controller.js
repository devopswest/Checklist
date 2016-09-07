(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('FeatureAuthorityDeleteController',FeatureAuthorityDeleteController);

    FeatureAuthorityDeleteController.$inject = ['$uibModalInstance', 'entity', 'FeatureAuthority'];

    function FeatureAuthorityDeleteController($uibModalInstance, entity, FeatureAuthority) {
        var vm = this;

        vm.featureAuthority = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FeatureAuthority.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
