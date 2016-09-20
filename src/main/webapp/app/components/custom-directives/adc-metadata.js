(function() {
	'use strict';
	
	angular
    .module('checklistApp')
    .directive('adcMetadata', function () {
    	return {
    		restrict: 'E',
    		scope: {
    			metadata: '=treeData',
    			toggle: '&onToggle'
    		},
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


