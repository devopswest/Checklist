'use strict';

describe('Controller Tests', function() {

    describe('DisclosureRequirement Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDisclosureRequirement, MockDisclosureRequirementTag, MockChecklistTemplate, MockRequirement;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDisclosureRequirement = jasmine.createSpy('MockDisclosureRequirement');
            MockDisclosureRequirementTag = jasmine.createSpy('MockDisclosureRequirementTag');
            MockChecklistTemplate = jasmine.createSpy('MockChecklistTemplate');
            MockRequirement = jasmine.createSpy('MockRequirement');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DisclosureRequirement': MockDisclosureRequirement,
                'DisclosureRequirementTag': MockDisclosureRequirementTag,
                'ChecklistTemplate': MockChecklistTemplate,
                'Requirement': MockRequirement
            };
            createController = function() {
                $injector.get('$controller')("DisclosureRequirementDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:disclosureRequirementUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
