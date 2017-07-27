import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { FactorComponent } from './factor.component';
import { FactorDetailComponent } from './factor-detail.component';
import { FactorPopupComponent } from './factor-dialog.component';
import { FactorDeletePopupComponent } from './factor-delete-dialog.component';

import { Principal } from '../../shared';

export const factorRoute: Routes = [
    {
        path: 'factor',
        component: FactorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.factor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'factor/:id',
        component: FactorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.factor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const factorPopupRoute: Routes = [
    {
        path: 'factor-new',
        component: FactorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.factor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'factor/:id/edit',
        component: FactorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.factor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'factor/:id/delete',
        component: FactorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.factor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
