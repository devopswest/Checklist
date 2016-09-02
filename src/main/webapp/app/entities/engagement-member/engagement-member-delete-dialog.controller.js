(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('EngagementMemberDeleteController',EngagementMemberDeleteController);

    EngagementMemberDeleteController.$inject = ['$uibModalInstance', 'entity', 'EngagementMember'];

    function EngagementMemberDeleteController($uibModalInstance, entity, EngagementMember) {
        var vm = this;

        vm.engagementMember = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EngagementMember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
