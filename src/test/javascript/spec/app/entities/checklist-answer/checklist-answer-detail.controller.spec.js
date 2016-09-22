'use strict';

describe('Controller Tests', function() {

    describe('ChecklistAnswer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChecklistAnswer, MockDisclosureRequirement, MockChecklist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChecklistAnswer = jasmine.createSpy('MockChecklistAnswer');
            MockDisclosureRequirement = jasmine.createSpy('MockDisclosureRequirement');
            MockChecklist = jasmine.createSpy('MockChecklist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ChecklistAnswer': MockChecklistAnswer,
                'DisclosureRequirement': MockDisclosureRequirement,
                'Checklist': MockChecklist
            };
            createController = function() {
                $injector.get('$controller')("ChecklistAnswerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:checklistAnswerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
