(function() {
    'use strict';

    angular
        .module('smartinvestplusApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('titre', {
            parent: 'entity',
            url: '/titre?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.titre.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/titre/titres.html',
                    controller: 'TitreController',
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
                    $translatePartialLoader.addPart('titre');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('titre-detail', {
            parent: 'entity',
            url: '/titre/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'smartinvestplusApp.titre.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/titre/titre-detail.html',
                    controller: 'TitreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('titre');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Titre', function($stateParams, Titre) {
                    return Titre.get({id : $stateParams.id});
                }]
            }
        })
        .state('titre.new', {
            parent: 'titre',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/titre/titre-dialog.html',
                    controller: 'TitreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nom: null,
                                symbole: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('titre', null, { reload: true });
                }, function() {
                    $state.go('titre');
                });
            }]
        })
        .state('titre.edit', {
            parent: 'titre',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/titre/titre-dialog.html',
                    controller: 'TitreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Titre', function(Titre) {
                            return Titre.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('titre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('titre.delete', {
            parent: 'titre',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/titre/titre-delete-dialog.html',
                    controller: 'TitreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Titre', function(Titre) {
                            return Titre.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('titre', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
