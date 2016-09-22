(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('notification', {
            parent: 'entity',
            url: '/notification?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.notification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notifications.html',
                    controller: 'NotificationController',
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
                    $translatePartialLoader.addPart('notification');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('notification-detail', {
            parent: 'entity',
            url: '/notification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.notification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/notification/notification-detail.html',
                    controller: 'NotificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('notification');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Notification', function($stateParams, Notification) {
                    return Notification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'notification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('notification-detail.edit', {
            parent: 'notification-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-dialog.html',
                    controller: 'NotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Notification', function(Notification) {
                            return Notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification.new', {
            parent: 'notification',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-dialog.html',
                    controller: 'NotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                content: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('notification', null, { reload: 'notification' });
                }, function() {
                    $state.go('notification');
                });
            }]
        })
        .state('notification.edit', {
            parent: 'notification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-dialog.html',
                    controller: 'NotificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Notification', function(Notification) {
                            return Notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification', null, { reload: 'notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('notification.delete', {
            parent: 'notification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/notification/notification-delete-dialog.html',
                    controller: 'NotificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Notification', function(Notification) {
                            return Notification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('notification', null, { reload: 'notification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
