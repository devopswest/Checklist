(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('checklist', {
            parent: 'entity',
            url: '/checklist?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklist.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist/checklists.html',
                    controller: 'ChecklistController',
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
                    $translatePartialLoader.addPart('checklist');
                    $translatePartialLoader.addPart('checklistStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('checklist-detail', {
            parent: 'entity',
            url: '/checklist/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklist.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist/checklist-detail.html',
                    controller: 'ChecklistDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('checklist');
                    $translatePartialLoader.addPart('checklistStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Checklist', function($stateParams, Checklist) {
                    return Checklist.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'checklist',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('checklist-detail.edit', {
            parent: 'checklist-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist/checklist-dialog.html',
                    controller: 'ChecklistDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Checklist', function(Checklist) {
                            return Checklist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist.new', {
            parent: 'checklist',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist/checklist-dialog.html',
                    controller: 'ChecklistDialogController',
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
                    $state.go('checklist', null, { reload: 'checklist' });
                }, function() {
                    $state.go('checklist');
                });
            }]
        })
        .state('checklist.edit', {
            parent: 'checklist',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist/checklist-dialog.html',
                    controller: 'ChecklistDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Checklist', function(Checklist) {
                            return Checklist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist', null, { reload: 'checklist' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist.delete', {
            parent: 'checklist',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist/checklist-delete-dialog.html',
                    controller: 'ChecklistDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Checklist', function(Checklist) {
                            return Checklist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist', null, { reload: 'checklist' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
