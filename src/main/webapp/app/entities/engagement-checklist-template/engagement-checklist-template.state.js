(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('engagement-checklist-template', {
            parent: 'entity',
            url: '/engagement-checklist-template?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.engagementChecklistTemplate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/engagement-checklist-template/engagement-checklist-templates.html',
                    controller: 'EngagementChecklistTemplateController',
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
                    $translatePartialLoader.addPart('engagementChecklistTemplate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('engagement-checklist-template-detail', {
            parent: 'entity',
            url: '/engagement-checklist-template/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.engagementChecklistTemplate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/engagement-checklist-template/engagement-checklist-template-detail.html',
                    controller: 'EngagementChecklistTemplateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('engagementChecklistTemplate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EngagementChecklistTemplate', function($stateParams, EngagementChecklistTemplate) {
                    return EngagementChecklistTemplate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'engagement-checklist-template',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('engagement-checklist-template-detail.edit', {
            parent: 'engagement-checklist-template-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-checklist-template/engagement-checklist-template-dialog.html',
                    controller: 'EngagementChecklistTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EngagementChecklistTemplate', function(EngagementChecklistTemplate) {
                            return EngagementChecklistTemplate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('engagement-checklist-template.new', {
            parent: 'engagement-checklist-template',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-checklist-template/engagement-checklist-template-dialog.html',
                    controller: 'EngagementChecklistTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('engagement-checklist-template', null, { reload: 'engagement-checklist-template' });
                }, function() {
                    $state.go('engagement-checklist-template');
                });
            }]
        })
        .state('engagement-checklist-template.edit', {
            parent: 'engagement-checklist-template',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-checklist-template/engagement-checklist-template-dialog.html',
                    controller: 'EngagementChecklistTemplateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EngagementChecklistTemplate', function(EngagementChecklistTemplate) {
                            return EngagementChecklistTemplate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('engagement-checklist-template', null, { reload: 'engagement-checklist-template' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('engagement-checklist-template.delete', {
            parent: 'engagement-checklist-template',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/engagement-checklist-template/engagement-checklist-template-delete-dialog.html',
                    controller: 'EngagementChecklistTemplateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EngagementChecklistTemplate', function(EngagementChecklistTemplate) {
                            return EngagementChecklistTemplate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('engagement-checklist-template', null, { reload: 'engagement-checklist-template' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
