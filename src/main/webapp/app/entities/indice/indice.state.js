(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('indice', {
            parent: 'entity',
            url: '/indice?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.indice.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/indice/indices.html',
                    controller: 'IndiceController',
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
                    $translatePartialLoader.addPart('indice');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('indice-detail', {
            parent: 'entity',
            url: '/indice/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.indice.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/indice/indice-detail.html',
                    controller: 'IndiceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('indice');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Indice', function($stateParams, Indice) {
                    return Indice.get({id : $stateParams.id});
                }]
            }
        })
        .state('indice.new', {
            parent: 'indice',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/indice/indice-dialog.html',
                    controller: 'IndiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                top: null,
                                composite: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('indice', null, { reload: true });
                }, function() {
                    $state.go('indice');
                });
            }]
        })
        .state('indice.edit', {
            parent: 'indice',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/indice/indice-dialog.html',
                    controller: 'IndiceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Indice', function(Indice) {
                            return Indice.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('indice', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('indice.delete', {
            parent: 'indice',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/indice/indice-delete-dialog.html',
                    controller: 'IndiceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Indice', function(Indice) {
                            return Indice.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('indice', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
