(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('checklist-workflow', {
            parent: 'entity',
            url: '/checklist-workflow?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistWorkflow.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-workflow/checklist-workflows.html',
                    controller: 'ChecklistWorkflowController',
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
                    $translatePartialLoader.addPart('checklistWorkflow');
                    $translatePartialLoader.addPart('workflowTaskStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('checklist-workflow-detail', {
            parent: 'entity',
            url: '/checklist-workflow/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistWorkflow.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-workflow/checklist-workflow-detail.html',
                    controller: 'ChecklistWorkflowDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('checklistWorkflow');
                    $translatePartialLoader.addPart('workflowTaskStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChecklistWorkflow', function($stateParams, ChecklistWorkflow) {
                    return ChecklistWorkflow.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'checklist-workflow',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('checklist-workflow-detail.edit', {
            parent: 'checklist-workflow-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-workflow/checklist-workflow-dialog.html',
                    controller: 'ChecklistWorkflowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistWorkflow', function(ChecklistWorkflow) {
                            return ChecklistWorkflow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-workflow.new', {
            parent: 'checklist-workflow',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-workflow/checklist-workflow-dialog.html',
                    controller: 'ChecklistWorkflowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                happened: null,
                                status: null,
                                comments: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('checklist-workflow', null, { reload: 'checklist-workflow' });
                }, function() {
                    $state.go('checklist-workflow');
                });
            }]
        })
        .state('checklist-workflow.edit', {
            parent: 'checklist-workflow',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-workflow/checklist-workflow-dialog.html',
                    controller: 'ChecklistWorkflowDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistWorkflow', function(ChecklistWorkflow) {
                            return ChecklistWorkflow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-workflow', null, { reload: 'checklist-workflow' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-workflow.delete', {
            parent: 'checklist-workflow',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-workflow/checklist-workflow-delete-dialog.html',
                    controller: 'ChecklistWorkflowDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChecklistWorkflow', function(ChecklistWorkflow) {
                            return ChecklistWorkflow.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-workflow', null, { reload: 'checklist-workflow' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
