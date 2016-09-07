'use strict';

describe('Controller Tests', function() {

    describe('ClientMetadata Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockClientMetadata, MockTaxonomy, MockClientProfile;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockClientMetadata = jasmine.createSpy('MockClientMetadata');
            MockTaxonomy = jasmine.createSpy('MockTaxonomy');
            MockClientProfile = jasmine.createSpy('MockClientProfile');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ClientMetadata': MockClientMetadata,
                'Taxonomy': MockTaxonomy,
                'ClientProfile': MockClientProfile
            };
            createController = function() {
                $injector.get('$controller')("ClientMetadataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'checklistApp:clientMetadataUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
