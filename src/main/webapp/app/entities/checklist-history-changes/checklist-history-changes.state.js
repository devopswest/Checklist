(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('checklist-history-changes', {
            parent: 'entity',
            url: '/checklist-history-changes?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistHistoryChanges.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-history-changes/checklist-history-changes.html',
                    controller: 'ChecklistHistoryChangesController',
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
                    $translatePartialLoader.addPart('checklistHistoryChanges');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('checklist-history-changes-detail', {
            parent: 'entity',
            url: '/checklist-history-changes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistHistoryChanges.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-history-changes/checklist-history-changes-detail.html',
                    controller: 'ChecklistHistoryChangesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('checklistHistoryChanges');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChecklistHistoryChanges', function($stateParams, ChecklistHistoryChanges) {
                    return ChecklistHistoryChanges.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'checklist-history-changes',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('checklist-history-changes-detail.edit', {
            parent: 'checklist-history-changes-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-history-changes/checklist-history-changes-dialog.html',
                    controller: 'ChecklistHistoryChangesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistHistoryChanges', function(ChecklistHistoryChanges) {
                            return ChecklistHistoryChanges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-history-changes.new', {
            parent: 'checklist-history-changes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-history-changes/checklist-history-changes-dialog.html',
                    controller: 'ChecklistHistoryChangesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                happened: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('checklist-history-changes', null, { reload: 'checklist-history-changes' });
                }, function() {
                    $state.go('checklist-history-changes');
                });
            }]
        })
        .state('checklist-history-changes.edit', {
            parent: 'checklist-history-changes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-history-changes/checklist-history-changes-dialog.html',
                    controller: 'ChecklistHistoryChangesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistHistoryChanges', function(ChecklistHistoryChanges) {
                            return ChecklistHistoryChanges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-history-changes', null, { reload: 'checklist-history-changes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-history-changes.delete', {
            parent: 'checklist-history-changes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-history-changes/checklist-history-changes-delete-dialog.html',
                    controller: 'ChecklistHistoryChangesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChecklistHistoryChanges', function(ChecklistHistoryChanges) {
                            return ChecklistHistoryChanges.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-history-changes', null, { reload: 'checklist-history-changes' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
