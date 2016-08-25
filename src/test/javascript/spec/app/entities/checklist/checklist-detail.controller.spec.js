'use strict';

describe('Controller Tests', function() {

    describe('Checklist Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChecklist, MockChecklistQuestion, MockAuditProfile, MockCountry;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChecklist = jasmine.createSpy('MockChecklist');
            MockChecklistQuestion = jasmine.createSpy('MockChecklistQuestion');
            MockAuditProfile = jasmine.createSpy('MockAuditProfile');
            MockCountry = jasmine.createSpy('MockCountry');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Checklist': MockChecklist,
                'ChecklistQuestion': MockChecklistQuestion,
                'AuditProfile': MockAuditProfile,
                'Country': MockCountry
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
