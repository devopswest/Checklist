(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('workflow-step', {
            parent: 'entity',
            url: '/workflow-step?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.workflowStep.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/workflow-step/workflow-steps.html',
                    controller: 'WorkflowStepController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workflowStep');
                    $translatePartialLoader.addPart('applicationAuthorities');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('workflow-step-detail', {
            parent: 'entity',
            url: '/workflow-step/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.workflowStep.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/workflow-step/workflow-step-detail.html',
                    controller: 'WorkflowStepDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('workflowStep');
                    $translatePartialLoader.addPart('applicationAuthorities');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'WorkflowStep', function($stateParams, WorkflowStep) {
                    return WorkflowStep.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'workflow-step',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('workflow-step-detail.edit', {
            parent: 'workflow-step-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/workflow-step/workflow-step-dialog.html',
                    controller: 'WorkflowStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkflowStep', function(WorkflowStep) {
                            return WorkflowStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('workflow-step.new', {
            parent: 'workflow-step',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/workflow-step/workflow-step-dialog.html',
                    controller: 'WorkflowStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                authority: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('workflow-step', null, { reload: 'workflow-step' });
                }, function() {
                    $state.go('workflow-step');
                });
            }]
        })
        .state('workflow-step.edit', {
            parent: 'workflow-step',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/workflow-step/workflow-step-dialog.html',
                    controller: 'WorkflowStepDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['WorkflowStep', function(WorkflowStep) {
                            return WorkflowStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('workflow-step', null, { reload: 'workflow-step' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('workflow-step.delete', {
            parent: 'workflow-step',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/workflow-step/workflow-step-delete-dialog.html',
                    controller: 'WorkflowStepDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['WorkflowStep', function(WorkflowStep) {
                            return WorkflowStep.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('workflow-step', null, { reload: 'workflow-step' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
