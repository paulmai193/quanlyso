import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ProfitFactorComponent } from './profit-factor.component';
import { ProfitFactorDetailComponent } from './profit-factor-detail.component';
import { ProfitFactorPopupComponent } from './profit-factor-dialog.component';
import { ProfitFactorDeletePopupComponent } from './profit-factor-delete-dialog.component';

import { Principal } from '../../shared';

export const profitFactorRoute: Routes = [
    {
        path: 'profit-factor',
        component: ProfitFactorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.profitFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'profit-factor/:id',
        component: ProfitFactorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.profitFactor.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const profitFactorPopupRoute: Routes = [
    {
        path: 'profit-factor-new',
        component: ProfitFactorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.profitFactor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'profit-factor/:id/edit',
        component: ProfitFactorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.profitFactor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'profit-factor/:id/delete',
        component: ProfitFactorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.profitFactor.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
