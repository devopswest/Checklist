(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('UserProfileDeleteController',UserProfileDeleteController);

    UserProfileDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserProfile'];

    function UserProfileDeleteController($uibModalInstance, entity, UserProfile) {
        var vm = this;

        vm.userProfile = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserProfile.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
