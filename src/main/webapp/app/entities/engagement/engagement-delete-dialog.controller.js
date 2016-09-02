(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementDeleteController',EngagementDeleteController);

    EngagementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Engagement'];

    function EngagementDeleteController($uibModalInstance, entity, Engagement) {
        var vm = this;

        vm.engagement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Engagement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
