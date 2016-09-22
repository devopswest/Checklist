'use strict';

describe('Controller Tests', function() {

    describe('DisclosureRequirementTag Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDisclosureRequirementTag, MockTaxonomy, MockDisclosureRequirement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDisclosureRequirementTag = jasmine.createSpy('MockDisclosureRequirementTag');
            MockTaxonomy = jasmine.createSpy('MockTaxonomy');
            MockDisclosureRequirement = jasmine.createSpy('MockDisclosureRequirement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DisclosureRequirementTag': MockDisclosureRequirementTag,
                'Taxonomy': MockTaxonomy,
                'DisclosureRequirement': MockDisclosureRequirement
            };
            createController = function() {
                $injector.get('$controller')("DisclosureRequirementTagDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:disclosureRequirementTagUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
