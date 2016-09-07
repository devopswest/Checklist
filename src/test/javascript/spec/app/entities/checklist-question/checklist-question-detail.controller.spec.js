'use strict';

describe('Controller Tests', function() {

    describe('ChecklistQuestion Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChecklistQuestion, MockChecklist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChecklistQuestion = jasmine.createSpy('MockChecklistQuestion');
            MockChecklist = jasmine.createSpy('MockChecklist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ChecklistQuestion': MockChecklistQuestion,
                'Checklist': MockChecklist
            };
            createController = function() {
                $injector.get('$controller')("ChecklistQuestionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:checklistQuestionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
