'use strict';

describe('Controller Tests', function() {

    describe('ChecklistWorkflow Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChecklistWorkflow, MockChecklist, MockUser, MockWorkflow, MockChecklistAnswer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChecklistWorkflow = jasmine.createSpy('MockChecklistWorkflow');
            MockChecklist = jasmine.createSpy('MockChecklist');
            MockUser = jasmine.createSpy('MockUser');
            MockWorkflow = jasmine.createSpy('MockWorkflow');
            MockChecklistAnswer = jasmine.createSpy('MockChecklistAnswer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ChecklistWorkflow': MockChecklistWorkflow,
                'Checklist': MockChecklist,
                'User': MockUser,
                'Workflow': MockWorkflow,
                'ChecklistAnswer': MockChecklistAnswer
            };
            createController = function() {
                $injector.get('$controller')("ChecklistWorkflowDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:checklistWorkflowUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
