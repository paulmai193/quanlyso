import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { TransactionDetailsComponent } from './transaction-details.component';
import { TransactionDetailsDetailComponent } from './transaction-details-detail.component';
import { TransactionDetailsPopupComponent } from './transaction-details-dialog.component';
import { TransactionDetailsDeletePopupComponent } from './transaction-details-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class TransactionDetailsResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: PaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const transactionDetailsRoute: Routes = [
    {
        path: 'transaction-details',
        component: TransactionDetailsComponent,
        resolve: {
            'pagingParams': TransactionDetailsResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.transactionDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'transaction-details/:id',
        component: TransactionDetailsDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.transactionDetails.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const transactionDetailsPopupRoute: Routes = [
    {
        path: 'transaction-details-new',
        component: TransactionDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.transactionDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transaction-details/:id/edit',
        component: TransactionDetailsPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.transactionDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'transaction-details/:id/delete',
        component: TransactionDetailsDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'quanlysoApp.transactionDetails.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
