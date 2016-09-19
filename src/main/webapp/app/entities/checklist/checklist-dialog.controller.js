(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ChecklistDialogController', ChecklistDialogController)
        .directive('adcMetadata', function () {
        	return {
        		restrict: 'E',
        		scope: {
        			metadata: '=treeData',
        			toggle: '&onToggle'
        		},
        		templateUrl: 'app/components/custom-directives/metadata-tree.html'
        	};
        });

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
vm.metaDataEditorEnabled = false;
vm.metaDataTreeEditorEnabled = false;

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

vm.openMetaDataEditor = openMetaDataEditor;
function openMetaDataEditor (scope, node) {
	vm.current = node
	vm.content = node.description;
	vm.metaDataEditorEnabled = true;
};

vm.metaDataEditorClear = metaDataEditorClear;
function metaDataEditorClear() {
	vm.metaDataEditorEnabled = false;
}

vm.addMetaData = addMetaData;
function addMetaData (metadata) {
	var existing_flag = false;
	var max_id = 0;
	var new_option = {"value": 0, "name": ""};
	if (metadata) {
		if (vm.metaDataOptions) {
			for (i = 0; i < vm.metaDataOptions.length; i++) {
				var option = vm.metaDataOptions[i];
				if (metadata == option.name) {
					existing_flag = true;
				}
				if (max_id < option.value) {
					max_id = option.value;
				}
			}
			if (!existing_flag) {
				new_option.name = metadata;
				new_option.value = max_id + 1;
				vm.metaDataOptions.splice(0,0,new_option);
				vm.metaDataText = '';
			}
		}
	}
}

vm.deleteMetaData = deleteMetaData;
function deleteMetaData (id_list) {
	if (id_list) {
		for (i = 0; i < id_list.length; i++) {
			var option_id = id_list[i];
			console.log('Id: ' + option_id);
			if (vm.metaDataOptions) {
				for (var j = 0; j < vm.metaDataOptions.length; j++) {
					var option = vm.metaDataOptions[j];
					if (option_id == option.value) {
						console.log('Object id: ' + option.value);
						vm.metaDataOptions.splice(j,1);
					} 
				}
			}
			
		}
	}
}

vm.metaDataOptions = 
	[
	 	{
	 		"value": 1,
	 		"name": "SEC 10-K"
	 	},
	 	{
	 		"value": 2,
	 		"name": "Registered Investment Company"
	 	},
	 	{
	 		"value": 3,
			"name": "Insurance Company"
	 	},
	 	{
	 		"value": 4,
			"name": "RIC Reg Statements"
	 	},
	 	{
	 		"value": 5,
			"name": "Insurance"
	 	},
	 	{
	 		"value": 6,
			"name": "REIT"
	 	}
	 ]

vm.metaDataText = '';

vm.openMetaDataTreeEditor = openMetaDataTreeEditor;
function openMetaDataTreeEditor (scope, node) {
	vm.current = node
	vm.content = node.description;
	vm.metaDataTreeEditorEnabled = true;
};

vm.metaDataTreeEditorClear = metaDataTreeEditorClear;
function metaDataTreeEditorClear() {
	vm.metaDataTreeEditorEnabled = false;
}

///

vm.treeMetaData = 
	[
	 	{
	 		"selected": true,
	 		"tagName": "Public",
	 		"tagCode": "1",
	 		"children": 
	 			[
	 			 	{
	 			 		"selected": true,
	 			 		"tagName": "SEC 10-K",
	 			 		"tagCode": "1A",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": true,
	 			 		"tagName": "SEC 10-Q",
	 			 		"tagCode": "1B",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": true,
	 			 		"tagName": "Small Reporting Company",
	 			 		"tagCode": "1C",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": true,
	 			 		"tagName": "Registerd Investment Company",
	 			 		"tagCode": "1D",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": true,
	 			 		"tagName": "Insurance Company",
	 			 		"tagCode": "1E",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": true,
	 			 		"tagName": "RIC Reg Statements",
	 			 		"tagCode": "1F",
	 			 		"children": []
	 			 	}
	 			 
	 			 ]
	 	
	 	},
	 	{
	 		"selected": false,
	 		"tagName": "Non-Public",
	 		"tagCode": "2",
	 		"children": 
	 			[
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Non-public main",
	 			 		"tagCode": "2A",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Non-public investment company",
	 			 		"tagCode": "2B",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "State and Local Government",
	 			 		"tagCode": "2C",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Government Agencies",
	 			 		"tagCode": "2D",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Federal Acct",
	 			 		"tagCode": "2E",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Insurance Company-Stat",
	 			 		"tagCode": "2F",
	 			 		"children": []
	 			 	}
	 			 ]
	 	},
	 	{
	 		"selected": false,
	 		"tagName": "EBP",
	 		"tagCode": "3",
	 		"children": 
	 			[
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Defined Benefit",
	 			 		"tagCode": "3A",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Defined Compensation",
	 			 		"tagCode": "3B",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Health and Welfare",
	 			 		"tagCode": "3C",
	 			 		"children": []
	 			 	}
	 			 ]
	 	},
	 	{
	 		"selected": false,
	 		"tagName": "Supplements",
	 		"tagCode": "4",
	 		"children": 
	 			[
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Savings and Loan (Article 9)",
	 			 		"tagCode": "4A",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Savings and Loan (Guide 3)",
	 			 		"tagCode": "4B",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Insurance",
	 			 		"tagCode": "4C",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Mortgage Banking",
	 			 		"tagCode": "4D",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Oil and Gas",
	 			 		"tagCode": "4E",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Public Utility",
	 			 		"tagCode": "4F",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "REIT",
	 			 		"tagCode": "4G",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Credit Union",
	 			 		"tagCode": "4H",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Not for Profit",
	 			 		"tagCode": "4I",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Not for Profit Healthcare",
	 			 		"tagCode": "4J",
	 			 		"children": []
	 			 	},
	 			 	{
	 			 		"selected": false,
	 			 		"tagName": "Yellowbook",
	 			 		"tagCode": "4K",
	 			 		"children": []
	 			 	}
	 			 ]
	 	} 
	 ]

    }
})();
