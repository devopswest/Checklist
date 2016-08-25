(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('audit-question-response', {
            parent: 'entity',
            url: '/audit-question-response?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.auditQuestionResponse.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-question-response/audit-question-responses.html',
                    controller: 'AuditQuestionResponseController',
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
                    $translatePartialLoader.addPart('auditQuestionResponse');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('audit-question-response-detail', {
            parent: 'entity',
            url: '/audit-question-response/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.auditQuestionResponse.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/audit-question-response/audit-question-response-detail.html',
                    controller: 'AuditQuestionResponseDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('auditQuestionResponse');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AuditQuestionResponse', function($stateParams, AuditQuestionResponse) {
                    return AuditQuestionResponse.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'audit-question-response',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('audit-question-response-detail.edit', {
            parent: 'audit-question-response-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-question-response/audit-question-response-dialog.html',
                    controller: 'AuditQuestionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditQuestionResponse', function(AuditQuestionResponse) {
                            return AuditQuestionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-question-response.new', {
            parent: 'audit-question-response',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-question-response/audit-question-response-dialog.html',
                    controller: 'AuditQuestionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                questionResponse: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('audit-question-response', null, { reload: 'audit-question-response' });
                }, function() {
                    $state.go('audit-question-response');
                });
            }]
        })
        .state('audit-question-response.edit', {
            parent: 'audit-question-response',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-question-response/audit-question-response-dialog.html',
                    controller: 'AuditQuestionResponseDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AuditQuestionResponse', function(AuditQuestionResponse) {
                            return AuditQuestionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-question-response', null, { reload: 'audit-question-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('audit-question-response.delete', {
            parent: 'audit-question-response',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/audit-question-response/audit-question-response-delete-dialog.html',
                    controller: 'AuditQuestionResponseDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AuditQuestionResponse', function(AuditQuestionResponse) {
                            return AuditQuestionResponse.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('audit-question-response', null, { reload: 'audit-question-response' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
