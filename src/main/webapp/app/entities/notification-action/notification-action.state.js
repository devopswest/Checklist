(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification-action', {
            parent: 'entity',
            url: '/notification-action?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.notificationAction.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-action/notification-actions.html',
                    controller: 'NotificationActionController',
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
                    $translatePartialLoader.addPart('notificationAction');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notification-action-detail', {
            parent: 'entity',
            url: '/notification-action/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.notificationAction.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification-action/notification-action-detail.html',
                    controller: 'NotificationActionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notificationAction');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NotificationAction', function($stateParams, NotificationAction) {
                    return NotificationAction.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notification-action',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('notification-action-detail.edit', {
            parent: 'notification-action-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-action/notification-action-dialog.html',
                    controller: 'NotificationActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationAction', function(NotificationAction) {
                            return NotificationAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-action.new', {
            parent: 'notification-action',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-action/notification-action-dialog.html',
                    controller: 'NotificationActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                action: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification-action', null, { reload: 'notification-action' });
                }, function() {
                    $state.go('notification-action');
                });
            }]
        })
        .state('notification-action.edit', {
            parent: 'notification-action',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-action/notification-action-dialog.html',
                    controller: 'NotificationActionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NotificationAction', function(NotificationAction) {
                            return NotificationAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-action', null, { reload: 'notification-action' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification-action.delete', {
            parent: 'notification-action',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification-action/notification-action-delete-dialog.html',
                    controller: 'NotificationActionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NotificationAction', function(NotificationAction) {
                            return NotificationAction.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification-action', null, { reload: 'notification-action' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
