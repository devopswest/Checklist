(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('audit-profile', {
            parent: 'entity',
            url: '/audit-profile?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.auditProfile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-profile/audit-profiles.html',
                    controller: 'AuditProfileController',
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
                    $translatePartialLoader.addPart('auditProfile');
                    $translatePartialLoader.addPart('responseStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('audit-profile-detail', {
            parent: 'entity',
            url: '/audit-profile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.auditProfile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-profile/audit-profile-detail.html',
                    controller: 'AuditProfileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auditProfile');
                    $translatePartialLoader.addPart('responseStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AuditProfile', function($stateParams, AuditProfile) {
                    return AuditProfile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'audit-profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('audit-profile-detail.edit', {
            parent: 'audit-profile-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile/audit-profile-dialog.html',
                    controller: 'AuditProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditProfile', function(AuditProfile) {
                            return AuditProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-profile.new', {
            parent: 'audit-profile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile/audit-profile-dialog.html',
                    controller: 'AuditProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('audit-profile', null, { reload: 'audit-profile' });
                }, function() {
                    $state.go('audit-profile');
                });
            }]
        })
        .state('audit-profile.edit', {
            parent: 'audit-profile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile/audit-profile-dialog.html',
                    controller: 'AuditProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditProfile', function(AuditProfile) {
                            return AuditProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-profile', null, { reload: 'audit-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-profile.delete', {
            parent: 'audit-profile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-profile/audit-profile-delete-dialog.html',
                    controller: 'AuditProfileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuditProfile', function(AuditProfile) {
                            return AuditProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-profile', null, { reload: 'audit-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
