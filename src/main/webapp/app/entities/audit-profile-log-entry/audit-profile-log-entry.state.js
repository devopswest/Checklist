(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('audit-profile-log-entry', {
            parent: 'entity',
            url: '/audit-profile-log-entry?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.auditProfileLogEntry.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-profile-log-entry/audit-profile-log-entries.html',
                    controller: 'AuditProfileLogEntryController',
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
                    $translatePartialLoader.addPart('auditProfileLogEntry');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('audit-profile-log-entry-detail', {
            parent: 'entity',
            url: '/audit-profile-log-entry/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.auditProfileLogEntry.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-profile-log-entry/audit-profile-log-entry-detail.html',
                    controller: 'AuditProfileLogEntryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auditProfileLogEntry');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AuditProfileLogEntry', function($stateParams, AuditProfileLogEntry) {
                    return AuditProfileLogEntry.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'audit-profile-log-entry',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('audit-profile-log-entry-detail.edit', {
            parent: 'audit-profile-log-entry-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile-log-entry/audit-profile-log-entry-dialog.html',
                    controller: 'AuditProfileLogEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditProfileLogEntry', function(AuditProfileLogEntry) {
                            return AuditProfileLogEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-profile-log-entry.new', {
            parent: 'audit-profile-log-entry',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile-log-entry/audit-profile-log-entry-dialog.html',
                    controller: 'AuditProfileLogEntryDialogController',
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
                    $state.go('audit-profile-log-entry', null, { reload: 'audit-profile-log-entry' });
                }, function() {
                    $state.go('audit-profile-log-entry');
                });
            }]
        })
        .state('audit-profile-log-entry.edit', {
            parent: 'audit-profile-log-entry',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile-log-entry/audit-profile-log-entry-dialog.html',
                    controller: 'AuditProfileLogEntryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditProfileLogEntry', function(AuditProfileLogEntry) {
                            return AuditProfileLogEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-profile-log-entry', null, { reload: 'audit-profile-log-entry' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-profile-log-entry.delete', {
            parent: 'audit-profile-log-entry',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile-log-entry/audit-profile-log-entry-delete-dialog.html',
                    controller: 'AuditProfileLogEntryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuditProfileLogEntry', function(AuditProfileLogEntry) {
                            return AuditProfileLogEntry.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-profile-log-entry', null, { reload: 'audit-profile-log-entry' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
