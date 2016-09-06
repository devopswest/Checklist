(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('AuditProfileDialogController', AuditProfileDialogController);

    AuditProfileDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AuditProfile', 'AuditProfileLogEntry', 'AuditQuestionResponse', 'Engagement', 'Checklist','ChecklistQuestion', '$interval', 'uiGridTreeViewConstants'];

    function AuditProfileDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AuditProfile, AuditProfileLogEntry, AuditQuestionResponse, Engagement, Checklist, ChecklistQuestion, $interval, uiGridTreeViewConstants) {
        var vm = this;

        vm.auditProfile = entity;
        vm.clear = clear;
        vm.save = save;
        vm.auditprofilelogentries = AuditProfileLogEntry.query();
        vm.auditquestionresponses = AuditQuestionResponse.query();
        vm.engagements = Engagement.query();
        vm.checklists = Checklist.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auditProfile.id !== null) {
                AuditProfile.update(vm.auditProfile, onSaveSuccess, onSaveError);
            } else {
                AuditProfile.save(vm.auditProfile, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:auditProfileUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }



// Grid

vm.treedata = {
    enableSorting: false,
    enableFiltering: false,
    enableCellEditOnFocus: true,

    columnDefs: [
      { name: 'id',   width: '3%',  enableCellEdit: false, displayName: '' },
      { name: 'title',width: '60%', enableCellEdit: false, displayName: '' },
      { name: 'na',   width: '10%', enableCellEdit: true, type: 'boolean' },
      { name: 'yes',  width: '10%', enableCellEdit: true, type: 'boolean' },
      { name: 'no',   width: '10%', enableCellEdit: true, type: 'boolean' }
    ],
    onRegisterApi: function( gridApi ) {
      vm.gridApi = gridApi;
      // vm.gridApi.treeBase.on.rowExpanded($scope, function(row) {
      //   if( row.entity.$$hashKey === $scope.gridOptions.data[50].$$hashKey && !$scope.nodeLoaded ) {
      //     $interval(function() {
      //       $scope.gridOptions.data.splice(51,0,
      //         {name: 'Dynamic 1', gender: 'female', age: 53, company: 'Griddable grids', balance: 38000, $$treeLevel: 1},
      //         {name: 'Dynamic 2', gender: 'male', age: 18, company: 'Griddable grids', balance: 29000, $$treeLevel: 1}
      //       );
      //       $scope.nodeLoaded = true;
      //     }, 2000, 1);
      //   }
      // });
    }
  };



// Pull Dta
         vm.checklistquestions = ChecklistQuestion.query();

      vm.checklistquestions.$promise.then(function (result) {

        //vm.treedataTree = transformToTree(result);
        vm.treedata.data = transformToGrid(result);

        //collapseAll();
      });


 function addToGrid(data, rows, level) {
    for(var l=0;l<rows.length;l++){

       var row = {
                        "id": rows[l].id,
                        "title": rows[l].code + ":" + rows[l].description,
                        "description": rows[l].description,
                        "yes": false,
                        "no": false,
                        "na": false
         };


        data.push(row);
        data[data.length-1].$$treeLevel = level;


        if ( rows[l].children.length>0) {
           addToGrid(data, rows[l].children, level++);
        }
    }
 }


 function transformToGrid(result) {

   var data=[];
   addToGrid(data, result, 0);
   return data;
 }

  vm.expandAll = function(){
    vm.gridApi.treeBase.expandAllRows();
  };

    vm.collapseAll = function(){
    vm.gridApi.treeBase.collapseAllRows();
  };


  vm.toggleGridRow = function( rowNum ){
   vm.gridApi.treeBase.toggleRowTreeState(vm.gridApi.grid.renderContainers.body.visibleRowCache[rowNum]);
  };



//

    }
})();
