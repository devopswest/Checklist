(function() {
	'use strict';
	
	angular
    .module('checklistApp')
    .directive('adcTaxonomy', function () {
    	
    	var controller = ['$scope', function($scope) {
    		var vmDir = this;
    		vmDir.load();
    	}];
    	
    	return {
    		restrict: 'E',
    		controller: controller,
    		controllerAs: 'vmDir',
    		bindToController: true,
    		scope: {
    			selectedid: '=model',
    			taxonomies: '=optionsData',
    			load: '&loadData'
    		},
    		templateUrl: 'app/components/custom-directives/taxonomy-select.html'
    	};
    	
    });

})();
