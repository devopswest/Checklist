(function() {
	'use strict';
	
	angular
    .module('checklistApp')
    .directive('adcMetadata', function () {
    	
    	var controller = ['$scope', function() {
    		var vmDir = this;
    		vmDir.toggle = function toggle(scope) {
    			scope.toggle();
    		};
    	}];
    	
    	return {
    		restrict: 'E',
    		scope: {
    			metadata: '=treeData'
    		},
    		controller: controller,
    		controllerAs: 'vmDir',
    		bindToController: true,
    		templateUrl: function(element, attr) {
    			if(attr.type == 'metadata') {
    				return 'app/components/custom-directives/metadata-tree.html';
    			}
    			else {
    				return 'app/components/custom-directives/metadata-tree.html';
    			}
    		}
    	};
    });	
})();


