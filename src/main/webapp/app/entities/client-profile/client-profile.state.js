(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('client-profile', {
            parent: 'entity',
            url: '/client-profile?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientProfile.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-profile/client-profiles.html',
                    controller: 'ClientProfileController',
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
                    $translatePartialLoader.addPart('clientProfile');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('client-profile-detail', {
            parent: 'entity',
            url: '/client-profile/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.clientProfile.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/client-profile/client-profile-detail.html',
                    controller: 'ClientProfileDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('clientProfile');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ClientProfile', function($stateParams, ClientProfile) {
                    return ClientProfile.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'client-profile',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('client-profile-detail.edit', {
            parent: 'client-profile-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-profile/client-profile-dialog.html',
                    controller: 'ClientProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientProfile', function(ClientProfile) {
                            return ClientProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-profile.new', {
            parent: 'client-profile',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-profile/client-profile-dialog.html',
                    controller: 'ClientProfileDialogController',
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
                    $state.go('client-profile', null, { reload: 'client-profile' });
                }, function() {
                    $state.go('client-profile');
                });
            }]
        })
        .state('client-profile.edit', {
            parent: 'client-profile',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-profile/client-profile-dialog.html',
                    controller: 'ClientProfileDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ClientProfile', function(ClientProfile) {
                            return ClientProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-profile', null, { reload: 'client-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('client-profile.delete', {
            parent: 'client-profile',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/client-profile/client-profile-delete-dialog.html',
                    controller: 'ClientProfileDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ClientProfile', function(ClientProfile) {
                            return ClientProfile.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('client-profile', null, { reload: 'client-profile' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
