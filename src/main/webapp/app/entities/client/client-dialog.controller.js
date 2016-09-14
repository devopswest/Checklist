(function() {
    'use strict';

    angular
        .module('checklistApp')
        .controller('ClientDialogController', ClientDialogController);

    ClientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Client'];

    function ClientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Client) {
        var vm = this;

        vm.client = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.client.id !== null) {
                Client.update(vm.client, onSaveSuccess, onSaveError);
            } else {
                Client.save(vm.client, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('checklistApp:clientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

///NEW
vm.children = [
  {
    "id": 1,
    "title1": "LOS",
    "title2": "Assurance"
  },
  {
    "id": 2,
    "title1": "Company Type",
    "title2": "Public Sector"
  },
  {
    "id": 3,
    "title1": "Company Size",
    "title2": "500"
  }
];


vm.newItem = function () {
        var nodeData = vm.children[vm.children.length - 1];
        vm.children.push({
          id: vm.children.length + 1,
          title1: 'node ' + (vm.children.length + 1),
          title2: 'node ' + (vm.children.length + 1)
        });
      };


vm.removeItem = function (scope) {
        scope.remove();
      };
      
vm.toggle=toggle;
function toggle (scope) {
	scope.toggle();
};

///NEW

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
      	 			 		"selected": false,
      	 			 		"tagName": "SEC 10-Q",
      	 			 		"tagCode": "1B",
      	 			 		"children": []
      	 			 	},
      	 			 	{
      	 			 		"selected": false,
      	 			 		"tagName": "Small Reporting Company",
      	 			 		"tagCode": "1C",
      	 			 		"children": []
      	 			 	},
      	 			 	{
      	 			 		"selected": false,
      	 			 		"tagName": "Registerd Investment Company",
      	 			 		"tagCode": "1D",
      	 			 		"children": []
      	 			 	},
      	 			 	{
      	 			 		"selected": false,
      	 			 		"tagName": "Insurance Company",
      	 			 		"tagCode": "1E",
      	 			 		"children": []
      	 			 	},
      	 			 	{
      	 			 		"selected": false,
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
