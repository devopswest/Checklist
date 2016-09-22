'use strict';

describe('Controller Tests', function() {

    describe('Checklist Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChecklist, MockChecklistHistoryChanges, MockChecklistWorkflow, MockEngagement, MockUser, MockChecklistAnswer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChecklist = jasmine.createSpy('MockChecklist');
            MockChecklistHistoryChanges = jasmine.createSpy('MockChecklistHistoryChanges');
            MockChecklistWorkflow = jasmine.createSpy('MockChecklistWorkflow');
            MockEngagement = jasmine.createSpy('MockEngagement');
            MockUser = jasmine.createSpy('MockUser');
            MockChecklistAnswer = jasmine.createSpy('MockChecklistAnswer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Checklist': MockChecklist,
                'ChecklistHistoryChanges': MockChecklistHistoryChanges,
                'ChecklistWorkflow': MockChecklistWorkflow,
                'Engagement': MockEngagement,
                'User': MockUser,
                'ChecklistAnswer': MockChecklistAnswer
            };
            createController = function() {
                $injector.get('$controller')("ChecklistDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:checklistUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
