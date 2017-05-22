import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { QuanlysoSharedModule } from '../../shared';
import {
    TransactionDetailsService,
    TransactionDetailsPopupService,
    TransactionDetailsComponent,
    TransactionDetailsDetailComponent,
    TransactionDetailsDialogComponent,
    TransactionDetailsPopupComponent,
    TransactionDetailsDeletePopupComponent,
    TransactionDetailsDeleteDialogComponent,
    transactionDetailsRoute,
    transactionDetailsPopupRoute,
    TransactionDetailsResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...transactionDetailsRoute,
    ...transactionDetailsPopupRoute,
];

@NgModule({
    imports: [
        QuanlysoSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        TransactionDetailsComponent,
        TransactionDetailsDetailComponent,
        TransactionDetailsDialogComponent,
        TransactionDetailsDeleteDialogComponent,
        TransactionDetailsPopupComponent,
        TransactionDetailsDeletePopupComponent,
    ],
    entryComponents: [
        TransactionDetailsComponent,
        TransactionDetailsDialogComponent,
        TransactionDetailsPopupComponent,
        TransactionDetailsDeleteDialogComponent,
        TransactionDetailsDeletePopupComponent,
    ],
    providers: [
        TransactionDetailsService,
        TransactionDetailsPopupService,
        TransactionDetailsResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class QuanlysoTransactionDetailsModule {}
