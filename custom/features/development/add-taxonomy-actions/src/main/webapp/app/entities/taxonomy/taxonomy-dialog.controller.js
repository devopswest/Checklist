(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('TaxonomyDialogController', TaxonomyDialogController);

    TaxonomyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Taxonomy'];

    function TaxonomyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Taxonomy) {
        var vm = this;

        vm.taxonomy = entity;
        vm.clear = clear;
        vm.save = save;
        vm.taxonomies = Taxonomy.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.taxonomy.id !== null) {
                Taxonomy.update(vm.taxonomy, onSaveSuccess, onSaveError);
            } else {
                vm.taxonomy.taxonomies = vm.treedata;
                Taxonomy.save(vm.taxonomy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('taxonomyApp:taxonomyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        //taxonomyQuestion.query is incorrect - instead ask taxonomy to get child data
        //It retrieves all rows of taxonomyQuestion table and then creates hierarchy tree
        //So, for example if 2 is child for 1. then using this query it would be pulled twice
        //One time as a child for 1, and second time as next record in the table
        //So instead of this rely on the taxonomy and ask it get all of its child
        //vm.taxonomyquestions = taxonomyQuestion.query();
        vm.treedata = [{
        'id': 1,
        'label': 'node1',
        'children': [
          {
            'id': 11,
            'label': 'node1.1',
            'children': [
              {
                'id': 111,
                'label': 'node1.1.1',
                'children': []
              }
            ]
          },
          {
            'id': 12,
            'label': 'node1.2',
            'children': []
          }
        ]
      }, {
        'id': 2,
        'label': 'node2',
        'nodrop': true, // An arbitrary property to check in custom template for nodrop-enabled
        'children': [
          {
            'id': 21,
            'label': 'node2.1',
            'children': []
          },
          {
            'id': 22,
            'label': 'node2.2',
            'children': []
          }
        ]
      }, {
        'id': 3,
        'label': 'node3',
        'children': [
          {
            'id': 31,
            'label': 'node3.1',
            'children': []
          }
        ]
      }];





        vm.maxid = 0;
        vm.taxonomyId = 1;
        vm.taxonomyName = "";
        vm.taxonomy.$promise.then(function (result) {
            //vm.treedata = result.taxonomies;
            //setTaxonomyIdAndName(vm.treedata);
            //getMaxId(vm.treedata);
            collapseAll();
        });

        /**
         * Set taxonomyId and taxonomyName which can be used when addQuestion is clicked
         */
        function setTaxonomyIdAndName(node){
            for(var l=0;l<node.length;l++){
                vm.taxonomyId = node[l].taxonomyId;
                vm.taxonomyName = node[l].taxonomyName;
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
                "taxonomyId":null,
                "taxonomyName":null,
                "children":[],
                "code":nodeData.code + "|"  + "xx",
                "label": "<Please add description>",
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
vm.addNode=addNode;
    function addNode () {
        var id = getNextId();
        var newQuestion = {
                "taxonomyId":vm.taxonomyId,
                "taxonomyName":vm.taxonomyName,
                "children":[],
                "code":id,
                "label": "<Please add description>",
                "id": id,
                "parentDescription":null,
                "parentId":null
        };
        vm.treedata.push(newQuestion);
    }

    //collapseAll();

    }
})();
