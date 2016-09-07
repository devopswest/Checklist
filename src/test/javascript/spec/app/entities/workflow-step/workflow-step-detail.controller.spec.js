'use strict';

describe('Controller Tests', function() {

    describe('WorkflowStep Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockWorkflowStep, MockTemplate, MockWorkflow;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockWorkflowStep = jasmine.createSpy('MockWorkflowStep');
            MockTemplate = jasmine.createSpy('MockTemplate');
            MockWorkflow = jasmine.createSpy('MockWorkflow');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'WorkflowStep': MockWorkflowStep,
                'Template': MockTemplate,
                'Workflow': MockWorkflow
            };
            createController = function() {
                $injector.get('$controller')("WorkflowStepDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:workflowStepUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
