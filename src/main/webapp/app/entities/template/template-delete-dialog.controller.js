(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('TemplateDeleteController',TemplateDeleteController);

    TemplateDeleteController.$inject = ['$uibModalInstance', 'entity', 'Template'];

    function TemplateDeleteController($uibModalInstance, entity, Template) {
        var vm = this;

        vm.template = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Template.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
