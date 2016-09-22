(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('checklist-template', {
            parent: 'entity',
            url: '/checklist-template?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistTemplate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-template/checklist-templates.html',
                    controller: 'ChecklistTemplateController',
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
                    $translatePartialLoader.addPart('checklistTemplate');
                    $translatePartialLoader.addPart('checklistTemplateStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('checklist-template-detail', {
            parent: 'entity',
            url: '/checklist-template/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistTemplate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-template/checklist-template-detail.html',
                    controller: 'ChecklistTemplateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('checklistTemplate');
                    $translatePartialLoader.addPart('checklistTemplateStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChecklistTemplate', function($stateParams, ChecklistTemplate) {
                    return ChecklistTemplate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'checklist-template',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('checklist-template-detail.edit', {
            parent: 'checklist-template-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-template/checklist-template-dialog.html',
                    controller: 'ChecklistTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistTemplate', function(ChecklistTemplate) {
                            return ChecklistTemplate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-template.new', {
            parent: 'checklist-template',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-template/checklist-template-dialog.html',
                    controller: 'ChecklistTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                version: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('checklist-template', null, { reload: 'checklist-template' });
                }, function() {
                    $state.go('checklist-template');
                });
            }]
        })
        .state('checklist-template.edit', {
            parent: 'checklist-template',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-template/checklist-template-dialog.html',
                    controller: 'ChecklistTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistTemplate', function(ChecklistTemplate) {
                            return ChecklistTemplate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-template', null, { reload: 'checklist-template' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-template.delete', {
            parent: 'checklist-template',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-template/checklist-template-delete-dialog.html',
                    controller: 'ChecklistTemplateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChecklistTemplate', function(ChecklistTemplate) {
                            return ChecklistTemplate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-template', null, { reload: 'checklist-template' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
