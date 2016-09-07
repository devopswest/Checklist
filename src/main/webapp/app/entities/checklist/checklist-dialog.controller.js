(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistDialogController', ChecklistDialogController);

    ChecklistDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Checklist', 'ChecklistQuestion', 'Taxonomy'];

    function ChecklistDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Checklist, ChecklistQuestion, Taxonomy) {
        var vm = this;

        vm.checklist = entity;
        vm.clear = clear;
        vm.save = save;
        vm.checklistquestions = ChecklistQuestion.query();
        vm.taxonomies = Taxonomy.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checklist.id !== null) {
                Checklist.update(vm.checklist, onSaveSuccess, onSaveError);
            } else {
                vm.checklist.checklistQuestions = vm.treedata;
                Checklist.save(vm.checklist, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:checklistUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }



        //ChecklistQuestion.query is incorrect - instead ask Checklist to get child data
        //It retrieves all rows of ChecklistQuestion table and then creates hierarchy tree
        //So, for example if 2 is child for 1. then using this query it would be pulled twice
        //One time as a child for 1, and second time as next record in the table
        //So instead of this rely on the checklist and ask it get all of its child
        //vm.checklistquestions = ChecklistQuestion.query();
        vm.treedata = [];
        vm.maxid = 0;
        vm.checklistId = 1;
        vm.checklistName = "";
        vm.checklist.$promise.then(function (result) {
            vm.treedata = result.checklistQuestions;
            setChecklistIdAndName(vm.treedata);
            getMaxId(vm.treedata);
            collapseAll();
        });

        /**
         * Set checklistId and checklistName which can be used when addQuestion is clicked
         */
        function setChecklistIdAndName(node){
            for(var l=0;l<node.length;l++){
                vm.checklistId = node[l].checklistId;
                vm.checklistName = node[l].checklistName;
            }
        }
        /**
         * Recursively traverses all nodes and children and find the maxid, which can be used if new nodes are added
         */
        function getMaxId(node){
            for(var l=0;l<node.length;l++){
                //Update maxid if current id is less than node.id
                if(node[l].id > vm.maxid){
                    vm.maxid = node[l].id;
                }

                //Recursively check all the children too
                if(node[l].children.length > 0){
                    getMaxId(node[l].children);
                }
            }
        }

        function getNextId(){
            vm.maxid = vm.maxid + 1;
            return vm.maxid
        }

vm.remove=remove;
function remove (scope) {
        scope.remove();
      };

vm.toggle=toggle;
      function toggle (scope) {
        scope.toggle();
      };
vm.moveLastToTheBeginning=moveLastToTheBeginning;
      function moveLastToTheBeginning () {
        var a = $scope.data.pop();
        $scope.data.splice(0, 0, a);
      };
vm.newSubItem=newSubItem;
      function newSubItem (scope) {
        var id = getNextId();
        var nodeData = scope.$modelValue;
        var newQuestionSub = {
                "checklistId":null,
                "checklistName":null,
                "children":[],
                "code":nodeData.code + "|"  + "xx",
                "description": "<Please add description>",
                "id": id,
                "parentDescription":nodeData.description,
                "parentId":nodeData.id
        };
        nodeData.children.push(newQuestionSub);

        scope.collapsed = false;
      };
vm.collapseAll=collapseAll;
      function collapseAll () {
        $scope.$broadcast('angular-ui-tree:collapse-all');
      };
vm.expandAll=expandAll;
     function expandAll () {
        $scope.$broadcast('angular-ui-tree:expand-all');
      };
vm.addQuestion=addQuestion;
    function addQuestion () {
        var id = getNextId();
        var newQuestion = {
                "checklistId":vm.checklistId,
                "checklistName":vm.checklistName,
                "children":[],
                "code":id,
                "description": "<Please add description>",
                "id": id,
                "parentDescription":null,
                "parentId":null
        };
        vm.treedata.push(newQuestion);
    }

    //collapseAll();


//Editor
vm.editorOptions = {
    // settings more at http://docs.ckeditor.com/#!/guide/dev_configuration
};

vm.current = null;
vm.editorTitle = "";
vm.editorEnabled = false;

vm.openEditor=openEditor;
      function openEditor (scope, node) {

        //scope.$broadcast('ckeditor-visible');
        vm.current=node;
        vm.content=node.description;
        vm.editorEnabled=true;
        vm.editorTitle="Editing [" + node.code + "]";

      };
vm.editorClear=editorClear;
function editorClear() {
  vm.editorEnabled=false;
}


vm.editorSave=editorSave;
function editorSave(scope, node) {
  vm.editorEnabled=false;
  vm.current.description=vm.content;
}



///


    }
})();
