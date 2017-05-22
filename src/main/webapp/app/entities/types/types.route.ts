import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { TypesComponent } from './types.component';
import { TypesDetailComponent } from './types-detail.component';
import { TypesPopupComponent } from './types-dialog.component';
import { TypesDeletePopupComponent } from './types-delete-dialog.component';

import { Principal } from '../../shared';

export const typesRoute: Routes = [
    {
        path: 'types',
        component: TypesComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.types.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'types/:id',
        component: TypesDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.types.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const typesPopupRoute: Routes = [
    {
        path: 'types-new',
        component: TypesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.types.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'types/:id/edit',
        component: TypesPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.types.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'types/:id/delete',
        component: TypesDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.types.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
