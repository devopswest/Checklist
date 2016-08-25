(function() {
    'use strict';

    angular
        .module('checklistApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('checklist-question', {
            parent: 'entity',
            url: '/checklist-question?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistQuestion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-question/checklist-questions.html',
                    controller: 'ChecklistQuestionController',
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
                    $translatePartialLoader.addPart('checklistQuestion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('checklist-question-detail', {
            parent: 'entity',
            url: '/checklist-question/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'checklistApp.checklistQuestion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/checklist-question/checklist-question-detail.html',
                    controller: 'ChecklistQuestionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('checklistQuestion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ChecklistQuestion', function($stateParams, ChecklistQuestion) {
                    return ChecklistQuestion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'checklist-question',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('checklist-question-detail.edit', {
            parent: 'checklist-question-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-question/checklist-question-dialog.html',
                    controller: 'ChecklistQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistQuestion', function(ChecklistQuestion) {
                            return ChecklistQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-question.new', {
            parent: 'checklist-question',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-question/checklist-question-dialog.html',
                    controller: 'ChecklistQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                code: null,
                                question: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('checklist-question', null, { reload: 'checklist-question' });
                }, function() {
                    $state.go('checklist-question');
                });
            }]
        })
        .state('checklist-question.edit', {
            parent: 'checklist-question',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-question/checklist-question-dialog.html',
                    controller: 'ChecklistQuestionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ChecklistQuestion', function(ChecklistQuestion) {
                            return ChecklistQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-question', null, { reload: 'checklist-question' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('checklist-question.delete', {
            parent: 'checklist-question',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/checklist-question/checklist-question-delete-dialog.html',
                    controller: 'ChecklistQuestionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ChecklistQuestion', function(ChecklistQuestion) {
                            return ChecklistQuestion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('checklist-question', null, { reload: 'checklist-question' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
