import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    TransactionsService,
    TransactionsPopupService,
    TransactionsComponent,
    TransactionsDetailComponent,
    TransactionsDialogComponent,
    TransactionsPopupComponent,
    TransactionsDeletePopupComponent,
    TransactionsDeleteDialogComponent,
    transactionsRoute,
    transactionsPopupRoute,
    TransactionsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...transactionsRoute,
    ...transactionsPopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TransactionsComponent,
        TransactionsDetailComponent,
        TransactionsDialogComponent,
        TransactionsDeleteDialogComponent,
        TransactionsPopupComponent,
        TransactionsDeletePopupComponent,
    ],
    entryComponents: [
        TransactionsComponent,
        TransactionsDialogComponent,
        TransactionsPopupComponent,
        TransactionsDeleteDialogComponent,
        TransactionsDeletePopupComponent,
    ],
    providers: [
        TransactionsService,
        TransactionsPopupService,
        TransactionsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoTransactionsModule {}
