'use strict';

describe('Controller Tests', function() {

    describe('ChecklistHistoryChanges Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockChecklistHistoryChanges, MockChecklist, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockChecklistHistoryChanges = jasmine.createSpy('MockChecklistHistoryChanges');
            MockChecklist = jasmine.createSpy('MockChecklist');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ChecklistHistoryChanges': MockChecklistHistoryChanges,
                'Checklist': MockChecklist,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ChecklistHistoryChangesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:checklistHistoryChangesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
