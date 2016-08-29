(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('TaxonomyDeleteController',TaxonomyDeleteController);

    TaxonomyDeleteController.$inject = ['$uibModalInstance', 'entity', 'Taxonomy'];

    function TaxonomyDeleteController($uibModalInstance, entity, Taxonomy) {
        var vm = this;

        vm.taxonomy = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Taxonomy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
